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
import com.taki.common.utlis.JsonUtil;
import com.taki.common.utlis.ObjectUtil;
import com.taki.common.utlis.ParamCheckUtil;
import com.taki.common.utlis.ResponseData;
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
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private OrderOperateLogDao orderOperateLogDao;


    @Autowired
    private OrderPaymentDetailDao orderPaymentDetailDao;

    @Autowired
    private OrderNoManager orderNoManager;


    @Autowired
    private OrderManager orderManager;




    @Autowired
    private DefaultProducer defaultProducer;


    @Autowired
    private RedisLock redisLock;


    /**
     * 库存服务
     */
    @DubboReference(version ="1.0.0" ,retries = 0)
    private InventoryApi inventoryApi;

    /**
     * 营销服务
     */
    @DubboReference(version = "1.0.0" ,retries = 0)
    private MarketApi marketApi;

    @DubboReference
    private RiskApi riskApi;


    @DubboReference
    private ProductApi productApi;

    @DubboReference
    private AddressApi addressApi;

    @DubboReference
    private PayApi payApi;


    @Override
    public GenOrderIdDTO getGenOrderIdDTO(GenOrderIdRequest genOrderIdRequest) {

        String userId = genOrderIdRequest.getUserId();
        ParamCheckUtil.checkStringNonEmpty(userId);

        Integer businessIdentifier = genOrderIdRequest.getBusinessIdentifier();
        ParamCheckUtil.checkObjectNonNull(businessIdentifier);
        String orderId = orderNoManager.genOrderId(OrderAutoTypeEnum.SALE_ORDER.getCode(),userId);
        GenOrderIdDTO genOrderId = new GenOrderIdDTO();
        genOrderId.setOrderId(orderId);
        log.info("生单id:{}",orderId);
        return genOrderId;
    }

    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public CreateOrderDTO createOrder(CreateOrderRequest createOrderRequest) {

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


        //addNewOrder(createOrderRequest,productSkus,calculateOrderAmount);

        // 7.发送延时订单消息用于支付超时自动关单
        sendPayOrderTimeoutDelayMessage(createOrderRequest);
        // 返回订单数据
        CreateOrderDTO createOrderDTO = new CreateOrderDTO();
        createOrderDTO.setOrderId(createOrderDTO.getOrderId());
        return createOrderDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PrePayOrderDTO preOrder(PrePayOrderRequest prePayOrderRequest) {
        // 入参检查
        checkPerPayOrderRequestPram(prePayOrderRequest);

        String orderId = prePayOrderRequest.getOrderId();

        BigDecimal payAmount = prePayOrderRequest.getPayAmount();

        //  加分布式锁（与订单支付回调加的是同一把锁）

        String lockKey = RedisLockKeyConstants.ORDER_PAY_KEY + orderId;

        boolean lock = redisLock.lock(lockKey);

        if (!lock){
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_PRE_PAY_ERROR);
        }
        try {
            // 预支付订单检查
            checkPerPayOrderInfo(orderId,payAmount);

            //调用 支付系统 进行预支付
            PayOrderRequest payOrderRequest =  prePayOrderRequest.clone(PayOrderRequest.class);

            ResponseData<PayOrderDTO> responseResult = payApi.payOrder(payOrderRequest);

            if (!responseResult.getSuccess()){
                throw new OrderBizException(OrderErrorCodeEnum.ORDER_PRE_PAY_ERROR);
            }
            PayOrderDTO payOrder = responseResult.getData();

            //更新订单表 与支付信息表
            updateOrderPaymentInfo(payOrder);

            return payOrder.clone(PrePayOrderDTO.class);
        }finally {
            redisLock.unLock(lockKey);
        }

    }

    @Override
    public void payCallback(PayCallbackRequest payCallbackRequest) {
        // 入参检查
        checkPayCallbackRequestParam(payCallbackRequest);

        String orderId = payCallbackRequest.getOrderId();

        BigDecimal payAmount = payCallbackRequest.getPayAmount();

        Integer payType = payCallbackRequest.getPayType();

        List<String> redisKeyList = Lists.newArrayList();


        String orderPayKey = RedisLockKeyConstants.ORDER_PAY_KEY + orderId;

        String cancelOrderPayKey = RedisLockKeyConstants.CANCEL_KEY + orderId;

        redisKeyList.add(orderPayKey);
        redisKeyList.add(cancelOrderPayKey);



        boolean lock = redisLock.multiLock(redisKeyList);

        if (!lock){
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_PAY_CALLBACK_ERROR);
        }

        try {
        // 从数据库中查询出订单信息
        OrderInfoDO orderInfo = orderInfoDao.getByOrderId(orderId);
        OrderPaymentDetailDO orderPaymentDetail = orderPaymentDetailDao.getPaymentDetailByOrderId(orderId);

        // 检验参数
        if(ObjectUtils.isEmpty(orderInfo) || ObjectUtils.isEmpty(orderPaymentDetail)){
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_INFO_IS_NULL);
        }

        if (payAmount.compareTo(orderInfo.getPayAmount()) == 0){
            throw new ServiceException(OrderErrorCodeEnum.ORDER_PAY_AMOUNT_ERROR);
        }

        // 异常场景
        Integer orderStatus = orderInfo.getOrderStatus();

        if (OrderStatusEnum.CREATED.getCode().equals(orderStatus)){
            //如果订单状态是 ”已创建“ 直接更新订单状态为已支付，并发送事务消息

            TransactionMQProducer transactionMQProducer = defaultProducer.getProducer();

            transactionMQProducer.setTransactionListener(new TransactionListener() {
                @Override
                public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                    try {
                    orderManager.updateOrderStatusPaid(payCallbackRequest,orderInfo,orderPaymentDetail);

                    return LocalTransactionState.COMMIT_MESSAGE;
                    }catch (OrderBizException e){
                        throw e;
                    }catch (Exception e){

                        log.error("system error:{}",e);

                        return LocalTransactionState.ROLLBACK_MESSAGE;
                    }

                }

                @Override
                public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                        // 检查

                    return null;
                }
            });

            // 发送 “订单已完成支付” 消息
            sendPaidOrderSuccessMessage(orderInfo);
        }else {
            // 如果订单状态 不是 “已创建”
            if (OrderStatusEnum.CANCELED.getCode().equals(orderStatus)) {
                //如果订单是取消状态
                Integer payStatus = orderPaymentDetail.getPayStatus();

                if (PayStatusEnum.UNPAID.getCode().equals(payStatus)) {
                    // 调用退款
                    executeOrderRefund(orderInfo, orderPaymentDetail);

                    throw new OrderBizException(OrderErrorCodeEnum.ORDER_CANCEL_PAY_CALLBACK_ERROR);
                } else if (PayStatusEnum.PAID.getCode().equals(payStatus)) {
                    if (payType.equals(orderPaymentDetail.getPayType())) {
                        throw new OrderBizException(OrderErrorCodeEnum.ORDER_CANCEL_PAY_CALLBACK_PAY_TYPE_NO_SAME_ERROR);
                    } else {
                        throw new OrderBizException(OrderErrorCodeEnum.ORDER_CANCEL_PAY_CALLBACK_PAY_TYPE_NO_SAME_ERROR);
                    }
                }else {
                    // 如果订单状态不是取消状态
                    if (PayStatusEnum.PAID.getCode().equals(orderPaymentDetail.getPayStatus())){
                        if (payType.equals(orderPaymentDetail.getPayType())){
                            return;
                        }
                        // 调用退款
                        executeOrderRefund(orderInfo,orderPaymentDetail);
                        throw new OrderBizException(OrderErrorCodeEnum.ORDER_CANCEL_PAY_CALLBACK_REPEAT_ERROR);
                    }
                }
            }
        }

        }finally {
            redisLock.unMultiLock(redisKeyList);
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

        return null;
    }
    
    /** 
     * @description:
     * @param orderInfoDO
     * @return  boolean
     * @author Long
     * @date: 2022/2/26 19:33
     */ 
    private boolean canRemove(OrderInfoDO orderInfoDO) {
        return OrderStatusEnum.canRemoveStatus().contains(orderInfoDO.getOrderStatus()) &&  DeleteStatusEnum.NO.getCode().equals(orderInfoDO.getDeleteStatus());
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
        payApi.executeRefund(payRefundRequest);
    }

    /**
     * @description:  发送订单已支付消息，触发订单进行履约
     * @param orderInfo 订单
     * @return  void
     * @author Long
     * @date: 2022/1/17 17:13
     */
    private void sendPaidOrderSuccessMessage(OrderInfoDO orderInfo) {
        String orderId = orderInfo.getOrderId();
        PaidOrderSuccessMessage paidOrderSuccessMessage = new PaidOrderSuccessMessage();

        paidOrderSuccessMessage.setOrderId(orderId);

        String msgJson = JSON.toJSONString(paidOrderSuccessMessage);
        defaultProducer.sendMessage(RocketMQConstant.PAID_ORDER_SUCCESS_TOPIC,msgJson,"订单已完成支付");
    }



    /**
     * @description: 检查支付回调请求参数
     * @param payCallbackRequest 支付回调请求
     * @return  void
     * @author Long
     * @date: 2022/1/17 16:15
     */
    private void checkPayCallbackRequestParam(PayCallbackRequest payCallbackRequest) {
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
        LocalDateTime now = LocalDateTime.now();

        // 订单支付信息
        OrderInfoDO orderInfoDO = orderInfoDao.getByOrderId(orderId);
        orderInfoDO.setPayTime(now);
        orderInfoDO.setPayType(payType);
        orderInfoDao.updateById(orderInfoDO);

        // 支付明细
        OrderPaymentDetailDO orderPaymentDetailDO = orderPaymentDetailDao.getPaymentDetailByOrderId(orderId);
        orderPaymentDetailDO.setPayTime(now);
        orderPaymentDetailDO.setPayType(payType);
        orderPaymentDetailDO.setOutTradeNo(outTradeNo);
        orderPaymentDetailDao.updateById(orderPaymentDetailDO);

        // 判断 是否存在 子订单
        List<OrderInfoDO> subOrderList = orderInfoDao.listByParentOrderId(orderId);
        if (subOrderList != null && !subOrderList.isEmpty()) {
            subOrderList.forEach(subOrder -> {
                subOrder.setPayTime(now);
                subOrder.setPayType(payType);
                orderInfoDao.updateById(subOrder);

            OrderPaymentDetailDO  subOrderPaymentDetail = orderPaymentDetailDao.getPaymentDetailByOrderId(subOrder.getOrderId());
                subOrderPaymentDetail.setPayTime(now);
                subOrderPaymentDetail.setPayType(payType);
                subOrderPaymentDetail.setOutTradeNo(outTradeNo);
                orderPaymentDetailDao.updateById(subOrderPaymentDetail);

            });
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

        PayOrderTimeOutDelayMessage payOrderTimeOutDelayMessage = PayOrderTimeOutDelayMessage
                .builder()
                .orderId(createOrderRequest.getOrderId())
                .businessIdentifier(createOrderRequest.getBusinessIdentifier())
                .cancelType(OrderCancelTypeEnum.TIMEOUT_CANCELED.getCode())
                .orderType(createOrderRequest.getOrderType())
                .orderStatus(OrderStatusEnum.CREATED.getCode())
                .userId(createOrderRequest.getUserId()).build();


        String msgJson = JsonUtil.object2Json(payOrderTimeOutDelayMessage);

        defaultProducer.sendMessage(RocketMQConstant.PAY_ORDER_TIMEOUT_DELAY_TOPIC,msgJson, RocketDelayedLevel.DELAYED_30M,"支付订单超时延时消息");

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
     * @description: 锁定商品库存
     * @param createOrderRequest  生单请求
     * @return  void
     * @author Long
     * @date: 2022/1/6 10:12
     */ 
//    private void lockProductStock(CreateOrderRequest createOrderRequest) {
//
//    String orderId = createOrderRequest.getOrderId();
//
//    List<LockProductStockRequest.OrderItemRequest> orderItemRequests = ObjectUtil.convertList(createOrderRequest.getOrderItemRequests(),LockProductStockRequest.OrderItemRequest.class);
//
//    LockProductStockRequest lockProductStockRequest = createOrderRequest.clone(LockProductStockRequest.class);
//
//    lockProductStockRequest.setOrderItemRequests(orderItemRequests);
//
//    ResponseData<Boolean> responseResult = inventoryApi.lockProductStock(lockProductStockRequest);
//
//    if (!responseResult.getSuccess()){
//        log.error("锁定商品仓库失败,订单号：{}",orderId);
//
//        throw  new OrderBizException(responseResult.getCode(),responseResult.getMessage());
//    }
//
//
//    }


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
        ResponseData<CalculateOrderAmountDTO> responseResult = marketApi.calculateOrderAmount(calculateOrderAmountRequest);

        if (responseResult.getSuccess()){
            log.error("调用营销服务计算价格失败错误信息：{}",responseResult.getMessage());
            throw new OrderBizException(responseResult.getCode(),responseResult.getMessage());
        }
        CalculateOrderAmountDTO calculateOrderAmount = responseResult.getData();

        if (!ObjectUtils.isNotEmpty(calculateOrderAmount)){
            log.error("计算订单价格失败");
            throw new OrderBizException(OrderErrorCodeEnum.CALCULATE_ORDER_AMOUNT_ERROR);
        }
        // 订单费用
        List<OrderAmountDTO> orderAmountDTOList = ObjectUtil.convertList(calculateOrderAmount.getOrderAmountDTOList(),OrderAmountDTO.class);

        if (orderAmountDTOList == null || orderAmountDTOList.isEmpty()){
            log.error("计算订单价格的订单费用信息为空");
            throw  new OrderBizException(OrderErrorCodeEnum.CALCULATE_ORDER_AMOUNT_ERROR);
        }

        List<OrderAmountDetailDTO> orderAmountDetailList = ObjectUtil.convertList(calculateOrderAmount.getOrderAmountDetailDTOList(),OrderAmountDetailDTO.class);

        if (orderAmountDetailList == null || orderAmountDetailList.isEmpty()){
            log.error("计算订单价格的订单费用详细信息为空");
            throw  new OrderBizException(OrderErrorCodeEnum.CALCULATE_ORDER_AMOUNT_ERROR);
        }

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

        List<CreateOrderRequest.OrderItemRequest> itemRequests = createOrderRequest.getOrderItemRequests();

        List<ProductSkuDTO> productSkus = new ArrayList<>();

        itemRequests.forEach(orderItemRequest -> {
            ProductSkuQuery query = new ProductSkuQuery();
            query.setSkuCode(orderItemRequest.getSkuCode());
            query.setSellerId(query.getSellerId());
            ResponseData<ProductSkuDTO> responseResult = productApi.getProductSku(query);
            if (!responseResult.getSuccess()){
                log.error("调用商品RPC失败错误信息:{}",responseResult.getMessage());
                throw new OrderBizException(responseResult.getCode(),responseResult.getMessage());
            }
            ProductSkuDTO productSku = responseResult.getData();

            if (!ObjectUtils.isNotEmpty(productSku)){
                log.error("商品不存在SKU:{}",orderItemRequest.getSkuCode());
                throw new OrderBizException(OrderErrorCodeEnum.PRODUCT_SKU_CODE_ERROR, orderItemRequest.getSkuCode());
            }

            productSkus.add(productSku);
        });

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

        ResponseData<CheckOrderRiskDTO> responseResult = riskApi.checkOrderRisk(checkOrderRiskRequest);
        if (!responseResult.getSuccess()){
            log.error("风控服务调用失败异常信息:{}",responseResult.getMessage());
            throw new OrderBizException(responseResult.getCode(),responseResult.getMessage());
        }

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
