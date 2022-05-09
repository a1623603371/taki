package com.taki.order.service.impl;



import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.taki.common.constants.RedisLockKeyConstants;
import com.taki.common.constants.RocketMQConstant;
import com.taki.common.enums.*;
import com.taki.common.exception.ServiceException;
import com.taki.common.message.ActualRefundMessage;
import com.taki.common.redis.RedisLock;
import com.taki.common.utlis.ObjectUtil;
import com.taki.common.utlis.ParamCheckUtil;
import com.taki.common.utlis.RandomUtil;
import com.taki.common.utlis.ResponseData;
import com.taki.customer.api.CustomerApi;
import com.taki.customer.domain.request.CustomReviewReturnGoodsRequest;
import com.taki.fulfill.api.FulFillApi;
import com.taki.fulfill.domain.request.CancelFulfillRequest;
import com.taki.market.request.CancelOrderReleaseUserCouponRequest;
import com.taki.order.dao.*;
import com.taki.order.domain.dto.*;
import com.taki.order.domain.entity.*;
import com.taki.order.domain.request.*;
import com.taki.order.enums.*;
import com.taki.order.exception.OrderBizException;
import com.taki.order.exception.OrderErrorCodeEnum;
import com.taki.order.manager.OrderNoManager;
import com.taki.order.mq.producer.DefaultProducer;
import com.taki.order.service.OrderAfterSaleService;
import com.taki.pay.api.PayApi;
import com.taki.pay.domian.rquest.PayRefundRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName OrderAfterSaleServiceImpl
 * @Description 订单售后 service
 * @Author Long
 * @Date 2022/3/8 17:05
 * @Version 1.0
 */
@Service
@Slf4j
public class OrderAfterSaleServiceImpl implements OrderAfterSaleService {



    @Autowired
    private RedisLock redisLock;


    @Autowired
    private OrderInfoDao orderInfoDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private OrderAmountDao orderAmountDao;

    @Autowired
    private OrderOperateLogDao orderOperateLogDao;

    @Autowired
    private OrderPaymentDetailDao orderPaymentDetailDao;


    @Autowired
    private AfterSaleItemDao afterSaleItemDao;

    @Autowired
    private AfterSaleInfoDao afterSaleInfoDao;

    @Autowired
    private AfterSaleRefundDAO afterSaleRefundDAO;


    @Autowired
    private AfterSaleLogDAO afterSaleLogDAO;

    @Autowired
    private DefaultProducer defaultProducer;


    @Autowired
    private OrderNoManager orderNoManager;


    @Autowired
    private AfterSaleOperateLogFactory afterSaleOperateLogFactory;


    @DubboReference
    private FulFillApi fulFillApi;

    @DubboReference
    private CustomerApi customerApi;

    @DubboReference
    private PayApi payApi;

    private Integer returnGoodsItemNum = 1;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelOrder(CancelOrderRequest cancelOrderRequest) {

        // 入参检查
        checkCancelOrderRequestPram(cancelOrderRequest);
        //分布式锁
        String orderId = cancelOrderRequest.getOrderId();

        String key = RedisLockKeyConstants.CANCEL_KEY + orderId;

        try {
            Boolean lock = redisLock.lock(key);

            if (!lock){
                throw new OrderBizException(OrderErrorCodeEnum.CANCEL_ORDER_REPEAT);
            }

            // 执行取消订单
            executeCancelOrder(cancelOrderRequest,orderId);

            return true;
        }catch (Exception e){
            throw new OrderBizException(e.getMessage());
        }
        finally {
            redisLock.unLock(key);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean processApplyAfterSale(ReturnGoodsOrderRequest request) {

        // 1.参数效验
        checkAfterSaleRequestPram(request);

        // redis 分布式锁
        String orderId = request.getOrderId();

        String key = RedisLockKeyConstants.REFUND_KEY + orderId;
        Boolean lock = redisLock.lock(key);
        if (!lock){
            throw new OrderBizException(OrderErrorCodeEnum.PROCESS_AFTER_SALE_RETURN_GOODS);
        }

        try {
            //1.售后单状态验证
            String skuCode = request.getSkuCode();

            List<AfterSaleItemDO> afterSaleItems = afterSaleItemDao.getOrderIdAndSkuCode(orderId,skuCode);

            if (!afterSaleItems.isEmpty()){
                Long afterSaleId = afterSaleItems.get(0).getAfterSaleId();
                // 用售后 id 查询 支付表
                AfterSaleRefundDO afterSaleRefundDO =  afterSaleRefundDAO.getByAfterSaleId(afterSaleId);
                // 幂等效验： 售后支付表里存在当前这笔未退款的记录 不能重复发起售后
                if (orderId.equals(afterSaleRefundDO.getOrderId()) &&
                        RefundStatusEnum.UN_REFUND.getCode().equals(afterSaleRefundDO.getRefundStatus())){
                    throw new OrderBizException(OrderErrorCodeEnum.PROCESS_APPLY_AFTER_SALE_CANNOT_REPEAT);
                }

                //  业务 效应： 完成支付退款的订单不能再次重复发 起手动售后
                AfterSaleInfoDO afterSaleInfoDO = afterSaleInfoDao.getByAfterSaleId(afterSaleId);

                if (afterSaleInfoDO.getAfterSaleStatus() > AfterSaleStatusEnum.REFUNDING.getCode()){
                    throw new OrderBizException(OrderErrorCodeEnum.PROCESS_APPLY_AFTER_SALE_CANNOT_REPEAT);
                }

            }

            // 2.封装数据
            ReturnGoodsAssembleRequest returnGoodsAssembleRequest = buildReturnGoodsData(request);


            // 3.计算退款金额
            returnGoodsAssembleRequest = calculateReturnGoodsAmount(returnGoodsAssembleRequest);

            // 4. 售后数据落库
            insertReturnGoodsAfterSale(returnGoodsAssembleRequest,AfterSaleStatusEnum.COMMITED.getCode());

            // 5. 发起客服审核
            CustomReviewReturnGoodsRequest customReviewReturnGoodsRequest = returnGoodsAssembleRequest.clone(CustomReviewReturnGoodsRequest.class);

            customerApi.customerAudit(customReviewReturnGoodsRequest);
        }catch (ServiceException e){
            log.error("system error",e);
            return false;
        }finally {
            redisLock.unLock(key);
        }


        return true;
    }

    @Override
    @Transactional
    public  ResponseData<Boolean> refundMoney(ActualRefundMessage actualRefundMessage) {
        Long afterSaleId = actualRefundMessage.getAfterSaleId();
        String key = RedisLockKeyConstants.REFUND_KEY + afterSaleId;
        try {
            Boolean lock = redisLock.lock(key);
            if (!lock){
                throw new OrderBizException(OrderErrorCodeEnum.REFUND_MONEY_REPEAT);
            }
            AfterSaleInfoDO afterSaleInfoDO = afterSaleInfoDao.getByAfterSaleId(afterSaleId);
            Long afterSaleRefundId = actualRefundMessage.getAfterSaleRefundId();
            AfterSaleRefundDO afterSaleRefundDO = afterSaleRefundDAO.getById(afterSaleRefundId);

            //1.封装调用支付退款接口数据
            PayRefundRequest payRefundRequest = buildPayRefundRequest(actualRefundMessage,afterSaleRefundDO);

            //2.执行退款
            if(!payApi.executeRefund(payRefundRequest)){
                throw new OrderBizException(OrderErrorCodeEnum.ORDER_REFUND_AMOUNT_FAILED);
            }

            //3 售后记录状态
            // 执行退款信息更新售后信息
            updateAfterSaleStatus(afterSaleInfoDO,AfterSaleStatusEnum.REVIEW_PASS.getCode(),AfterSaleStatusEnum.REFUNDING.getCode());

            // 4.退款的最后一笔退优惠券
            if (actualRefundMessage.isLastReturnGoods()){
                //释放优惠券权益
                String orderId = actualRefundMessage.getOrderId();
                OrderInfoDO orderInfoDO = orderInfoDao.getByOrderId(orderId);
                CancelOrderReleaseUserCouponRequest cancelOrderReleaseUserCouponRequest =orderInfoDO.clone(CancelOrderReleaseUserCouponRequest.class);

                defaultProducer.sendMessage(RocketMQConstant.CANCEL_RELEASE_PROPERTY_TOPIC,
                        JSONObject.toJSONString(cancelOrderReleaseUserCouponRequest),"释放优惠券权益");
            }
        }finally {

            redisLock.unLock(key);
        }

        return ResponseData.success(true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseData<Boolean> processCancelOrder(CancelOrderAssembleRequest cancelOrderAssembleRequest) {

        String orderId = cancelOrderAssembleRequest.getOrderId();

        String key = RedisLockKeyConstants.REFUND_KEY + orderId;

        try {
            Boolean lock = redisLock.lock(key);
            if (!lock){
                throw new OrderBizException(OrderErrorCodeEnum.PROCESS_REFUND_FAILED);
            }

            // 执行退款 准备工作
            // 1 计算 取消订单 退款金额
            CancelOrderRefundAmountDTO cancelOrderRefundAmountDTO = calculateCancelOrderRefundAmount(cancelOrderAssembleRequest);
            cancelOrderAssembleRequest.setCancelOrderRefundAmount(cancelOrderRefundAmountDTO);

            //2 取消 订单操作 记录售后信息
            insertCancelOrderAfterSale(cancelOrderAssembleRequest,AfterSaleStatusEnum.REVIEW_PASS.getCode());

            // 3.发送退款MQ
            ActualRefundMessage actualRefundMessage = new ActualRefundMessage();
            actualRefundMessage.setAfterSaleRefundId(cancelOrderAssembleRequest.getAfterSaleRefundId());
            actualRefundMessage.setOrderId(orderId);
            actualRefundMessage.setLastReturnGoods(cancelOrderAssembleRequest.isLastReturnGoods());
            actualRefundMessage.setAfterSaleId(Long.valueOf(cancelOrderAssembleRequest.getAfterSaleId()));

            defaultProducer.sendMessage(RocketMQConstant.ACTUAL_REFUND_TOPIC,JSONObject.toJSONString(actualRefundMessage),"实际退款");
        }catch (Exception e){
            log.error(e.getMessage());
            throw new OrderBizException(OrderErrorCodeEnum.PROCESS_REFUND_FAILED);
        }
        finally {
            redisLock.unLock(key);

        }
        return ResponseData.success(true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean receivePaymentRefundCallback(RefundCallbackRequest refundCallbackRequest) {
        String orderId = refundCallbackRequest.getOrderId();

        String key = RedisLockKeyConstants.REFUND_KEY + orderId;

        boolean lock = redisLock.lock(key);

        if (!lock){
            throw new OrderBizException(OrderErrorCodeEnum.PROCESS_PAY_REFUND_CALLBACK_REPEAT);
        }
        // 1 入参检查
        checkedRefundCallbackPram(refundCallbackRequest);

        // 2. 获取第三方支付 退款 的放回结果
        Integer afterSaleStatus;
        Integer refundStatus;
        String refundStatusMsg;

        if (RefundStatusEnum.REFUND_SUCCESS.getCode().equals(refundCallbackRequest.getRefundStatus())){
            afterSaleStatus = AfterSaleStatusEnum.REFUNDED.getCode();
            refundStatus = RefundStatusEnum.REFUND_SUCCESS.getCode();
            refundStatusMsg =   RefundStatusEnum.REFUND_SUCCESS.getMsg();

        }else {
            afterSaleStatus = AfterSaleStatusEnum.FAILED.getCode();
            refundStatus = RefundStatusEnum.REFUND_FAIL.getCode();
            refundStatusMsg = RefundStatusEnum.REFUND_FAIL.getMsg();
        }

        // 3 .更新售后记录 支付退款 回调更新售后信息
        updatePaymentRefundCallbackAfterSale(refundCallbackRequest,afterSaleStatus,refundStatus,refundStatusMsg);


        // 4. 发送短信

        // 5. 发送 APP 通知
        return true;
    }

    @Override
    public Boolean receiveCustomerAuditResult(CustomerAuditAssembleRequest customerAuditAssembleRequest) {
        return null;
    }

    /**
     * @description: 更新售后记录 支付退款 回调更新售后信息
     * @param refundCallbackRequest 支付退款 回调请求 参数
     * @param  afterSaleStatus 售后单状态
     * @param refundStatus  退款状态
     * @param refundStatusMsg 退款状态
     * @return  void
     * @author Long
     * @date: 2022/4/6 18:05
     */
    private void updatePaymentRefundCallbackAfterSale(RefundCallbackRequest refundCallbackRequest, Integer afterSaleStatus, Integer refundStatus, String refundStatusMsg) {

        Long afterSaleId = Long.valueOf( refundCallbackRequest.getAfterSaleId());
        
        //更新 售后订单表
        afterSaleInfoDao.updateStatus(afterSaleId,AfterSaleStatusEnum.REFUNDING.getCode(),afterSaleStatus);

        // 新增 售后单

        AfterSaleInfoDO afterSaleInfoDO = afterSaleInfoDao.getByAfterSaleId(afterSaleId);

        AfterSaleLogDO afterSaleLogDO = afterSaleOperateLogFactory.get(afterSaleInfoDO  ,AfterSaleStatusChannelEnum.getBy(AfterSaleStatusEnum.REFUNDING.getCode(),refundStatus));

        afterSaleLogDAO.save(afterSaleLogDO);

        // 更新 售后 退款

        AfterSaleRefundDO afterSaleRefundDO = new AfterSaleRefundDO();
        afterSaleRefundDO.setAfterSaleId(refundCallbackRequest.getAfterSaleId());
        afterSaleRefundDO.setRefundStatus(refundStatus);
        afterSaleRefundDO.setRefundPayTime(refundCallbackRequest.getRefundTime());
        afterSaleRefundDO.setRemark(refundStatusMsg);

        afterSaleRefundDAO.updateAfterSaleRefundStatus(afterSaleRefundDO);


    }

    /**
     * @description:  检查 退款 支付回调 参
     * @param refundCallbackRequest 退款支付回调请求
     * @return  void
     * @author Long
     * @date: 2022/4/6 17:54
     */
    private void checkedRefundCallbackPram(RefundCallbackRequest refundCallbackRequest) {
        ParamCheckUtil.checkObjectNonNull(refundCallbackRequest);

        String orderId = refundCallbackRequest.getOrderId();
        ParamCheckUtil.checkStringNonEmpty(orderId, OrderErrorCodeEnum.CANCEL_ORDER_ID_IS_NULL);

        String batchNo = refundCallbackRequest.getBatchNo();
        ParamCheckUtil.checkStringNonEmpty(batchNo, OrderErrorCodeEnum.PROCESS_PAY_REFUND_CALLBACK_BATCH_NO_IS_NULL);

        Integer refundStatus = refundCallbackRequest.getRefundStatus();
        ParamCheckUtil.checkObjectNonNull(refundStatus, OrderErrorCodeEnum.PROCESS_PAY_REFUND_CALLBACK_STATUS_NO_IS_NUL);

        Integer refundFee = refundCallbackRequest.getRefundFee();
        ParamCheckUtil.checkObjectNonNull(refundFee, OrderErrorCodeEnum.PROCESS_PAY_REFUND_CALLBACK_FEE_NO_IS_NUL);

        Integer totalFee = refundCallbackRequest.getTotalFee();
        ParamCheckUtil.checkObjectNonNull(totalFee, OrderErrorCodeEnum.PROCESS_PAY_REFUND_CALLBACK_TOTAL_FEE_NO_IS_NUL);

        String sign = refundCallbackRequest.getSign();
        ParamCheckUtil.checkStringNonEmpty(sign, OrderErrorCodeEnum.PROCESS_PAY_REFUND_CALLBACK_SIGN_NO_IS_NUL);

        String tradeNo = refundCallbackRequest.getTradeNo();
        ParamCheckUtil.checkStringNonEmpty(tradeNo, OrderErrorCodeEnum.PROCESS_PAY_REFUND_CALLBACK_TRADE_NO_IS_NUL);

        String afterSaleId = refundCallbackRequest.getAfterSaleId();
        ParamCheckUtil.checkStringNonEmpty(afterSaleId, OrderErrorCodeEnum.PROCESS_PAY_REFUND_CALLBACK_AFTER_SALE_ID_IS_NULL);

        LocalDateTime refundTime = refundCallbackRequest.getRefundTime();
        ParamCheckUtil.checkObjectNonNull(refundTime, OrderErrorCodeEnum.PROCESS_PAY_REFUND_CALLBACK_AFTER_SALE_REFUND_TIME_IS_NULL);

        //  数据库中当前售后单不是未退款状态，表示已经退款成功 or 失败，那么本次就不能再执行支付回调退款
        AfterSaleRefundDO afterSaleByDatabase = afterSaleRefundDAO.getByAfterSaleId(Long.valueOf(afterSaleId));
        if (!RefundStatusEnum.UN_REFUND.getCode().equals(afterSaleByDatabase.getRefundStatus())) {
            throw new OrderBizException(OrderErrorCodeEnum.REPEAT_CALLBACK);
        }


    }

    /**
     * @description: 插入取消订单售后信息
     * @param cancelOrderAssembleRequest 取消订单 数据集合请求
     * @param afterSaleStatus 售后单状态
     * @return  void
     * @author Long
     * @date: 2022/4/5 20:03
     */
    private void insertCancelOrderAfterSale(CancelOrderAssembleRequest cancelOrderAssembleRequest, Integer afterSaleStatus) {
        OrderInfoDTO orderInfoDTO = cancelOrderAssembleRequest.getOrderInfo();
        OrderInfoDO orderInfoDO = orderInfoDTO.clone(OrderInfoDO.class);
        Integer afterSaleType = cancelOrderAssembleRequest.getAfterSaleType();
        Integer cancelOrderAfterSaleStatus = AfterSaleStatusEnum.REVIEW_PASS.getCode();

        //取消订单过程中的 申请退款金额 和实际金额 都是实际退款金额 金额相同
        AfterSaleInfoDO afterSaleInfoDO = new AfterSaleInfoDO();
        afterSaleInfoDO.setRealRefundAmount(orderInfoDO.getPayAmount());
        afterSaleInfoDO.setApplyRefundAmount(orderInfoDO.getPayAmount());

        //1.新增售后条目
      String afterSaleId =   insertCancelOrderAfterSaleInfoTable(orderInfoDO,afterSaleType,cancelOrderAfterSaleStatus,afterSaleInfoDO);
    }

    /**
     * @description: 新增售后条目
     * @param orderInfoDO 订单信息
     * @param afterSaleType 售后类型
     * @param cancelOrderAfterSaleStatus 取消订单售后单 状态
     * @param afterSaleInfoDO  售后 单 信息
     * @return  void
     * @author Long
     * @date: 2022/4/5 20:08
     */
    private String insertCancelOrderAfterSaleInfoTable(OrderInfoDO orderInfoDO, Integer afterSaleType, Integer cancelOrderAfterSaleStatus, AfterSaleInfoDO afterSaleInfoDO) {

        // 生成售后订单号
        String afterSaleId = orderNoManager.genOrderId(OrderNoTypeEnum.AFTER_SALE.getCode(),orderInfoDO.getUserId());
        afterSaleInfoDO.setAfterSaleId(afterSaleId);
        afterSaleInfoDO.setBusinessIdentifier(BusinessIdentifierEnum.SELF_MALL.getCode());
        afterSaleInfoDO.setOrderId(orderInfoDO.getOrderId());
        afterSaleInfoDO.setOrderSourceChannel(BusinessIdentifierEnum.SELF_MALL.getCode());
        afterSaleInfoDO.setUserId(orderInfoDO.getUserId());
        afterSaleInfoDO.setOrderType(OrderTypeEnum.NORMAL.getCode());
        afterSaleInfoDO.setApplyTime(LocalDateTime.now());
        afterSaleInfoDO.setAfterSaleStatus(cancelOrderAfterSaleStatus);

        // 取消订单整笔退
        afterSaleInfoDO.setAfterSaleType(AfterSaleTypeEnum.RETURN_MONEY.getCode());

        Integer cancelType = Integer.valueOf(orderInfoDO.getCancelType());

        if(OrderCancelTypeEnum.TIMEOUT_CANCELED.getCode().equals(cancelType)){
            afterSaleInfoDO.setAfterSaleTypeDetail(AfterSaleTypeDetailEnum.TIMEOUT_NO_PAY.getCode());
            afterSaleInfoDO.setRemark("超时支付自动取消");

        }


        if (OrderCancelTypeEnum.USE_CANCELED.getCode().equals(cancelType)){
            afterSaleInfoDO.setAfterSaleTypeDetail(AfterSaleTypeDetailEnum.USER_CANCEL.getCode());
            afterSaleInfoDO.setRemark("用户手动取消");
        }
        afterSaleInfoDO.setApplyReasonCode(AfterSaleReasonEnum.CANCEL.getCode());
        afterSaleInfoDO.setApplyReason(AfterSaleReasonEnum.CANCEL.getMsg());
        afterSaleInfoDO.setApplySource(AfterSaleApplySourceEnum.SYSTEM.getCode());
        afterSaleInfoDO.setReviewTime(LocalDateTime.now());
        afterSaleInfoDao.save(afterSaleInfoDO);

        log.info("新增订单 售后记录 订单号：{}，售后单号:{},订单售后状态：{}",afterSaleInfoDO.getOrderId(),
                afterSaleInfoDO.getAfterSaleId(),afterSaleInfoDO.getAfterSaleStatus());

        return afterSaleId;
    }

    /**
     * @description: 计算 取消订单 退款金额
     * @param cancelOrderAssembleRequest 取消订单 数据集合 请求
     * @return  取消订单退款信息
     * @author Long
     * @date: 2022/4/5 19:58
     */
    private CancelOrderRefundAmountDTO calculateCancelOrderRefundAmount(CancelOrderAssembleRequest cancelOrderAssembleRequest) {
        OrderInfoDTO orderInfoDTO = cancelOrderAssembleRequest.getOrderInfo();
        CancelOrderRefundAmountDTO cancelOrderRefundAmountDTO = new CancelOrderRefundAmountDTO();

        // 退款 整笔订单的实际支付金额
        cancelOrderRefundAmountDTO.setOrderId(orderInfoDTO.getOrderId());
        cancelOrderRefundAmountDTO.setTotalAmount(orderInfoDTO.getTotalAmount());
        cancelOrderRefundAmountDTO.setReturnGoodAmount(orderInfoDTO.getPayAmount());

        return cancelOrderRefundAmountDTO;

    }

    /**
     * @description: 执行退款信息更新售后信息
     * @param afterSaleInfoDO 售后单信息
     * @param fromStatus 上个状态
     * @param toStatus 变更状态
     * @return  void
     * @author Long
     * @date: 2022/4/5 18:04
     */
    private void updateAfterSaleStatus(AfterSaleInfoDO afterSaleInfoDO, Integer fromStatus, Integer toStatus) {
        // 更新 订单售后表
        afterSaleInfoDao.updateStatus(Long.valueOf(afterSaleInfoDO.getAfterSaleId()),fromStatus,toStatus);

        // 新增 售后 变更表
        AfterSaleLogDO afterSaleLogDO = afterSaleOperateLogFactory.get(afterSaleInfoDO,AfterSaleStatusChannelEnum.getBy(fromStatus,toStatus));
        afterSaleLogDAO.save(afterSaleLogDO);
        log.info("保存售后变更记录，售后单:{},fromStatus:{},toStatus:{}",afterSaleInfoDO.getAfterSaleId(),fromStatus,toStatus);

    }

    /**
     * @description:  构造 支付退款请求
     * @param actualRefundMessage 退款消息
     * @param afterSaleRefundDO 售后退款消息
     * @return  支付退款请求
     * @author Long
     * @date: 2022/4/5 17:58
     */
    private PayRefundRequest buildPayRefundRequest(ActualRefundMessage actualRefundMessage, AfterSaleRefundDO afterSaleRefundDO) {
        String  orderId = actualRefundMessage.getOrderId();
        PayRefundRequest payRefundRequest = new PayRefundRequest();
        payRefundRequest.setOrderId(orderId);
        payRefundRequest.setRefundAmount(afterSaleRefundDO.getRefundAmount());
        payRefundRequest.setAfterSaleId(afterSaleRefundDO.getAfterSaleId());

        return payRefundRequest;
    }


    /**
     * @description: 插入客服审核 退货 申请
     * @param returnGoodsAssembleRequest 退货商品集合 请求
     * @param   afterSaleStatus 退货状态
     * @return  void
     * @author Long
     * @date: 2022/4/4 13:45
     */
    private void insertReturnGoodsAfterSale(ReturnGoodsAssembleRequest returnGoodsAssembleRequest, Integer afterSaleStatus) {
        OrderInfoDTO orderInfoDTO = returnGoodsAssembleRequest.getOrderInfoDTO();
        OrderInfoDO orderInfoDO = orderInfoDTO.clone(OrderInfoDO.class);
        Integer afterSaleType = returnGoodsAssembleRequest.getAfterSaleType();

        //售后退货过程中 申请退款金额 和 实际退款金额  计算出来 可能不同

        AfterSaleInfoDO afterSaleInfoDO = new AfterSaleInfoDO();

        BigDecimal applyRefundAmount = returnGoodsAssembleRequest.getApplyRefundAmount();
        afterSaleInfoDO.setApplyRefundAmount(applyRefundAmount);

        BigDecimal returnGoodsAmount = returnGoodsAssembleRequest.getReturnGoodsAmount();
        afterSaleInfoDO.setRealRefundAmount(returnGoodsAmount);


        //1 新增售后订单表
        Integer cancelOrderAfterSaleStatus = AfterSaleStatusEnum.COMMITED.getCode();
        String afterSaleId = insertReturnGoodsAfterSaleInfoTable(orderInfoDO,afterSaleType,cancelOrderAfterSaleStatus,afterSaleInfoDO);

        returnGoodsAssembleRequest.setAfterSaleId(afterSaleId);

        // 2.新增售后条目表
        insertAfterSaleItemTable(orderInfoDO.getOrderId(),returnGoodsAssembleRequest.getRefundOrderItems(),afterSaleId);

        // 3. 新增售后变更表
        insertReturnGoodsAfterSaleLogTable(afterSaleId,AfterSaleStatusEnum.UN_CREATE.getCode(),afterSaleStatus);

        // 4 新增售后支付表
        AfterSaleRefundDO afterSaleRefundDO = insertAfterSaleRefundTable(orderInfoDO,afterSaleId,afterSaleInfoDO);

        returnGoodsAssembleRequest.setAfterSaleRefundId(afterSaleRefundDO.getId());

    }
    /**
     * @description: 新增售后支付表
     * @param orderInfoDO 订单信息
     * @param afterSaleId 售后Id
     * @param afterSaleInfoDO 售后信息
     * @return
     * @author Long
     * @date: 2022/4/4 16:01
     */
    private AfterSaleRefundDO insertAfterSaleRefundTable(OrderInfoDO orderInfoDO, String afterSaleId, AfterSaleInfoDO afterSaleInfoDO) {
        String orderId = orderInfoDO.getOrderId();
        OrderPaymentDetailDO orderPaymentDetailDO = orderPaymentDetailDao.getPaymentDetailByOrderId(orderId);

        AfterSaleRefundDO afterSaleRefundDO = new AfterSaleRefundDO();
        afterSaleRefundDO.setAfterSaleId(afterSaleId);
        afterSaleRefundDO.setOrderId(orderId);
        afterSaleRefundDO.setAccountType(AccountTypeEnum.THIRD.getCode());
        afterSaleRefundDO.setRefundStatus(RefundStatusEnum.UN_REFUND.getCode());
        afterSaleRefundDO.setRemark(RefundStatusEnum.UN_REFUND.getMsg());
        afterSaleRefundDO.setRefundAmount(afterSaleInfoDO.getRealRefundAmount());
        afterSaleRefundDO.setAfterSaleBatchNo(orderId + RandomUtil.genRandomNumber(10));

        if (orderPaymentDetailDO != null){
            afterSaleRefundDO.setOutTradeNo(orderPaymentDetailDO.getOutTradeNo());
            afterSaleRefundDO.setPayType(orderPaymentDetailDO.getPayType());
        }

        afterSaleRefundDAO.save(afterSaleRefundDO);

        log.info("新增售后支付信息,订单号:{},售后号：{}，状态：{}",orderId,afterSaleId,afterSaleInfoDO.getAfterSaleStatus());

        return afterSaleRefundDO;
    }

    /**
     * @description: 新增售后变更表
     * @param afterSaleId 售后Id
     * @param  preAfterSaleStatus 上一个售后状态
     * @param currentAfterSaleStatus 当前售后状态
     * @return  void
     * @author Long
     * @date: 2022/4/4 14:42
     */
    private void insertReturnGoodsAfterSaleLogTable(String afterSaleId, Integer preAfterSaleStatus, Integer currentAfterSaleStatus) {
        AfterSaleLogDO afterSaleLogDO = new AfterSaleLogDO();
        afterSaleLogDO.setAfterSaleId(Long.valueOf(afterSaleId));
        afterSaleLogDO.setPreStatus(preAfterSaleStatus);
        afterSaleLogDO.setCurrentStatus(currentAfterSaleStatus);

        // 售后 退货的业务值
        afterSaleLogDO.setRemark(ReturnGoodsTypeEnum.AFTER_SALE_RETURN_GOODS.getMsg());

        afterSaleLogDAO.save(afterSaleLogDO);

        log.info("新增售后单变更信息， 售后单：{}，状态：{}PreStatus{}，CurrentStatus：{}",afterSaleLogDO.getAfterSaleId(),preAfterSaleStatus,currentAfterSaleStatus);

    }

    /**
     * @description: 新增售后条目表
     * @param orderId 订单Id
     * @param refundOrderItems  退货订单条目
     * @param afterSaleId 售后Id
     * @return  void
     * @author Long
     * @date: 2022/4/4 14:34
     */ 
    private void insertAfterSaleItemTable(String orderId, List<OrderItemDTO> refundOrderItems, String afterSaleId) {

        List<AfterSaleItemDO> afterSaleItemDOS = new ArrayList<>();
        refundOrderItems.forEach(orderItemDTO -> {
            AfterSaleItemDO afterSaleItemDO = new AfterSaleItemDO();
            afterSaleItemDO.setAfterSaleId(Long.valueOf(afterSaleId));
            afterSaleItemDO.setOrderId(orderId);
            afterSaleItemDO.setSkuCode(orderItemDTO.getSkuCode());
            afterSaleItemDO.setProductName(orderItemDTO.getProductName());
            afterSaleItemDO.setProductImg(orderItemDTO.getProductImg());
            afterSaleItemDO.setReturnQuantity(orderItemDTO.getSaleQuantity());
            afterSaleItemDO.setOriginAmount(orderItemDTO.getOriginAmount());
            afterSaleItemDO.setApplyRefundAmount(orderItemDTO.getOriginAmount());
            afterSaleItemDO.setRealRefundAmount(orderItemDTO.getPayAmount());
            afterSaleItemDOS.add(afterSaleItemDO);
        });
        afterSaleItemDao.saveBatch(afterSaleItemDOS);
    }

    /**
     * @description:售后退货流程 插入订单销售表
     * @param orderInfoDO 订单信息
     * @param afterSaleType 售后类型
     * @param cancelOrderAfterSaleStatus 取消订单售后状态
     * @param afterSaleInfoDO 售后信息
     * @return  java.lang.String
     * @author Long
     * @date: 2022/4/4 13:57
     */
    private String insertReturnGoodsAfterSaleInfoTable(OrderInfoDO orderInfoDO, Integer afterSaleType, Integer cancelOrderAfterSaleStatus, AfterSaleInfoDO afterSaleInfoDO) {

        //1 生成售后订单号
        String afterSaleId = orderNoManager.genOrderId(OrderNoTypeEnum.AFTER_SALE.getCode(), orderInfoDO.getUserId());
        afterSaleInfoDO.setAfterSaleId(afterSaleId);
        afterSaleInfoDO.setBusinessIdentifier(orderInfoDO.getBusinessIdentifier());
        afterSaleInfoDO.setOrderId(orderInfoDO.getOrderId());
        afterSaleInfoDO.setOrderSourceChannel(BusinessIdentifierEnum.SELF_MALL.getCode());
        afterSaleInfoDO.setUserId(orderInfoDO.getUserId());
        afterSaleInfoDO.setOrderType(OrderTypeEnum.NORMAL.getCode());
        afterSaleInfoDO.setApplyTime(LocalDateTime.now());
        afterSaleInfoDO.setAfterSaleStatus(cancelOrderAfterSaleStatus);

        // 用户售后退货的业务值
        afterSaleInfoDO.setApplySource(AfterSaleApplySourceEnum.USER_RETURN_GOODS.getCode());
        afterSaleInfoDO.setRemark(ReturnGoodsTypeEnum.AFTER_SALE_RETURN_GOODS.getMsg());
        afterSaleInfoDO.setApplyReasonCode(AfterSaleReasonEnum.USER.getCode());
        afterSaleInfoDO.setApplyReason(AfterSaleReasonEnum.USER.getMsg());
        afterSaleInfoDO.setAfterSaleTypeDetail(AfterSaleTypeDetailEnum.PART_REFUND.getCode());

        // 退货流程 只退订单 一笔条目

        if (AfterSaleTypeEnum.RETURN_GOODS.getCode().equals(afterSaleType)){
            afterSaleInfoDO.setAfterSaleType(AfterSaleTypeEnum.RETURN_GOODS.getCode());
        }

        // 退货 流程 退订单的全部条目 后续 按照 整笔退款逻辑 处理

        if (AfterSaleTypeEnum.RETURN_MONEY.getCode().equals(afterSaleType)){
            afterSaleInfoDO.setAfterSaleType(AfterSaleTypeEnum.RETURN_MONEY.getCode());
        }

        afterSaleInfoDao.save(afterSaleInfoDO);

        log.info("新增订单销售记录, 订单号:{},售后单号：{}，订单售后状态：{}",orderInfoDO.getOrderId(),afterSaleId,orderInfoDO.getOrderStatus());

        return afterSaleId;
    }

    /**
     * @description: 计算退货商品 净额
     * @param returnGoodsAssembleRequest  退货商品集合 参数
     * @return  退货商品集合 参数
     * @author Long
     * @date: 2022/4/3 20:41
     */
    private ReturnGoodsAssembleRequest calculateReturnGoodsAmount(ReturnGoodsAssembleRequest returnGoodsAssembleRequest) {
        String skuCode = returnGoodsAssembleRequest.getSkuCode();
        String orderId = returnGoodsAssembleRequest.getOrderId();

        List<OrderItemDTO> refundOrderItemDTOS = Lists.newArrayList();
        List<OrderItemDTO> orderItemDTOS = returnGoodsAssembleRequest.getOrderItems();
        List<AfterSaleOrderItemDTO> afterSaleOrderItemDTOS = returnGoodsAssembleRequest.getAfterSaleOrderItems();


        // 订单条目数量
        int orderItemNum = orderItemDTOS.size();

        //售后条目数量
        int afterSaleItemNum = afterSaleOrderItemDTOS.size();

        //订单条目数，一条 整笔订单退，售后类型：全部退款

        if (orderItemNum == returnGoodsItemNum ){
            OrderItemDTO orderItemDTO = orderItemDTOS.get(0);
            returnGoodsAssembleRequest.setAfterSaleType(AfterSaleTypeEnum.RETURN_MONEY.getCode());

            return calculateWholeOrderFefundAmount(orderId,orderItemDTO.getPayAmount(),orderItemDTO.getOriginAmount(),returnGoodsAssembleRequest);
        }

        // 多数量商品退款
        returnGoodsAssembleRequest.setAfterSaleType(AfterSaleTypeEnum.RETURN_GOODS.getCode());

        OrderItemDO orderItemDO = orderItemDao.getOrderItemBySkuAndOrderId(orderId,skuCode);

        // 该笔订单条目数 = 已存的售后订单条目数 + 本次退货的条目 （每次退1条）
        if(orderItemNum == afterSaleItemNum + 1){
            // 当前条目是订单的最后一笔
            returnGoodsAssembleRequest = calculateWholeOrderFefundAmount(orderId,
                    orderItemDO.getPayAmount(),
                    orderItemDO.getOriginAmount(),
                    returnGoodsAssembleRequest);

        }else {
                // 只退当前
            returnGoodsAssembleRequest.setReturnGoodsAmount(orderItemDO.getPayAmount());
            returnGoodsAssembleRequest.setApplyRefundAmount(orderItemDO.getOriginAmount());
            returnGoodsAssembleRequest.setLastReturnGoods(true);
        }
        refundOrderItemDTOS.add(orderItemDO.clone(OrderItemDTO.class));
        returnGoodsAssembleRequest.setOrderItems(refundOrderItemDTOS);

        return returnGoodsAssembleRequest;

    }

    /**
     * @description:  计算 整笔订单退款金额
     * @param orderId 订单 Id
     * @param payAmount 支付金额
     * @param originAmount 实际支付金额
     * @param returnGoodsAssembleRequest 退货商品集合 请求
     * @return 退货商品集合 请求
     * @author Long
     * @date: 2022/4/3 20:50
     */
    private ReturnGoodsAssembleRequest calculateWholeOrderFefundAmount(String orderId, BigDecimal payAmount, BigDecimal originAmount, ReturnGoodsAssembleRequest returnGoodsAssembleRequest) {

        OrderAmountDO deliverAmount = orderAmountDao.getByIdAndAmountType(orderId, AmountTypeEnum.SHIPPING_AMOUNT.getCode());

        BigDecimal freightAmount =(deliverAmount == null || deliverAmount.getAmount() == null) ? BigDecimal.ZERO : deliverAmount.getAmount();

        /**
         * 最终退款
         */
        BigDecimal returnGoodAmount = payAmount.add(freightAmount);
        returnGoodsAssembleRequest.setReturnGoodsAmount(returnGoodAmount);
        returnGoodsAssembleRequest.setApplyRefundAmount(originAmount);
        returnGoodsAssembleRequest.setAfterSaleType(AfterSaleTypeEnum.RETURN_MONEY.getCode());
        returnGoodsAssembleRequest.setLastReturnGoods(true);
        return returnGoodsAssembleRequest;
    }


    /**
     * @description: 构造退货商品数据请求
     * @param request 退货商品订单请求参数
     * @return  退货商品集合 请求
     * @author Long
     * @date: 2022/4/3 20:29
     */
    private ReturnGoodsAssembleRequest buildReturnGoodsData(ReturnGoodsOrderRequest request) {
        ReturnGoodsAssembleRequest returnGoodsAssembleRequest = request.clone(ReturnGoodsAssembleRequest.class);
        // 订单Id
        String orderId = request.getOrderId();

        //封装数据
        OrderInfoDO orderInfoDO= orderInfoDao.getByOrderId(orderId);
        OrderInfoDTO orderInfoDTO = orderInfoDO.clone(OrderInfoDTO.class);
        returnGoodsAssembleRequest.setOrderInfoDTO(orderInfoDTO);

        // 封装 订单条目
        List<OrderItemDO> orderItemDOS = orderItemDao.listByOrderId(orderId);
        List<OrderItemDTO> orderItemDTOS =ObjectUtil.convertList(orderItemDOS,OrderItemDTO.class);
        returnGoodsAssembleRequest.setOrderItems(orderItemDTOS);

        //封装订单 售后条目

        List<AfterSaleItemDO> afterSaleItemDOS = afterSaleItemDao.listByOrderId(orderId);

        List<AfterSaleItemDTO> afterSaleItemDTOS = ObjectUtil.convertList(afterSaleItemDOS,AfterSaleItemDTO.class);

        return returnGoodsAssembleRequest;
    }

    /**
     * @description: 退货申请请求参数效验
     * @param request 退货申请请求
     * @return  void
     * @author Long
     * @date: 2022/4/3 15:33
     */
    private void checkAfterSaleRequestPram(ReturnGoodsOrderRequest request) {
        ParamCheckUtil.checkObjectNonNull(request);

        String orderId = request.getOrderId();
        ParamCheckUtil.checkStringNonEmpty(orderId,OrderErrorCodeEnum.ORDER_ID_IS_NULL);

        String userId = request.getUserId();
        ParamCheckUtil.checkStringNonEmpty(userId,OrderErrorCodeEnum.USER_ID_IS_NULL);

        Integer businessIdentifier = request.getBusinessIdentifier();

        ParamCheckUtil.checkObjectNonNull(businessIdentifier,OrderErrorCodeEnum.BUSINESS_IDENTIFIER_IS_NULL);

        Integer returnGoodsCode = request.getReturnGoodsCode();
        ParamCheckUtil.checkObjectNonNull(returnGoodsCode,OrderErrorCodeEnum.RETURN_GOODS_CODE_IS_NULL);

        String skuCode = request.getSkuCode();
        ParamCheckUtil.checkStringNonEmpty(skuCode,OrderErrorCodeEnum.SKU_CODE_IS_NULL);


    }


    /** 
     * @description: z执行取消订单
     * @param cancelOrderRequest 取消订单请求
     * @param orderId 订单Id
     * @return  void
     * @author Long
     * @date: 2022/3/9 10:44
     */ 
    private void executeCancelOrder(CancelOrderRequest cancelOrderRequest, String orderId) {
        //1.组装数据
        CancelOrderAssembleRequest request = buildAssembleRequest(orderId,cancelOrderRequest);

        // 2 幂等效验 防止多线程同时操作取消同一订单
        if (OrderStatusEnum.CREATED.getCode().equals(request.getOrderInfo().getOrderStatus())){
            return;
        }
        // 3.更新订单状态 和记录 订单操作
        updateOrderStatusAndSaveOperationLog(request);

        //4.超时未支付的订单不用继续在执行取消
        if (OrderStatusEnum.PAID.getCode() > request.getOrderInfo().getOrderStatus()){
          return;
        }

        // 4. 履约取消
        cancelFulfill(request);

        // 5. 释放资产
        defaultProducer.sendMessage(RocketMQConstant.RELEASE_ASSETS_TOPIC, JSONObject.toJSONString(cancelOrderRequest),"释放资产");
    }

    /** 
     * @description: 履约取消
     * @param request 取消组装数据请求
     * @return  void
     * @author Long
     * @date: 2022/3/9 14:15
     */ 
    private void cancelFulfill(CancelOrderAssembleRequest request) {
        OrderInfoDTO orderInfoDTO = request.getOrderInfo();
        CancelFulfillRequest cancelFulfillRequest = orderInfoDTO.clone(CancelFulfillRequest.class);

        ResponseData<Boolean> response = fulFillApi.cancelFulfill(cancelFulfillRequest);

        if (!response.getSuccess()){
            throw new OrderBizException(OrderErrorCodeEnum.CANCEL_ORDER_FULFILL_ERROR);
        }
    }

    /**
     * @description: 修改订单状态并记录订单操作
     * @param request 取消订单请求
     * @return  void
     * @author Long
     * @date: 2022/3/9 11:42
     */
    private void updateOrderStatusAndSaveOperationLog(CancelOrderAssembleRequest request) {
        // 更新表
        OrderInfoDO orderInfoDO =request.getOrderInfo().clone(OrderInfoDO.class);
        orderInfoDO.setCancelType(request.getCancelType());
        orderInfoDO.setOrderStatus(OrderStatusEnum.CANCELED.getCode());
        orderInfoDO.setCancelTime(LocalDateTime.now());
        orderInfoDao.updateById(orderInfoDO);
        log.info("更新订单信息OrderInfo状态：orderId:{},status:{}",orderInfoDO.getOrderId(),orderInfoDO.getOrderStatus() );

        // 新增订单操作日志表
        Integer cancelTType = Integer.valueOf(orderInfoDO.getCancelType());
        String orderId = orderInfoDO.getOrderId();
        OrderOperateLogDO orderOperateLog = OrderOperateLogDO
                .builder()
                .orderId(orderId)
                .preStatus(request.getOrderInfo().getOrderStatus())
                .currentStatus(OrderStatusEnum.CREATED.getCode())
                .operateType(OrderOperateTypeEnum.AUTO_CANCEL_ORDER.getCode()).build();

        if (OrderCancelTypeEnum.USE_CANCELED.getCode().equals(cancelTType)){
            orderOperateLog.setOperateType(OrderOperateTypeEnum.MANUAL_CANCEL_ORDER.getCode());
            orderOperateLog.setRemark(OrderOperateTypeEnum.MANUAL_CANCEL_ORDER.getSmg() + orderOperateLog.getPreStatus() + "-" + orderOperateLog.getCurrentStatus());

        }

        if (OrderCancelTypeEnum.TIMEOUT_CANCELED.getCode().equals(cancelTType)){
            orderOperateLog.setOperateType(OrderOperateTypeEnum.AUTO_CANCEL_ORDER.getCode());
            orderOperateLog.setRemark(OrderOperateTypeEnum.AUTO_CANCEL_ORDER.getSmg() + orderOperateLog.getPreStatus() + "-" + orderOperateLog.getCurrentStatus());

        }
        orderOperateLogDao.save(orderOperateLog);

        log.info("新增订单操作日志 OrderOperateLog状态，orderId:{},PreStatus:{},CurrentStatus:{}",orderId,orderOperateLog.getPreStatus(),orderOperateLog.getCurrentStatus());
    }

    /** 
     * @description: 构建 取消订单 数据
     * @param orderId 订单Id
     * @param cancelOrderRequest 取消订单请求
     * @return
     * @author Long
     * @date: 2022/3/9 10:58
     */ 
    private CancelOrderAssembleRequest buildAssembleRequest(String orderId, CancelOrderRequest cancelOrderRequest) {

        Integer cancelType = cancelOrderRequest.getCancelType();

        OrderInfoDTO orderInfo =  findOrderInfo(orderId,cancelType);
        List<OrderItemDTO> orderItems = findOrderItems(orderId);
        CancelOrderAssembleRequest request = cancelOrderRequest.clone(CancelOrderAssembleRequest.class);
        request.setOrderId(orderId);
        request.setCancelType(cancelType);
        request.setOrderInfo(orderInfo);
        request.setOrderItems(orderItems);
        return request;

    }

    /**
     * @description: 查询 订单 条目
     * @param orderId 订单Id
     * @return  订单条目DTO
     * @author Long
     * @date: 2022/3/9 11:22
     */
    private List<OrderItemDTO> findOrderItems(String orderId) {
    List<OrderItemDO> orderItems = orderItemDao.listByOrderId(orderId);

    if (CollectionUtils.isEmpty(orderItems)){
        throw new OrderBizException(OrderErrorCodeEnum.ORDER_ITEM_IS_NULL);
    }

    List<OrderItemDTO> orderItemDTOs = ObjectUtil.convertList(orderItems,OrderItemDTO.class);

        return orderItemDTOs;
    }

    /**
     * @description: 查询 订单 信息
     * @param orderId 订单Id
     * @param cancelType 取消类型
     * @return  订单信息
     * @author Long
     * @date: 2022/3/9 11:09
     */
    private OrderInfoDTO findOrderInfo(String orderId, Integer cancelType) {

        OrderInfoDO orderInfo = orderInfoDao.getByOrderId(orderId);

        if (ObjectUtils.isEmpty(orderId)){
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_INFO_IS_NULL);
        }

        OrderInfoDTO orderInfoDTO = orderInfo.clone(OrderInfoDTO.class);
        orderInfoDTO.setCancelType(String.valueOf(cancelType));

        return orderInfoDTO;
    }

    /**
     * @description: 入参检查
     * @param cancelOrderRequest 取消订单请求
     * @return  void
     * @author Long
     * @date: 2022/3/8 17:17
     */
    private void checkCancelOrderRequestPram(CancelOrderRequest cancelOrderRequest) {
        ParamCheckUtil.checkObjectNonNull(cancelOrderRequest);

        // 订单状态
        Integer orderStatus = cancelOrderRequest.getOrderStatus();
        ParamCheckUtil.checkObjectNonNull(orderStatus, OrderErrorCodeEnum.ORDER_STATUS_IS_NULL);

        if (orderStatus >= OrderStatusEnum.OUT_STOCK.getCode()){
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_STATUS_CHANGED);
        }

        if (orderStatus.equals(OrderStatusEnum.CANCELED.getCode())){
                throw new OrderBizException(OrderErrorCodeEnum.ORDER_STATUS_CANCELED);
        }

        // 订单 ID
        String orderId = cancelOrderRequest.getOrderId();
        ParamCheckUtil.checkStringNonEmpty(orderId,OrderErrorCodeEnum.ORDER_ID_IS_NULL);

        // 业务线标识
        Integer businessIdentifier = cancelOrderRequest.getBusinessIdentifier();
        ParamCheckUtil.checkObjectNonNull(businessIdentifier,OrderErrorCodeEnum.BUSINESS_IDENTIFIER_IS_NULL);

        // 订单取消类型
        Integer cancelType = cancelOrderRequest.getCancelType();
        ParamCheckUtil.checkObjectNonNull(cancelType,OrderErrorCodeEnum.ORDER_TYPE_IS_NULL);

        // 用户Id
        String userId = cancelOrderRequest.getUserId();
        ParamCheckUtil.checkStringNonEmpty(userId,OrderErrorCodeEnum.USER_ID_IS_NULL);

        // 订单类型
        Integer orderType = cancelOrderRequest.getOrderType();
        ParamCheckUtil.checkObjectNonNull(orderType,OrderErrorCodeEnum.ORDER_TYPE_IS_NULL);


    }
}
