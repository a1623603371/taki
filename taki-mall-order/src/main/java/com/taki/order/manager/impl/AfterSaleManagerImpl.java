package com.taki.order.manager.impl;

import com.taki.common.enums.AfterSaleTypeDetailEnum;
import com.taki.common.enums.AfterSaleTypeEnum;
import com.taki.common.enums.OrderOperateTypeEnum;
import com.taki.common.enums.OrderStatusEnum;
import com.taki.common.utli.RandomUtil;
import com.taki.common.utli.ResponseData;
import com.taki.fulfill.api.FulFillApi;
import com.taki.fulfill.domain.request.CancelFulfillRequest;
import com.taki.order.converter.OrderConverter;
import com.taki.order.dao.*;
import com.taki.order.domain.dto.OrderInfoDTO;
import com.taki.order.domain.dto.OrderItemDTO;
import com.taki.order.domain.entity.*;
import com.taki.order.domain.request.CancelOrderAssembleRequest;
import com.taki.order.enums.*;
import com.taki.order.exception.OrderBizException;
import com.taki.order.exception.OrderErrorCodeEnum;
import com.taki.order.manager.AfterSaleManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName AfterSaleManagerImpl
 * @Description TODO
 * @Author Long
 * @Date 2022/5/17 23:05
 * @Version 1.0
 */
@Service
@Slf4j
public class AfterSaleManagerImpl implements AfterSaleManager {

    @DubboReference(version = "1.0.0")
    private FulFillApi fulFillApi;

    @Autowired
    private OrderInfoDao orderInfoDao;

    @Autowired
    private OrderOperateLogDao orderOperateLogDao;

    @Autowired
    private AfterSaleInfoDao afterSaleInfoDao;

    @Autowired
    private AfterSaleItemDao afterSaleItemDao;

    @Autowired
    private AfterSaleLogDAO afterSaleLogDAO;

    @Autowired
    private OrderPaymentDetailDao orderPaymentDetailDao;

    @Autowired
    private AfterSaleRefundDAO afterSaleRefundDAO;


    @Autowired
    private OrderConverter orderConverter;
    @Override
    public void cancelOrderFulfillmentAndUpdateOrderStatus(CancelOrderAssembleRequest cancelOrderAssembleRequest) {
        //履约取消
        cancelFulfill(cancelOrderAssembleRequest);

        // 更新订单状态，记录订单操作日志
        updateOrderStatusAndSaveOperationLog(cancelOrderAssembleRequest);
    }

    /**
     * @description: 修改订单状态 并 记录订单日志
     * @param cancelOrderAssembleRequest 取消订单 组装 请求 参数
     * @return  void
     * @author Long
     * @date: 2022/5/18 13:31
     */
    private void updateOrderStatusAndSaveOperationLog(CancelOrderAssembleRequest cancelOrderAssembleRequest) {

        //更新订单表
        OrderInfoDO orderInfoDO =  orderConverter.orderInfoDTO2DO(cancelOrderAssembleRequest.getOrderInfo());
        orderInfoDO.setCancelType(cancelOrderAssembleRequest.getCancelType());
        orderInfoDO.setOrderStatus(OrderStatusEnum.CANCELED.getCode());
        orderInfoDO.setCancelTime(LocalDateTime.now());
        orderInfoDao.updateOrderInfo(orderInfoDO);
        log.info("更新订单信息OrderInfo状态：orderId={},status={}",orderInfoDO.getOrderId(),orderInfoDO.getOrderStatus());

        //新增订单操作日志表

        Integer cancelType = Integer.valueOf(orderInfoDO.getCancelType());
        String orderId = orderInfoDO.getOrderId();
        OrderOperateLogDO orderOperateLogDO = new OrderOperateLogDO();
        orderOperateLogDO.setOrderId(orderId);
        orderOperateLogDO.setPreStatus(cancelOrderAssembleRequest.getOrderInfo().getOrderStatus());
        orderOperateLogDO.setCurrentStatus(OrderStatusEnum.CREATED.getCode());
        orderOperateLogDO.setOperateType(OrderOperateTypeEnum.AUTO_CANCEL_ORDER.getCode());

        if (OrderCancelTypeEnum.USE_CANCELED.getCode().equals(cancelType)){
            orderOperateLogDO.setOperateType(OrderOperateTypeEnum.MANUAL_CANCEL_ORDER.getCode());
            orderOperateLogDO.setRemark(OrderOperateTypeEnum.MANUAL_CANCEL_ORDER.getSmg() +
                    orderOperateLogDO.getPreStatus()+"-" + orderOperateLogDO.getCurrentStatus());
        }

        if (OrderCancelTypeEnum.TIMEOUT_CANCELED.getCode().equals(cancelType)){
            orderOperateLogDO.setOperateType(OrderOperateTypeEnum.AUTO_CANCEL_ORDER.getCode());
            orderOperateLogDO.setRemark(OrderOperateTypeEnum.AUTO_CANCEL_ORDER.getSmg() +
                    orderOperateLogDO.getPreStatus() + orderOperateLogDO.getCurrentStatus());

        }
        orderOperateLogDao.save(orderOperateLogDO);

        log.info("新增订单操作日志OrderOperateLog状态，orderId ={},PreStatus={},CurrentStatus={}",orderInfoDO.getOrderId(),orderOperateLogDO.getPreStatus(),orderOperateLogDO.getCurrentStatus());
    }

    /**
     * @description:  调用 履约取消 拦截订单
     * @param cancelOrderAssembleRequest 取消订单组装请求
     * @return  void
     * @author Long
     * @date: 2022/5/17 23:12
     */ 
    private void cancelFulfill(CancelOrderAssembleRequest cancelOrderAssembleRequest) {
        OrderInfoDTO orderInfo = cancelOrderAssembleRequest.getOrderInfo();

        if (orderInfo.getOrderStatus().equals(OrderStatusEnum.CANCELED.getCode())){
            return;
        }

        CancelFulfillRequest cancelFulfillRequest = orderConverter.convertCancelFulfillRequest(orderInfo);

        ResponseData result = fulFillApi.cancelFulfill(cancelFulfillRequest);

        if (!result.getSuccess()){
            throw new OrderBizException(OrderErrorCodeEnum.CANCEL_ORDER_FULFILL_ERROR);
        }


    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertCancelOrderAfterSale(CancelOrderAssembleRequest cancelOrderAssembleRequest, Integer afterSaleStatus) {

        String orderId = cancelOrderAssembleRequest.getOrderId();

        OrderInfoDO orderInfoDO = orderInfoDao.getByOrderId(orderId);
        String afterSaleId =cancelOrderAssembleRequest.getAfterSaleId();
        OrderInfoDTO orderInfo = cancelOrderAssembleRequest.getOrderInfo();
            //取消订单过程中 申请退款金额 和 实际退款金额 都是 实付退款金额 金额相同

        AfterSaleInfoDO afterSaleInfoDO = new AfterSaleInfoDO();
        afterSaleInfoDO.setApplyRefundAmount(orderInfoDO.getPayAmount());
        afterSaleInfoDO.setRealRefundAmount(orderInfoDO.getPayAmount());

            // 1/新增售后订单表
        Integer cancelOrderAfterSaleStatus = AfterSaleStatusEnum.REVIEW_PASS.getCode();
        insertCancelOrderAfterSaleInfoTable(orderInfoDO,cancelOrderAfterSaleStatus,afterSaleInfoDO,afterSaleId);
        cancelOrderAssembleRequest.setAfterSaleId(afterSaleId);

        // 2 新增售后条目表
        List<OrderItemDTO> orderItemDTOS = cancelOrderAssembleRequest.getOrderItems();
        insertAfterSaleItemTable(orderId,orderItemDTOS,afterSaleId);
        //3. 新增售后变跟表
        insertCancelOrderAfterSaleLogTable(afterSaleId,orderInfo,AfterSaleStatusEnum.UN_CREATE.getCode(),afterSaleStatus);
        //4.新增售后支付表
        AfterSaleRefundDO afterSaleRefundDO = insertAfterSaleRefundTable(orderInfo,afterSaleId,afterSaleInfoDO);
        cancelOrderAssembleRequest.setAfterSaleRefundId(afterSaleRefundDO.getId());

    }


    /**
     * @description: 保存 售后退款 表
     * @param orderInfo 订单信息
     * @param afterSaleId 售后Id
     * @param afterSaleInfoDO 售后单信息
     * @return  com.taki.order.domain.entity.AfterSaleRefundDO
     * @author Long
     * @date: 2022/5/18 14:36
     */
    private AfterSaleRefundDO insertAfterSaleRefundTable(OrderInfoDTO orderInfo, String afterSaleId, AfterSaleInfoDO afterSaleInfoDO) {
        String orderId = orderInfo.getOrderId();

        OrderPaymentDetailDO orderPaymentDetail = orderPaymentDetailDao.getPaymentDetailByOrderId(orderId);

        AfterSaleRefundDO afterSaleRefundDO = new AfterSaleRefundDO();

        afterSaleRefundDO.setAfterSaleId(afterSaleId);
        afterSaleRefundDO.setAfterSaleBatchNo(orderId + RandomUtil.genRandomNumber(10));
        afterSaleRefundDO.setOrderId(orderId);
        afterSaleRefundDO.setAccountType(AccountTypeEnum.THIRD.getCode());
        afterSaleRefundDO.setRefundAmount(afterSaleInfoDO.getRealRefundAmount());
        afterSaleRefundDO.setRefundStatus(RefundStatusEnum.UN_REFUND.getCode());
        afterSaleInfoDO.setRemark(RefundStatusEnum.UN_REFUND.getMsg());

        if (ObjectUtils.isNotEmpty(orderPaymentDetail)){
        afterSaleRefundDO.setOutTradeNo(orderPaymentDetail.getOutTradeNo());
        afterSaleRefundDO.setPayType(orderPaymentDetail.getPayType());
        }

        afterSaleRefundDAO.save(afterSaleRefundDO);

        log.info("新增支付信息，订单号:{},售后订单号：{},状态：{}",orderId,afterSaleId,afterSaleRefundDO.getRefundStatus());

        return afterSaleRefundDO;
    }

    /**
     * @description:  保存 售后变更表
     * @param afterSaleId 售后Id
     * @param orderInfo 订单信息
     * @param preAfterSaleStatus 售后单上一个状态
     * @param currentAfterSaleStatus  售后单当前状态
     * @return  void
     * @author Long
     * @date: 2022/5/18 14:26
     */
    private void insertCancelOrderAfterSaleLogTable(String afterSaleId, OrderInfoDTO orderInfo, Integer preAfterSaleStatus, Integer currentAfterSaleStatus) {

        AfterSaleLogDO afterSaleLogDO = new AfterSaleLogDO();
        afterSaleLogDO.setAfterSaleId(Long.valueOf(afterSaleId));
        afterSaleLogDO.setPreStatus(preAfterSaleStatus);
        afterSaleLogDO.setCurrentStatus(currentAfterSaleStatus);

        // 取消订单业务 值
        Integer cancelType = Integer.valueOf(orderInfo.getCancelType());
        if (OrderCancelTypeEnum.TIMEOUT_CANCELED.getCode().equals(cancelType)){
            afterSaleLogDO.setRemark(OrderCancelTypeEnum.TIMEOUT_CANCELED.getMsg());
        }

        if (OrderCancelTypeEnum.USE_CANCELED.getCode().equals(cancelType)){
            afterSaleLogDO.setRemark(OrderCancelTypeEnum.USE_CANCELED.getMsg());
        }

        afterSaleLogDAO.save(afterSaleLogDO);

        log.info("新增售后单变更信息,订单号:{},售后订单号:{},状态:preStatus={},currentStatus={}",orderInfo.getOrderId(),afterSaleId,preAfterSaleStatus,currentAfterSaleStatus);


    }

    /**
     * @description:  保存售后条目 表
     * @param orderId
     * @param  orderItemDTOS
     * @param  afterSaleId
     * @return  void
     * @author Long
     * @date: 2022/5/18 14:26
     */
    private void insertAfterSaleItemTable(String orderId, List<OrderItemDTO> orderItemDTOS, String afterSaleId) {

        List<AfterSaleItemDO> afterSaleItems = new ArrayList<>();
        orderItemDTOS.forEach(orderItemDTO -> {
            AfterSaleItemDO afterSaleItemDO = new AfterSaleItemDO();
            afterSaleItemDO.setAfterSaleId(afterSaleId);
            afterSaleItemDO.setOrderId(orderId);
            afterSaleItemDO.setOriginAmount(orderItemDTO.getOriginAmount());
            afterSaleItemDO.setProductImg(orderItemDTO.getProductImg());
            afterSaleItemDO.setReturnQuantity(orderItemDTO.getSaleQuantity());
            afterSaleItemDO.setProductName(orderItemDTO.getProductName());
            afterSaleItemDO.setSkuCode(orderItemDTO.getSkuCode());
            afterSaleItemDO.setRealRefundAmount(orderItemDTO.getPayAmount());
            afterSaleItemDO.setApplyRefundAmount(orderItemDTO.getOriginAmount());
            afterSaleItems.add(afterSaleItemDO);
        });
        afterSaleItemDao.saveBatch(afterSaleItems);
    }

    /**
     * @description: 插入 取消订单 售后表
     * @param orderInfoDO 订单 信息
     * @param cancelOrderAfterSaleStatus 取消订单售后状态
     * @param  afterSaleInfoDO 售后单信息
     * @param afterSaleId 售后id
     * @return  void
     * @author Long
     * @date: 2022/5/18 14:01
     */
    private void insertCancelOrderAfterSaleInfoTable(OrderInfoDO orderInfoDO, Integer cancelOrderAfterSaleStatus, AfterSaleInfoDO afterSaleInfoDO, String afterSaleId) {

        afterSaleInfoDO.setAfterSaleId(afterSaleId);
        afterSaleInfoDO.setBusinessIdentifier(BusinessIdentifierEnum.SELF_MALL.getCode());
        afterSaleInfoDO.setOrderId(orderInfoDO.getOrderId());
        afterSaleInfoDO.setOrderSourceChannel(BusinessIdentifierEnum.SELF_MALL.getCode());
        afterSaleInfoDO.setUserId(orderInfoDO.getUserId());
        afterSaleInfoDO.setOrderType(OrderTypeEnum.NORMAL.getCode());
        afterSaleInfoDO.setApplyTime(LocalDateTime.now());
        afterSaleInfoDO.setAfterSaleStatus(cancelOrderAfterSaleStatus);

        afterSaleInfoDO.setRealRefundAmount(afterSaleInfoDO.getRealRefundAmount());
        afterSaleInfoDO.setApplyRefundAmount(afterSaleInfoDO.getApplyRefundAmount());

        // 取消订单 整笔退款
        afterSaleInfoDO.setAfterSaleType(AfterSaleTypeEnum.RETURN_MONEY.getCode());

        Integer cancelType = Integer.valueOf(orderInfoDO.getCancelType());

        if (OrderCancelTypeEnum.TIMEOUT_CANCELED.getCode().equals(cancelType)){
            afterSaleInfoDO.setAfterSaleTypeDetail(AfterSaleTypeDetailEnum.TIMEOUT_NO_PAY.getCode());
            afterSaleInfoDO.setRemark("超时支付自动取消");
        }

        if (OrderCancelTypeEnum.USE_CANCELED.getCode().equals(cancelType)){
            afterSaleInfoDO.setAfterSaleTypeDetail(AfterSaleTypeDetailEnum.USER_CANCEL.getCode());
            afterSaleInfoDO.setRemark("用户手动取消");
        }
        afterSaleInfoDO.setApplyReason(AfterSaleReasonEnum.CANCEL.getMsg());
        afterSaleInfoDO.setApplyReasonCode(AfterSaleReasonEnum.CANCEL.getCode());
        afterSaleInfoDO.setApplySource(AfterSaleApplySourceEnum.SYSTEM.getCode());
        afterSaleInfoDO.setReviewTime(LocalDateTime.now());

        afterSaleInfoDao.save(afterSaleInfoDO);

        log.info("新增订单售后记录，订单号:{},售后单号:{},订单售后状态：{}",afterSaleInfoDO.getOrderId(),afterSaleId,afterSaleInfoDO.getAfterSaleStatus());




    }
}
