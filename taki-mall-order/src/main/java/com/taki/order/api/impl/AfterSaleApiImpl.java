package com.taki.order.api.impl;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.constants.RedisLockKeyConstants;
import com.taki.common.constants.RocketMQConstant;
import com.taki.common.enums.CustomerAuditResult;
import com.taki.common.enums.CustomerAuditSourceEnum;
import com.taki.common.message.ActualRefundMessage;
import com.taki.common.mq.MQMessage;
import com.taki.common.redis.RedisLock;
import com.taki.common.utli.ParamCheckUtil;
import com.taki.common.utli.ResponseData;
import com.taki.customer.domain.request.CustomReviewReturnGoodsRequest;
import com.taki.customer.domain.request.CustomerReceiveAfterSaleRequest;
import com.taki.order.api.AfterSaleApi;
import com.taki.order.dao.AfterSaleItemDao;
import com.taki.order.dao.AfterSaleRefundDAO;
import com.taki.order.dao.OrderItemDao;
import com.taki.order.domain.dto.CheckLackDTO;
import com.taki.order.domain.dto.LackDTO;
import com.taki.order.domain.dto.ReleaseProductStockDTO;
import com.taki.order.domain.entity.AfterSaleItemDO;
import com.taki.order.domain.entity.AfterSaleRefundDO;
import com.taki.order.domain.entity.OrderItemDO;
import com.taki.order.domain.request.*;
import com.taki.order.enums.AfterSaleStatusEnum;
import com.taki.order.exception.OrderBizException;
import com.taki.order.exception.OrderErrorCodeEnum;
import com.taki.order.mq.producer.CustomerAuditPassSendReleaseAssetsProducer;
import com.taki.order.service.OrderAfterSaleService;
import com.taki.order.service.OrderLackService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName AfterSaleApiImpl
 * @Description 售后Api
 * @Author Long
 * @Date 2022/3/9 15:07
 * @Version 1.0
 */
@Slf4j
@DubboService(version = "1.0.0",interfaceClass = AfterSaleApi.class,retries = 0)
public class AfterSaleApiImpl implements AfterSaleApi {


    @Autowired
    private OrderAfterSaleService orderAfterSaleService;


    @Autowired
    private OrderLackService orderLackService;


    @Autowired
    private AfterSaleItemDao afterSaleItemDao;

    @Autowired
    private AfterSaleRefundDAO afterSaleRefundDAO;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private CustomerAuditPassSendReleaseAssetsProducer customerAuditPassSendReleaseAssetsProducer;

    @Override
    public ResponseData<Boolean> cancelOrder(CancelOrderRequest cancelOrderRequest) {

        try {
            Boolean success = orderAfterSaleService.cancelOrder(cancelOrderRequest);
            return ResponseData.success(success);
        }catch (OrderBizException e){
            log.error("biz error",e);
            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());
        }catch (Exception e){
            log.error("system error",e);
            return ResponseData.error(e.getMessage());
        }

    }

    @Override
    public ResponseData<LackDTO> lockItem(LackRequest lackRequest) {
        log.info("request={}", JSONObject.toJSONString(lackRequest));
        try {

            // 1.参数基本效验
            ParamCheckUtil.checkObjectNonNull(lackRequest.getOrderId(), OrderErrorCodeEnum.ORDER_ID_IS_NULL);
            ParamCheckUtil.checkObjectNonNull(lackRequest.getLackItems(),OrderErrorCodeEnum.LACK_ITEM_IS_NULL);


            //2 加锁防止并发
            String lockKey = RedisLockKeyConstants.LACK_REQUEST_KEY + lackRequest.getOrderId();

            if (!redisLock.tryLock(lockKey)){
                throw new OrderBizException(OrderErrorCodeEnum.ORDER_NOT_ALLOW_TO_LACK);
            }

            // 1.參數效驗
            CheckLackDTO checkResult = orderLackService.checkRequest(lackRequest);
            // 2.缺品處理
            try {
                return ResponseData.success(orderLackService.executeLackRequest(lackRequest,checkResult));
            }finally {
                redisLock.unLock(lockKey);
            }
        }catch (OrderBizException e){
            log.error("biz error",e);
            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());
        }catch (Exception e){
            log.error("system error",e);
            return ResponseData.error(e.getMessage());
        }
    }

    @Override
    public ResponseData<Boolean> refundCallback(RefundCallbackRequest refundCallbackRequest) {
        String orderId = refundCallbackRequest.getOrderId();

        log.info("接受到取消订单支付退款回调，orderId：{}",orderId);

        return ResponseData.success(orderAfterSaleService.receivePaymentRefundCallback(refundCallbackRequest));
    }

    @Override
    public ResponseData<Boolean> receiveCustomerAuditResult(CustomReviewReturnGoodsRequest customReviewReturnGoodsRequest) {

        // 1组装接受客服审核结果的数据
        CustomerAuditAssembleRequest customerAuditAssembleRequest = buildCustomerAuditAssembleData(customReviewReturnGoodsRequest);

        // 客服审核拒绝

        if (CustomerAuditResult.REJECT.getCode().equals(customerAuditAssembleRequest.getReviewReasonCode())){
            //更新 审核拒绝 售后消息
          Boolean result =   orderAfterSaleService.receiveCustomerAuditReject(customerAuditAssembleRequest);
            return ResponseData.success(result);
        }
            // 客服 审核通过
        if (CustomerAuditResult.ACCEPT.getCode().equals(customerAuditAssembleRequest.getReviewReasonCode())){
            String orderId = customerAuditAssembleRequest.getOrderId();
            String afterSaleId = customerAuditAssembleRequest.getAfterSaleId();
            AfterSaleItemDO afterSaleItemDO = afterSaleItemDao.getOrderIdAndAfterSaleId(orderId,afterSaleId);
            if (ObjectUtils.isEmpty(afterSaleItemDO)){
                throw new OrderBizException(OrderErrorCodeEnum.AFTER_SALE_ITEM_CANNOT_NULL);
            }

            //4.组装释放库存参数
            AuditPassReleaseAssetsRequest auditPassReleaseAssetRequest = buildAuditPassReleaseAssets(afterSaleItemDO,customerAuditAssembleRequest,orderId);

            // 5. 发送客服审核通过释放权益资产事务MQ
            sendAuditPassReleaseAssets(customerAuditAssembleRequest,auditPassReleaseAssetRequest);
        }
        return ResponseData.success(true);
    }

    /**
     * @description: 发送 审核 资产 消息
     * @param customerAuditAssembleRequest
     * @param auditPassReleaseAssetsRequest
     * @return  void
     * @author Long
     * @date: 2022/6/8 20:32
     */
    private void sendAuditPassReleaseAssets(CustomerAuditAssembleRequest customerAuditAssembleRequest, AuditPassReleaseAssetsRequest auditPassReleaseAssetsRequest) {


        TransactionMQProducer transactionMQProducer = customerAuditPassSendReleaseAssetsProducer.getTransactionMQProducer();

        setSendAuditPassReleaseAssetsListener(transactionMQProducer);

        sendAuditPassReleaseAssetsSuccessMessage(transactionMQProducer,customerAuditAssembleRequest,auditPassReleaseAssetsRequest);



    }
    
    /** 
     * @description:  审核通过 发送释放资产消息
     * @param transactionMQProducer 消息生产者
     * @param customerAuditAssembleRequest 客服审核 资产请求
     * @param auditPassReleaseAssetsRequest 审核通过释放资产请求
     * @return  void
     * @author Long
     * @date: 2022/6/8 20:55
     */ 
    private void sendAuditPassReleaseAssetsSuccessMessage(TransactionMQProducer transactionMQProducer, CustomerAuditAssembleRequest customerAuditAssembleRequest, AuditPassReleaseAssetsRequest auditPassReleaseAssetsRequest) {
        try {

            Message message = new MQMessage(RocketMQConstant.CUSTOMER_AUDIT_PASS_RELEASE_ASSETS_TOPIC,JSONObject.toJSONString(auditPassReleaseAssetsRequest).getBytes(StandardCharsets.UTF_8));
            // 6.发送事务MQ 消息 客服审核通过后释放权益资产
            TransactionSendResult result = transactionMQProducer.sendMessageInTransaction(message,customerAuditAssembleRequest);

            if (!result.getLocalTransactionState().equals(LocalTransactionState.COMMIT_MESSAGE)){
                throw new OrderBizException(OrderErrorCodeEnum.SEND_AUDIT_PASS_RELEASE_ASSETS_FAILED);
            }

        }catch (Exception e){
            throw new OrderBizException(OrderErrorCodeEnum.SEND_TRANSACTION_MQ_FAILED);
        }

    }

    /**
     * @description:  设置 发送 审核
     * @param transactionMQProducer
     * @return  void
     * @author Long
     * @date: 2022/6/8 20:36
     */
    private void setSendAuditPassReleaseAssetsListener(TransactionMQProducer transactionMQProducer) {

        transactionMQProducer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object arg) {

                try {

                    CustomerAuditAssembleRequest customerAuditAssembleRequest = (CustomerAuditAssembleRequest) arg;
                    //更新 审核通过 售后信息
                    orderAfterSaleService.receiveCustomerAuditAccept(customerAuditAssembleRequest);
                    return LocalTransactionState.COMMIT_MESSAGE;
                }catch (Exception e){
                    log.error("system error");
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }


            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
            AuditPassReleaseAssetsRequest message = JSONObject.parseObject(
                    new String(messageExt.getBody(),StandardCharsets.UTF_8),AuditPassReleaseAssetsRequest.class);


                Integer customerAuditAfterSaleStatus = orderAfterSaleService.findCustomerAuditSaleStatus(
                        message.getActualRefundMessage().getAfterSaleId());

                if (AfterSaleStatusEnum.REVIEW_PASS.getCode().equals(customerAuditAfterSaleStatus)){
                    return LocalTransactionState.COMMIT_MESSAGE;
                }

                return LocalTransactionState.ROLLBACK_MESSAGE;
            }
        });

    }


    /**
     * @description:   构建审核通过 释放 资产
     * @param afterSaleItemDO 售后条目消息
     *   @param customerAuditAssembleRequest 客户审核组装请求
     * @return
     * @author Long
     * @date: 2022/5/19 21:03
     */
    private AuditPassReleaseAssetsRequest buildAuditPassReleaseAssets(AfterSaleItemDO afterSaleItemDO, CustomerAuditAssembleRequest customerAuditAssembleRequest,String orderId) {

        AuditPassReleaseAssetsRequest auditPassReleaseAssetRequest = new AuditPassReleaseAssetsRequest();


        ReleaseProductStockDTO releaseProductStockDTO = new ReleaseProductStockDTO();

        List<ReleaseProductStockDTO.OrderItemRequest> orderItemRequests = new ArrayList<>();
        ReleaseProductStockDTO.OrderItemRequest orderItemRequest = new ReleaseProductStockDTO.OrderItemRequest();
        orderItemRequest.setSkuCode(afterSaleItemDO.getSkuCode());
        orderItemRequest.setSaleQuantity(afterSaleItemDO.getReturnQuantity());
        orderItemRequests.add(orderItemRequest);

        releaseProductStockDTO.setOrderId(orderId);
        releaseProductStockDTO.setOrderItemRequests(orderItemRequests);

        auditPassReleaseAssetRequest.setReleaseProductStockDTO(releaseProductStockDTO);

        // 实际退款数据
        ActualRefundMessage actualRefundMessage = new ActualRefundMessage();
        actualRefundMessage.setAfterSaleId(afterSaleItemDO.getAfterSaleId());
        actualRefundMessage.setOrderId(afterSaleItemDO.getOrderId());
      //  actualRefundMessage.setAfterSaleRefundId(customerAuditAssembleRequest.getAfterSaleRefundId());

        /**
         * 当前版本判断售后条目是否订单所属的最后一条 业务限制：
         * 手动售后是整笔条目退，这里判断本次售后条目是否属于改订单的最后一个可售后的条目逻辑：
         * 如果 正向下单的订单条目总条数：= 售后已退款成功的订单条目 + 1（本次客服审核通过的这笔条目）
         * 那么 当前这笔审核通过的条目就是整笔订单的最后一条
         */

        // 判断是否要退的最后一条
        String afterSaleId = customerAuditAssembleRequest.getAfterSaleId();

        List<OrderItemDO> orderItemDOs = orderItemDao.listByOrderId(orderId);

        //查询 售后订单条目表中不包含当前条目的数量
        List<AfterSaleItemDO> afterSaleItems = afterSaleItemDao.listNotContainCurrentAfterSaleId(orderId,afterSaleId);

        // 查出数据中是否包含当前正在审核的这条
        if (orderItemDOs.size() ==  afterSaleItems.size() + 1){
            //本次条目就是当前订单的最后一笔
            actualRefundMessage.setLastReturnGoods(true);
        }
        auditPassReleaseAssetRequest.setActualRefundMessage(actualRefundMessage);

        return  auditPassReleaseAssetRequest;
    }


    /**
     * @description:  构造 客户审核组装请求
     * @param customReviewReturnGoodsRequest 自定义审核退货请求
     * @return
     * @author Long
     * @date: 2022/5/19 21:03
     */
    private CustomerAuditAssembleRequest buildCustomerAuditAssembleData(CustomReviewReturnGoodsRequest customReviewReturnGoodsRequest) {

        CustomerAuditAssembleRequest customerAuditAssembleRequest = new CustomerAuditAssembleRequest();

        String afterSaleId = customerAuditAssembleRequest.getAfterSaleId();

        String orderId = customerAuditAssembleRequest.getOrderId();

        Integer auditResult = customReviewReturnGoodsRequest.getAuditResult();

        customerAuditAssembleRequest.setAfterSaleId(afterSaleId);
        customerAuditAssembleRequest.setOrderId(orderId);
        customerAuditAssembleRequest.setAfterSaleRefundId(customReviewReturnGoodsRequest.getAfterSaleRefundId());
        customerAuditAssembleRequest.setReviewTime(new Date());
        customerAuditAssembleRequest.setReviewSource(CustomerAuditSourceEnum.SELF_MALL.getCode());
        customerAuditAssembleRequest.setReviewReasonCode(auditResult);
        customerAuditAssembleRequest.setAuditResultDesc(customerAuditAssembleRequest.getAuditResultDesc());

        return customerAuditAssembleRequest;

    }

    @Override
    public ResponseData<Boolean> revokeAfterSale(RevokeAfterSaleRequest revokeAfterSaleRequest) {
        // 1. 参数效应
        ParamCheckUtil.checkObjectNonNull(revokeAfterSaleRequest.getAfterSaleId(),OrderErrorCodeEnum.AFTER_SALE_ID_IS_NULL);

        String afterSaleId = revokeAfterSaleRequest.getAfterSaleId();

        String lockKey = RedisLockKeyConstants.REFUND_KEY + afterSaleId;

        //2 . 加锁 ，锁整个销售单 2个作用

        //2.1 防止并发
        //2.2 业务上考虑 ：只涉及售后表更新 ，就需要加锁，锁定整个售后表，否则算钱的时候，就由于突然撤销，导致钱多算

        if (!redisLock.tryLock(lockKey)){
            throw new OrderBizException(OrderErrorCodeEnum.AFTER_SALE_CANNOT_REVOKE);
        }

        try {
            // 3.撤销申请
            orderAfterSaleService.revokeAfterSale(revokeAfterSaleRequest);
        }finally {
            redisLock.unLock(lockKey);
        }


        return ResponseData.success(true);
    }

    @Override
    public ResponseData<Long> customerFindAfterSaleRefundInfo(CustomerReceiveAfterSaleRequest customerReceiveAfterSaleRequest) {

        Long afterSaleId = customerReceiveAfterSaleRequest.getAfterSaleId();

        AfterSaleRefundDO afterSaleRefundDO = afterSaleRefundDAO.findAfterSaleRefundByfterSaleId(afterSaleId);

        if(ObjectUtils.isEmpty(afterSaleRefundDO)){
            throw new OrderBizException(OrderErrorCodeEnum.AFTER_SALE_ID_IS_NULL);
        }
        return ResponseData.success(afterSaleRefundDO.getId());
    }
}
