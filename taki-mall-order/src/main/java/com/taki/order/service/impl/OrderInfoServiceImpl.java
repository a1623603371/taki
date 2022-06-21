package com.taki.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.taki.address.api.AddressApi;
import com.taki.address.domian.dto.AddressDTO;
import com.taki.address.domian.request.AddressQuery;
import com.taki.common.constants.RedisLockKeyConstants;
import com.taki.common.constants.RocketDelayedLevel;
import com.taki.common.constants.RocketMQConstant;
import com.taki.common.core.CloneDirection;
import com.taki.common.enums.AmountTypeEnum;
import com.taki.common.enums.OrderOperateTypeEnum;
import com.taki.common.enums.OrderStatusEnum;
import com.taki.common.enums.PayTypeEnum;
import com.taki.common.exception.ServiceException;
import com.taki.common.message.PaidOrderSuccessMessage;
import com.taki.common.message.PayOrderTimeOutDelayMessage;
import com.taki.common.redis.RedisLock;
import com.taki.common.utlis.*;
import com.taki.inventory.api.InventoryApi;
import com.taki.market.api.MarketApi;
import com.taki.market.domain.dto.CalculateOrderAmountDTO;
import com.taki.market.domain.dto.UserCouponDTO;
import com.taki.market.request.CalculateOrderAmountRequest;
import com.taki.market.request.LockUserCouponRequest;
import com.taki.market.domain.query.UserCouponQuery;
import com.taki.order.bulider.FullOrderData;
import com.taki.order.bulider.NewOrderBuilder;
import com.taki.order.config.OrderProperties;
import com.taki.order.dao.*;
import com.taki.order.domain.dto.*;
import com.taki.order.domain.request.*;
import com.taki.order.domain.entity.*;
import com.taki.order.manager.OrderManager;
import com.taki.order.mq.producer.DefaultProducer;
import com.taki.order.mq.producer.PaidOrderSuccessProducer;
import com.taki.order.remote.*;
import com.taki.pay.api.PayApi;
import com.taki.pay.domian.dto.PayOrderDTO;
import com.taki.pay.domian.rquest.PayOrderRequest;
import com.taki.pay.domian.rquest.PayRefundRequest;
import com.taki.product.domian.dto.OrderAmountDetailDTO;
import com.taki.order.enums.*;
import com.taki.order.exception.OrderBizException;
import com.taki.order.exception.OrderErrorCodeEnum;
import com.taki.order.manager.OrderNoManager;
import com.taki.order.service.OrderInfoService;
import com.taki.product.api.ProductApi;
import com.taki.product.domian.dto.ProductSkuDTO;
import com.taki.product.domian.query.ProductSkuQuery;
import com.taki.risk.api.RiskApi;
import com.taki.risk.domain.dto.CheckOrderRiskDTO;
import com.taki.risk.domain.request.CheckOrderRiskRequest;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName OrderInfoServiceImpl
 * @Description 订单 service接口实现
 * @Author Long
 * @Date 2022/1/2 17:07
 * @Version 1.0
 */
@Service
@Slf4j
public class OrderInfoServiceImpl implements OrderInfoService {

    @Autowired
    private OrderInfoDao orderInfoDao;

    @Autowired
    private OrderDeliveryDetailDao orderDeliveryDetailDao;


    @Autowired
    private OrderPaymentDetailDao orderPaymentDetailDao;

    @Autowired
    private OrderNoManager orderNoManager;


    @Autowired
    private OrderManager orderManager;


    @Autowired
    private DefaultProducer defaultProducer;


    @Autowired
    private PaidOrderSuccessProducer paidOrderSuccessProducer;

    @Autowired
    private RedisLock redisLock;



    /**
     * 营销服务
     */
    @Autowired
    private MarketRemote marketRemote;
    /**
     * 风控服务
     */
    @Autowired
    private RiskRemote riskRemote;

    /**
     * 商品服务
     */
    @Autowired
    private ProductRemote productRemote;

    /**
     * 支付服务
     */
    @Autowired
    private PayRemote payRemote;


    @Override
    public GenOrderIdDTO getGenOrderId(GenOrderIdRequest genOrderIdRequest) {


        log.info(LoggerFormat.build()
                .remark("genOrderId -> request")
                .data("request",genOrderIdRequest)
                .finish());

        String userId = genOrderIdRequest.getUserId();
        ParamCheckUtil.checkStringNonEmpty(userId);
        Integer businessIdentifier = genOrderIdRequest.getBusinessIdentifier();
        ParamCheckUtil.checkObjectNonNull(businessIdentifier);


        String orderId = orderNoManager.genOrderId(OrderAutoTypeEnum.SALE_ORDER.getCode(),userId);
        GenOrderIdDTO genOrderId = new GenOrderIdDTO();
        genOrderId.setOrderId(orderId);

        log.info(LoggerFormat.build()
                .remark("genOrderId -> request")
                .data("response",genOrderId)
                .finish());

        return genOrderId;
    }

   // @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public CreateOrderDTO createOrder(CreateOrderRequest createOrderRequest) {

        log.info(LoggerFormat.build()
                .remark("createOrder -> request")
                .data("request",createOrderRequest)
                .finish());


        // 1.入参检查
        checkCreateOrderRequestParam(createOrderRequest);

        // 2.风控检查
        checkRisk(createOrderRequest);

        // 3.查询商品
        List<ProductSkuDTO> productSkus = listProductSkus(createOrderRequest);

        // 4.计算订单商品价格
        CalculateOrderAmountDTO calculateOrderAmount = calculateOrderAmount(createOrderRequest,productSkus);

        // 5.验证订单实付金额
        checkRealPayAmount(createOrderRequest,calculateOrderAmount);

        //6. 生成订单到数据库
        createOrder(createOrderRequest,productSkus,calculateOrderAmount);

        // 7.发送延时订单消息用于支付超时自动关单
        sendPayOrderTimeoutDelayMessage(createOrderRequest);
        // 返回订单数据
        CreateOrderDTO createOrderDTO = new CreateOrderDTO();
        createOrderDTO.setOrderId(createOrderDTO.getOrderId());
        return createOrderDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PrePayOrderDTO prePayOrder(PrePayOrderRequest prePayOrderRequest) {

        log.info(LoggerFormat.build()
                .remark("preOrder -> request")
                .data("request",prePayOrderRequest)
                .finish());

        // 入参检查
        checkPerPayOrderRequestPram(prePayOrderRequest);

        String orderId = prePayOrderRequest.getOrderId();

        BigDecimal payAmount = prePayOrderRequest.getPayAmount();

        //  加分布式锁（与订单支付回调加的是同一把锁）

        String lockKey = RedisLockKeyConstants.ORDER_PAY_KEY + orderId;

        prePayOrderKey(lockKey);
        try {
            // 预支付订单检查
            checkPerPayOrderInfo(orderId,payAmount);

            //调用 支付系统 进行预支付
            PayOrderRequest payOrderRequest =  prePayOrderRequest.clone(PayOrderRequest.class);

            PayOrderDTO payOrder = payRemote.payOrder(payOrderRequest);
            //更新订单表 与支付信息表
            updateOrderPaymentInfo(payOrder);

            PrePayOrderDTO prePayOrderDTO = payOrder.clone(PrePayOrderDTO.class);

            log.info(LoggerFormat.build()
                    .remark("prePayOrder -> request")
                    .data("response",prePayOrderDTO)
                    .finish());
            return prePayOrderDTO;
        }finally {
            redisLock.unLock(lockKey);
        }

    }

    /**
     * @description: 支付 分布式锁
     * @param lockKey 锁key
     * @return  void
     * @author Long
     * @date: 2022/6/8 14:05
     */
    private void prePayOrderKey( String lockKey){
        boolean lock = redisLock.tryLock(lockKey);
        if (!lock){
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_PRE_PAY_ERROR);
        }
    }


    @Override
    public void payCallback(PayCallbackRequest payCallbackRequest) {

        log.info(LoggerFormat.build()
                .remark("payCallback->request")
                .data("request",payCallbackRequest)
                .finish());

        String orderId = payCallbackRequest.getOrderId();
        Integer payType = payCallbackRequest.getPayType();

        // 从数据库中查询出订单信息
        OrderInfoDO orderInfo = orderInfoDao.getByOrderId(orderId);
        OrderPaymentDetailDO orderPaymentDetail = orderPaymentDetailDao.getPaymentDetailByOrderId(orderId);
        // 入参检查
        checkPayCallbackRequestParam(payCallbackRequest, orderInfo,orderPaymentDetail);
        // 为支付回调操作进行多重分布式锁加锁
        List<String> redisKeyList = Lists.newArrayList();
        payCallbackMultiLock(redisKeyList,orderId);

        try {
        // 异常场景
        Integer orderStatus = orderInfo.getOrderStatus();

        Integer payStatus = orderPaymentDetail.getPayStatus();
        // 幂等检查
        if (!OrderStatusEnum.CREATED.getCode().equals(orderStatus)){
            // 异常场景
            payCallbackFailure(orderStatus,payStatus,payType,orderPaymentDetail,orderInfo);
            return;

        }
        //执行正式得订单支付回调处理
        doPayCallback(orderInfo);

        log.info(LoggerFormat.build()
                .remark("payCallback -> response")
                .finish());

        }catch (Exception e){
            log.error("payCallback error",e);
            throw new OrderBizException(e.getMessage());
        }finally {
            redisLock.unMultiLock(redisKeyList);
        }



    }
    /** 
     * @description:  支付回调成功的时候处理逻辑
     * @param orderInfo
     * @return  void
     * @author Long
     * @date: 2022/6/8 18:41
     */ 
    private void doPayCallback(OrderInfoDO orderInfo) throws MQClientException {
        // 如果 订单状态 “已创建” 直接更新订单状态为 已支付 ，并发送事务消息
        TransactionMQProducer transactionMQProducer = paidOrderSuccessProducer.getTransactionMQProducer();
        setPayCallbackTransactionListener(transactionMQProducer);
        sendPayCallbackSuccessMessage(transactionMQProducer,orderInfo);
    }

    /** 
     * @description: 发送订单已支付信息，触发履约
     * @param transactionMQProducer
     * @param  orderInfo
     * @return  void
     * @author Long
     * @date: 2022/6/8 22:22
     */ 
    private void sendPayCallbackSuccessMessage(TransactionMQProducer transactionMQProducer, OrderInfoDO orderInfo) throws MQClientException {
        String orderId = orderInfo.getOrderId();

        PaidOrderSuccessMessage message = new PaidOrderSuccessMessage();
        message.setOrderId(orderId);

        log.info(LoggerFormat.build()
                .remark("发送订单已支付消息")
                .data("message",message)
                .finish());
        String topic = RocketMQConstant.PAID_ORDER_SUCCESS_TOPIC;

        byte[] body = JSON.toJSONString(message).getBytes();

        Message mq = new Message(topic,null,orderId,body);

        TransactionSendResult result  = transactionMQProducer.sendMessageInTransaction(mq,orderInfo);

        if (!SendStatus.SEND_OK.equals(result.getSendStatus())){
        throw new OrderBizException(OrderErrorCodeEnum.ORDER_PAY_CALLBACK_SEND_MQ_ERROR);
        }
    }

    /** 
     * @description: 发送支付成功消息，时，设置事务消息 TransactionListener 组件
     * @param transactionMQProducer
     * @return  void
     * @author Long
     * @date: 2022/6/8 21:12
     */ 
    private void setPayCallbackTransactionListener(TransactionMQProducer transactionMQProducer) {
        transactionMQProducer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                try {
                    OrderInfoDO orderInfo = (OrderInfoDO) o;
                    orderManager.updateOrderStatusWhenPayCallback(orderInfo);
                    return  LocalTransactionState.COMMIT_MESSAGE;
                }catch (ServiceException e){
                    throw e;
                }catch (Exception e){
                    log.error("system error",e);
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                PaidOrderSuccessMessage paidOrderSuccessMessage = JSON.parseObject(
                        new String(messageExt.getBody(), StandardCharsets.UTF_8),PaidOrderSuccessMessage.class);

                // 检查订单是否已支付
                OrderInfoDO orderInfoDO = orderInfoDao.getByOrderId(paidOrderSuccessMessage.getOrderId());
                if(orderInfoDO != null && OrderStatusEnum.PAID.getCode().equals(orderInfoDO.getOrderStatus())){

                    return LocalTransactionState.COMMIT_MESSAGE;

                }
                return LocalTransactionState.ROLLBACK_MESSAGE;
            }
        });

    }

    /** 
     * @description: 支付回调异常处理逻辑
     * @param orderStatus 订单整体
     * @param payStatus 支付状态
     * @param payType 支付类型
     * @param orderPaymentDetail 订单支付详情
     * @param orderInfo 订单数据
     * @return  void
     * @author Long
     * @date: 2022/6/8 16:36
     */ 
    private void payCallbackFailure(Integer orderStatus, Integer payStatus, Integer payType, OrderPaymentDetailDO orderPaymentDetail, OrderInfoDO orderInfo) {
        // 如果订单状态 不是 “已创建”
        // 可能是支付回调就取消了订单，也可能支付回调成功后取消
        if (OrderStatusEnum.CANCELED.getCode().equals(orderStatus)) {
            //此时如果订单得支付状态是未支付得话
            //说明用户在取消订单得时候，支付系统还没有完成回调，而支付系统又已经扣了用户得钱，已调用一下退款
            if (PayStatusEnum.UNPAID.getCode().equals(payStatus)) {
                // 调用退款
                executeOrderRefund(orderInfo, orderPaymentDetail);
                throw new OrderBizException(OrderErrorCodeEnum.ORDER_CANCEL_PAY_CALLBACK_ERROR);

                //如果是已支付状态 代表了 用户在取消得时候不是 已创建状态了
            } else if (PayStatusEnum.PAID.getCode().equals(payStatus)) {
                if (payType.equals(orderPaymentDetail.getPayType())) {
                    //  非 “已创建”状态订单得取消状态操作本身就会进行退款
                    // 所以如果同种支付方式，说明用户并没有进行多次支付，不需要调用退款接口
                    throw new OrderBizException(OrderErrorCodeEnum.ORDER_CANCEL_PAY_CALLBACK_PAY_TYPE_SAME_ERROR);
                } else {
                    // 非同种支付方式，说明用户 用户 2中不同得支付方式进行了重复支付，所以调用退款接口

                    executeOrderRefund(orderInfo,orderPaymentDetail);

                    throw new OrderBizException(OrderErrorCodeEnum.ORDER_CANCEL_PAY_CALLBACK_PAY_TYPE_NO_SAME_ERROR);
                }
            }else {
                // 如果订单状态不是取消状态(那么就是 已履约，已出库，已配送 状态中)
                if (PayStatusEnum.PAID.getCode().equals(payStatus)){
                    // 如果是同种支付方式回调，说明用户是并m没有发起重复付款，只是支付系统 多触发了一次支付回调
                    //  这里做幂等判断，直接 返回，不需要调用 退款接口
                    if (payType.equals(orderPaymentDetail.getPayType())){
                        return;
                    }
                    //  如果 非同种支付方式，说明用户 更换了支付方式，发起了重复支付，所以调用退款接口进行退款
                    // 调用退款
                    executeOrderRefund(orderInfo,orderPaymentDetail);
                    throw new OrderBizException(OrderErrorCodeEnum.ORDER_CANCEL_PAY_CALLBACK_REPEAT_ERROR);
                }
            }
        }

    }


    /** 
     * @description: 支付回调加分布式锁
     * @param redisKeyList 锁集合
     * @param  orderId 订单id
     * @return  void
     * @author Long
     * @date: 2022/6/8 16:30
     */ 
    private void payCallbackMultiLock(List<String> redisKeyList, String orderId) {
        // 加支付分布式锁 避免重复支付
        String orderPayKey = RedisLockKeyConstants.ORDER_PAY_KEY + orderId;
        // 加 取消分布式锁 避免 取消同一笔订单
        String cancelOrderPayKey = RedisLockKeyConstants.CANCEL_KEY + orderId;
        redisKeyList.add(orderPayKey);
        redisKeyList.add(cancelOrderPayKey);
        boolean lock = redisLock.multiLock(redisKeyList);
        if (!lock){
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_PAY_CALLBACK_ERROR);
        }

    }

    @Override
    public Boolean removeOrders(List<String> orderIds) {
        List<OrderInfoDO> orderInfos = orderInfoDao.listByOrderIds(orderIds);

        if (ObjectUtils.isEmpty(orderInfos) || orderInfos.isEmpty()){
            return  true;
        }
        //  效验订单是否可以移除
        orderInfos.forEach(orderInfoDO -> {
            if (!canRemove(orderInfoDO)){
                throw new OrderBizException(OrderErrorCodeEnum.ORDER_CANNOT_REMOVE);
            }
        });

        //移除订单
      Boolean result =   orderInfoDao.softRemoveOrders(orderIds);
        return result;
    }
    
    /** 
     * @description: 判断订单是否可以移除
     * @param orderInfoDO 订单 信息
     * @return  boolean
     * @author Long
     * @date: 2022/2/26 19:33
     */ 
    private boolean canRemove(OrderInfoDO orderInfoDO) {
        return OrderStatusEnum.canRemoveStatus()
                .contains(orderInfoDO.getOrderStatus()) &&
                DeleteStatusEnum.NO.getCode().equals(orderInfoDO.getDeleteStatus());
    }


    @Override
    public Boolean adjustDeliveryAddress(AdjustDeliveryAddressRequest adjustDeliveryAddressRequest) {
        // 1.根据 id 查询订单
    OrderInfoDO orderInfo = orderInfoDao.getByOrderId(adjustDeliveryAddressRequest.getOrderId());
    ParamCheckUtil.checkObjectNonNull(orderInfo,OrderErrorCodeEnum.ORDER_NOT_FOUND);

    // 2效验 订单 是否 未出库
        if (!OrderStatusEnum.unOutStockStatus().contains(orderInfo.getOrderStatus())){
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_NOT_ALLOW_TO_ADJUST_ADDRESS);
        }
     // 3 查询订单配送信息

        OrderDeliveryDetailDO orderDeliveryDetail = orderDeliveryDetailDao.getByOrderId(orderInfo.getOrderId());

        if (ObjectUtils.isEmpty(orderDeliveryDetail)){
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_DELIVERY_NOT_FOUND);
        }

        // 4. 效验 配送信息是否已经被修改

        if (orderDeliveryDetail.getModifyAddressCount() > 0){
            throw  new OrderBizException(OrderErrorCodeEnum.ORDER_DELIVERY_ADDRESS_HAS_BEEN_ADJUSTED);
        }

        // 5.更新订单配送订单

       Boolean result =   orderDeliveryDetailDao.updateDeliveryAddress(orderDeliveryDetail.getId(),orderDeliveryDetail.getModifyAddressCount(),adjustDeliveryAddressRequest);

        return result;
    }



    /***
     * @description: 进行订单退款
     * @param orderInfo 订单信息
     * @param   orderPaymentDetail 订单支付详情
     * @return  void
     * @author Long
     * @date: 2022/1/18 16:15
     */
    private void executeOrderRefund(OrderInfoDO orderInfo, OrderPaymentDetailDO orderPaymentDetail) {
        PayRefundRequest payRefundRequest = new PayRefundRequest();
        payRefundRequest.setOrderId(orderInfo.getOrderId());
        payRefundRequest.setRefundAmount(orderPaymentDetail.getPayAmount());
        payRefundRequest.setOutTradeNo(orderPaymentDetail.getOutTradeNo());
        payRemote.executeRefund(payRefundRequest);
    }



    /**
     * @description: 检查支付回调请求参数
     * @param payCallbackRequest 支付回调请求
     * @return  void
     * @author Long
     * @date: 2022/1/17 16:15
     */
    private void checkPayCallbackRequestParam(PayCallbackRequest payCallbackRequest,OrderInfoDO orderInfo,OrderPaymentDetailDO orderPaymentDetail) {
        ParamCheckUtil.checkObjectNonNull(payCallbackRequest);

        // 订单号
        String orderId = payCallbackRequest.getOrderId();
        ParamCheckUtil.checkStringNonEmpty(orderId);

        // 支付金额
        BigDecimal payAmount = payCallbackRequest.getPayAmount();
        ParamCheckUtil.checkObjectNonNull(payAmount);

        // 支付系统交易流水号
        String outTradeNo = payCallbackRequest.getOutTraderNo();
        ParamCheckUtil.checkStringNonEmpty(outTradeNo);

        // 支付类型
        Integer payType = payCallbackRequest.getPayType();
        ParamCheckUtil.checkObjectNonNull(payType);

        if (PayTypeEnum.getByCode(payType) == null){
            throw new OrderBizException(OrderErrorCodeEnum.PAY_TYPE_PARAM_ERROR);
        }

        // 商户Id
        String merchantId = payCallbackRequest.getMerchantId();
        ParamCheckUtil.checkStringNonEmpty(merchantId);

        // 检验参数
        if(ObjectUtils.isEmpty(orderInfo) || ObjectUtils.isEmpty(orderPaymentDetail)){
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_INFO_IS_NULL);
        }

        if (payAmount.compareTo(orderInfo.getPayAmount()) == 0){
            throw new ServiceException(OrderErrorCodeEnum.ORDER_PAY_AMOUNT_ERROR);
        }

    }


    /**
     * @description: 更新订单支付信息
     * @param payOrder 支付订单
     * @return  void
     * @author Long
     * @date: 2022/1/16 15:31
     */
    private void updateOrderPaymentInfo(PayOrderDTO payOrder) {
        String orderId = payOrder.getOrderId();
       Integer payType = payOrder.getPayType();
        String outTradeNo = payOrder.getOutTradeNo();
        LocalDateTime payTime = LocalDateTime.now();

        // 更新 主订单 支付信息
        updateMasterOrderPaymentInfo(orderId,payType,outTradeNo,payTime);

        updateSubOrderPaymentInfo(orderId,payType,payTime,outTradeNo);


    }

    /**
     * @description:  更新子订单支付详情
     * @param orderId 订单 id
     * @param  payType 支付类型
     * @param payTime 支付时间
     * @param outTradeNo 交易系统流水Id
     * @return  void
     * @author Long
     * @date: 2022/6/8 14:50
     */
    private void updateSubOrderPaymentInfo(String orderId, Integer payType, LocalDateTime payTime, String outTradeNo) {
        //判断是否存在子订单，不存在则不处理
        List<String> subOrderIds = orderInfoDao.listSubOrderIds(orderId);

        if (CollectionUtils.isEmpty(subOrderIds)){
            return;
        }
        // 更新子订单信息
        updateOrderInfo(subOrderIds,payType,payTime);

        //更新子订单支付信息明细信息
        updateOrderPaymentDetail(subOrderIds,payType,payTime,outTradeNo);

    }

    /**
     * @description: 更新主订单支付信息
     * @param orderId 订单Id
     * @param payType 支付方式
     * @param outTradeNo 交易系统流水号
     * @param payTime 支付时间
     * @return  void
     * @author Long
     * @date: 2022/6/8 14:16
     */
    private void updateMasterOrderPaymentInfo(String orderId, Integer payType, String outTradeNo, LocalDateTime payTime) {

        List<String> orderIds = Collections.singletonList(orderId);

        // 更新订单支付明细 信息
        updateOrderInfo(orderIds,payType,payTime);

        //更新订单支付信息
        updateOrderPaymentDetail(orderIds,payType,payTime,outTradeNo);


    }

    /**
     * @description: 更新 订单支付明细
     * @param orderIds 订单Id 集合
     * @param payType 支付类型
     * @param payTime 支付时间
     * @param outTradeNo 订单系统交易流水id
     * @return  void
     * @author Long
     * @date: 2022/6/8 14:41
     */
    private void updateOrderPaymentDetail(List<String> orderIds, Integer payType, LocalDateTime payTime, String outTradeNo) {
        if (CollectionUtils.isEmpty(orderIds)){
            return;
        }
        OrderPaymentDetailDO orderPaymentDetailDO = new OrderPaymentDetailDO();

        orderPaymentDetailDO.setPayType(payType);
        orderPaymentDetailDO.setPayTime(payTime);
        orderPaymentDetailDO.setOutTradeNo(outTradeNo);

        if (orderIds.size() == 1){
            orderPaymentDetailDao.updateByOrderId(orderPaymentDetailDO,orderIds.get(0));
        }else {
            orderPaymentDetailDao.updateBatchByOrderId(orderPaymentDetailDO,orderIds);
        }

    }

    /**
     * @description:  更新 订单支付明细信息
     * @param orderIds 订单id 集合
     * @param payType 支付类型
     * @param payTime 支付时间
     * @return  void
     * @author Long
     * @date: 2022/6/8 14:19
     */
    private void updateOrderInfo(List<String> orderIds, Integer payType, LocalDateTime payTime) {

        if (CollectionUtils.isEmpty(orderIds)){
            return;
        }

        OrderInfoDO orderInfoDO = new OrderInfoDO();

        orderInfoDO.setPayType(payType);
        orderInfoDO.setPayTime(payTime);

        if (orderIds.size() == 1){
            orderInfoDao.updateByOrderId(orderInfoDO,orderIds.get(0));
        }else {
            orderInfoDao.updateBatchByOrderId(orderInfoDO,orderIds);
        }


    }

    /***
     * @description: 检查预支付订单
     * @param orderId 订单Id
     * @param  payAmount 支付金额
     * @return  void
     * @author Long
     * @date: 2022/1/16 13:37
     */
    private void checkPerPayOrderInfo(String orderId, BigDecimal payAmount) {
        // 查询订单信息
        OrderInfoDO orderInfo = orderInfoDao.getByOrderId(orderId);

        OrderPaymentDetailDO orderPaymentDetail = orderPaymentDetailDao.getPaymentDetailByOrderId(orderId);

        if(ObjectUtils.isEmpty(orderInfo) || ObjectUtils.isEmpty(orderPaymentDetail)){
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_INFO_IS_NULL);
        }

        //检查订单支付金额
        if (payAmount.compareTo(orderInfo.getPayAmount()) != 0){
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_PAY_AMOUNT_ERROR);
        }

        // 判断订单状态
        if (!OrderStatusEnum.CREATED.getCode().equals(orderInfo.getOrderStatus())){
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_STATUS_ERROR);
        }
        // 判断支付状态
        if (PayStatusEnum.PAID.getCode().equals(orderInfo.getOrderStatus())){
          throw new OrderBizException(OrderErrorCodeEnum.ORDER_PAY_STATUS_IS_PAID);
        }

        // 判断是否超时
        LocalDateTime currentDate = LocalDateTime.now();

        if (currentDate.compareTo(orderInfo.getExpireTime()) < 0){
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_PRE_PAY_EXPIRE_ERROR);
        }

    }

    /**
     * @description: 检查预支付订单请求参数
     * @param prePayOrderRequest 预支付请求
     * @return  void
     * @author Long
     * @date: 2022/1/15 14:48
     */
    private void checkPerPayOrderRequestPram(PrePayOrderRequest prePayOrderRequest) {
        String userId = prePayOrderRequest.getUserId();
        ParamCheckUtil.checkStringNonEmpty(userId,OrderErrorCodeEnum.USER_ID_IS_NULL);

        String businessIdentifier = prePayOrderRequest.getBusinessIdentifier();
        ParamCheckUtil.checkObjectNonNull(businessIdentifier,OrderErrorCodeEnum.BUSINESS_IDENTIFIER_ERROR);

        Integer payType = prePayOrderRequest.getPayType();

        ParamCheckUtil.checkObjectNonNull(payType,OrderErrorCodeEnum.PAY_TYPE_PARAM_ERROR);

        if (PayTypeEnum.getByCode(payType) == null){
            throw new OrderBizException(OrderErrorCodeEnum.PAY_TYPE_PARAM_ERROR);
        }

        String orderId = prePayOrderRequest.getOrderId();
        ParamCheckUtil.checkStringNonEmpty(orderId,OrderErrorCodeEnum.ORDER_ID_IS_NULL);

        BigDecimal payAmount = prePayOrderRequest.getPayAmount();
        ParamCheckUtil.checkObjectNonNull(payAmount,OrderErrorCodeEnum.PAY_TYPE_PARAM_ERROR);
    }

    /**
     * @description: 发送 订单延时支付消息，用于支付
     * @param createOrderRequest
     * @return  void
     * @author Long
     * @date: 2022/1/12 9:54
     */
    private void sendPayOrderTimeoutDelayMessage(CreateOrderRequest createOrderRequest) {

        String orderId = createOrderRequest.getOrderId();

        PayOrderTimeOutDelayMessage payOrderTimeOutDelayMessage = PayOrderTimeOutDelayMessage.builder()





                .orderId(orderId)
                .businessIdentifier(createOrderRequest.getBusinessIdentifier())
                .cancelType(OrderCancelTypeEnum.TIMEOUT_CANCELED.getCode())
                .orderType(createOrderRequest.getOrderType())
                .orderStatus(OrderStatusEnum.CREATED.getCode())
                .userId(createOrderRequest.getUserId()).build();


        String msgJson = JsonUtil.object2Json(payOrderTimeOutDelayMessage);

        defaultProducer.sendMessage(RocketMQConstant.PAY_ORDER_TIMEOUT_DELAY_TOPIC,msgJson,
                RocketDelayedLevel.DELAYED_30M,"支付订单超时延时消息",null,orderId);

    }



    /***
     * @description: 创建订单
     * @param createOrderRequest 创建订单请求 数据
     * @param productSkus 商品SKU 集合
     *  @param calculateOrderAmount    计算 订单金额 数据
     * @return  void
     * @author Long
     * @date: 2022/5/10 14:42
     */
    private void createOrder(CreateOrderRequest createOrderRequest,List<ProductSkuDTO> productSkus,CalculateOrderAmountDTO calculateOrderAmount ){

        orderManager.createOrder(createOrderRequest,productSkus, calculateOrderAmount );
    }


    /**
     * @description: 验证订单实付金额
     * @param createOrderRequest 创建订单请求
     * @param calculateOrderAmount 计算的订单费用
     * @return  void
     * @author Long
     * @date: 2022/1/6 9:17
     */ 
    private void checkRealPayAmount(CreateOrderRequest createOrderRequest, CalculateOrderAmountDTO calculateOrderAmount) {
        List<CreateOrderRequest.OrderAmountRequest> orderAmountRequests = createOrderRequest.getOrderAmountRequests();

        Map<Integer, CreateOrderRequest.OrderAmountRequest> originOrderAmountMap =orderAmountRequests.stream()
                .collect(Collectors.toMap(CreateOrderRequest.OrderAmountRequest::getAmountType,Function.identity()));

        BigDecimal originRealPayAmount = originOrderAmountMap.get(AmountTypeEnum.REAL_PAY_AMOUNT.getCode()).getAmount();

        Map<Integer,CalculateOrderAmountDTO.OrderAmountDTO> orderAmountMap = calculateOrderAmount.getOrderAmountDTOList().stream()
                .collect(Collectors.toMap(CalculateOrderAmountDTO.OrderAmountDTO::getAmountType,Function.identity()));

        // 营销计算的实付价格
        BigDecimal realPayAmount = orderAmountMap.get(AmountTypeEnum.REAL_PAY_AMOUNT.getCode()).getAmount();

        if (originRealPayAmount.compareTo(realPayAmount) != 0){
            log.error("验证订单实付价格不通过,前端传入价格:{},营销计算价格：{}",originRealPayAmount,realPayAmount);
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_CHECK_REAL_PAY_AMOUNT_FAIL);
        }

    }

    /**
     * @description: 计算订单费用 计算 优惠券 ,红包， 积分
     * @param createOrderRequest 生单请求
     * @return 计算结果
     * @author Long
     * @date: 2022/1/5 10:20
     */
    private CalculateOrderAmountDTO calculateOrderAmount(CreateOrderRequest createOrderRequest,List<ProductSkuDTO> productSkuList) {
        CalculateOrderAmountRequest calculateOrderAmountRequest = createOrderRequest.clone(CalculateOrderAmountRequest.class, CloneDirection.FORWARD);

        // 订单条目补充商品信息
        Map<String,ProductSkuDTO> productSkuDTOMap = productSkuList.stream().collect(Collectors.toMap(ProductSkuDTO::getSkuCode, Function.identity()));
        calculateOrderAmountRequest.getOrderItemRequests().forEach(orderItemRequest -> {
            String skuCode = orderItemRequest.getSkuCode();
            ProductSkuDTO productSku = productSkuDTOMap.get(skuCode);
            productSku.setSalePrice(orderItemRequest.getSalePrice());
            productSku.setProductId(orderItemRequest.getProductId());
        });
        // 调用营销服务计算订单价格
        CalculateOrderAmountDTO calculateOrderAmount = marketRemote.calculateOrderAmount(calculateOrderAmountRequest);
        if (!ObjectUtils.isNotEmpty(calculateOrderAmount)){
            throw new OrderBizException(OrderErrorCodeEnum.CALCULATE_ORDER_AMOUNT_ERROR);
        }
        // 订单费用
        List<OrderAmountDTO> orderAmountDTOList = ObjectUtil.convertList(calculateOrderAmount.getOrderAmountDTOList(),OrderAmountDTO.class);
        if (orderAmountDTOList == null || orderAmountDTOList.isEmpty()){
            throw  new OrderBizException(OrderErrorCodeEnum.CALCULATE_ORDER_AMOUNT_ERROR);
        }
        List<OrderAmountDetailDTO> orderAmountDetailList = ObjectUtil.convertList(calculateOrderAmount.getOrderAmountDetailDTOList(),OrderAmountDetailDTO.class);
        if (orderAmountDetailList == null || orderAmountDetailList.isEmpty()){
            throw  new OrderBizException(OrderErrorCodeEnum.CALCULATE_ORDER_AMOUNT_ERROR);
        }
        log.info(LoggerFormat.build()
                .remark("calculateOrderAmount->return")
                .data("return",calculateOrderAmount)
                .finish());
        return calculateOrderAmount;

    }

    /**
     * @description: 获取商品SKU
     * @param createOrderRequest 创建订单请求
     * @return  商品SKU 集合
     * @author Long
     * @date: 2022/1/4 11:32
     *
     */
    private List<ProductSkuDTO> listProductSkus(CreateOrderRequest createOrderRequest) {

        List<CreateOrderRequest.OrderItemRequest> orderItemRequests = createOrderRequest.getOrderItemRequests();


        List<String> skuCodeList = new ArrayList<>();

        orderItemRequests.forEach(orderItem->{
            skuCodeList.add(orderItem.getSkuCode());
        });

        List<ProductSkuDTO> productSkus = productRemote.listProductSku(skuCodeList,createOrderRequest.getSellerId());


        log.info(LoggerFormat.build()
                .remark("listProductSkus->return")
                .data("productSkus",productSkus)
                .finish());

        return productSkus;

    }

    /**
     * @description: 风控检查
     * @param createOrderRequest 生单请求
     * @return  void
     * @author Long
     * @date: 2022/1/4 9:37
     */
    private void checkRisk(CreateOrderRequest createOrderRequest) {
        CheckOrderRiskRequest checkOrderRiskRequest = createOrderRequest.clone(CheckOrderRiskRequest.class);
        riskRemote.checkOrderRisk(checkOrderRiskRequest);


    }

    /**
     * @description: 检查创建订单请求参数
     * @param createOrderRequest 创建订单请求
     * @return  void
     * @author Long
     * @date: 2022/1/3 14:58
     */
    private void checkCreateOrderRequestParam(CreateOrderRequest createOrderRequest) {
        ParamCheckUtil.checkObjectNonNull(createOrderRequest);

        // 订单Id
        String orderId = createOrderRequest.getOrderId();
        ParamCheckUtil.checkStringNonEmpty(orderId);

        //业务线标识
        Integer businessIdentifier = createOrderRequest.getBusinessIdentifier();
        ParamCheckUtil.checkObjectNonNull(businessIdentifier, OrderErrorCodeEnum.BUSINESS_IDENTIFIER_ERROR);

        if (BusinessIdentifierEnum.getByCode(businessIdentifier) == null){
            throw new ServiceException(OrderErrorCodeEnum.BUSINESS_IDENTIFIER_ERROR);
        }
        // 用户Id
        String userId = createOrderRequest.getUserId();
        ParamCheckUtil.checkStringNonEmpty(userId);

        //订单类型

        Integer orderType = createOrderRequest.getOrderType();;
        ParamCheckUtil.checkObjectNonNull(orderType,OrderErrorCodeEnum.ORDER_TYPE_IS_NULL);

        if (OrderTypeEnum.getByCode(orderType) == null){
            throw  new ServiceException(OrderErrorCodeEnum.ORDER_TYPE_ERROR);
        }

        //卖家Id
        String sellerId = createOrderRequest.getSellerId();
        ParamCheckUtil.checkStringNonEmpty(sellerId);

        //配送类型
        Integer deliverType = createOrderRequest.getDeliveryType();
        ParamCheckUtil.checkObjectNonNull(deliverType,OrderErrorCodeEnum.USER_ADDRESS_ERROR);
        if (DeliveryTypeEnum.getByCode(deliverType) == null){
            throw new ServiceException(OrderErrorCodeEnum.DELIVERY_TYPE_ERROR);
        }

        //地址信息
        String province = createOrderRequest.getProvince();
        String city = createOrderRequest.getCity();
        String area = createOrderRequest.getArea();
        String street = createOrderRequest.getStreet();

        ParamCheckUtil.checkStringNonEmpty(province,OrderErrorCodeEnum.USER_ADDRESS_ERROR);
        ParamCheckUtil.checkStringNonEmpty(city,OrderErrorCodeEnum.USER_ADDRESS_ERROR);
        ParamCheckUtil.checkStringNonEmpty(area,OrderErrorCodeEnum.USER_ADDRESS_ERROR);
        ParamCheckUtil.checkStringNonEmpty(street,OrderErrorCodeEnum.USER_ADDRESS_ERROR);

        // 区域Id
        String regionId = createOrderRequest.getRegionId();
        ParamCheckUtil.checkStringNonEmpty(regionId,OrderErrorCodeEnum.REGION_ID_IS_NULL);

        // 经纬度
        BigDecimal lon = createOrderRequest.getLon();
        BigDecimal lat = createOrderRequest.getLat();
        ParamCheckUtil.checkObjectNonNull(lon,OrderErrorCodeEnum.USER_LOCATION_IS_NULL);
        ParamCheckUtil.checkObjectNonNull(lat,OrderErrorCodeEnum.USER_LOCATION_IS_NULL);

        // 收货人信息

        String receiverName = createOrderRequest.getReceiverName();
        String receiverPhone = createOrderRequest.getReceiverPhone();
        ParamCheckUtil.checkStringNonEmpty(receiverName,OrderErrorCodeEnum.ORDER_RECEIVER_IS_NULL);
        ParamCheckUtil.checkStringNonEmpty(receiverPhone,OrderErrorCodeEnum.ORDER_RECEIVER_PHONE_IS_NULL);

        // 客户端设备信息
        String clientIp = createOrderRequest.getClientIp();
        ParamCheckUtil.checkStringNonEmpty(clientIp,OrderErrorCodeEnum.CLIENT_IP_IS_NULL);

        //商品条目信息
        List<CreateOrderRequest.OrderItemRequest> orderItemRequests = createOrderRequest.getOrderItemRequests();
        ParamCheckUtil.checkObjectNonNull(orderItemRequests,OrderErrorCodeEnum.ORDER_ITEM_IS_NULL);
        orderItemRequests.forEach(orderItemRequest -> {
            Integer productType = orderItemRequest.getProductType();
            Integer saleQuantity = orderItemRequest.getSaleQuantity();
            String  skuCode = orderItemRequest.getSkuCode();
            ParamCheckUtil.checkObjectNonNull(productType,OrderErrorCodeEnum.ORDER_ITEM_PARAM_ERROR);
            ParamCheckUtil.checkObjectNonNull(saleQuantity,OrderErrorCodeEnum.ORDER_ITEM_PARAM_ERROR);
            ParamCheckUtil.checkStringNonEmpty(skuCode,OrderErrorCodeEnum.ORDER_ITEM_PARAM_ERROR);
        });

        // 订单信息费用

        List<CreateOrderRequest.OrderAmountRequest> orderAmountRequests = createOrderRequest.getOrderAmountRequests();
        ParamCheckUtil.checkObjectNonNull(orderAmountRequests,OrderErrorCodeEnum.ORDER_AMOUNT_IS_NULL);
        orderAmountRequests.forEach(orderAmountRequest -> {
            Integer amountType = orderAmountRequest.getAmountType();
            ParamCheckUtil.checkObjectNonNull(amountType,OrderErrorCodeEnum.ORDER_AMOUNT_TYPE_PARAM_ERROR);
            if (AmountTypeEnum.getByCode(amountType) == null){
                throw  new ServiceException(OrderErrorCodeEnum.ORDER_AMOUNT_TYPE_PARAM_ERROR);
            }
        });

        Map<Integer,BigDecimal> orderAmountMap = orderAmountRequests.stream().collect(Collectors.toMap(CreateOrderRequest.OrderAmountRequest::getAmountType, CreateOrderRequest.OrderAmountRequest::getAmount));

        // 订单原价不能为空
        if (orderAmountMap.get(AmountTypeEnum.ORIGIN_PAY_AMOUNT) == null){
            throw new ServiceException(OrderErrorCodeEnum.ORDER_AMOUNT_TYPE_PARAM_ERROR);
        }
        // 订单运费不能为空
        if (orderAmountMap.get(AmountTypeEnum.SHIPPING_AMOUNT) == null){
            throw new ServiceException(OrderErrorCodeEnum.ORDER_SHIPPING_AMOUNT_IS_NULL);
        }
        // 订单实付价格不能为空
        if (orderAmountMap.get(AmountTypeEnum.REAL_PAY_AMOUNT) == null){
            throw new ServiceException(OrderErrorCodeEnum.ORDER_REAL_PAY_AMOUNT_IS_NULL);
        }

        if (!StringUtils.isNotBlank(createOrderRequest.getCouponId())){
            if(orderAmountMap.get(AmountTypeEnum.COUPON_DISCOUNT_AMOUNT) == null){
                throw new ServiceException(OrderErrorCodeEnum.ORDER_DISCOUNT_AMOUNT_IS_NULL);
            }

        }

        // 订单支付信息
        List<CreateOrderRequest.PaymentRequest> paymentRequests = createOrderRequest.getPaymentRequests();
        ParamCheckUtil.checkObjectNonNull(paymentRequests,OrderErrorCodeEnum.ORDER_PAYMENT_IS_NULL);
        paymentRequests.forEach(paymentRequest -> {
            Integer payType = paymentRequest.getPayType();
            Integer accountType = paymentRequest.getAccountType();

            if (payType == null || PayTypeEnum.getByCode(payType) == null){
                throw  new ServiceException(OrderErrorCodeEnum.PAY_TYPE_PARAM_ERROR);
            }

            if ( accountType == null || AccountTypeEnum.getByCode(accountType) == null){
                throw  new ServiceException(OrderErrorCodeEnum.ACCOUNT_TYPE_PARAM_ERROR);
            }

        });




    }
}
