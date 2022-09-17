package com.taki.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.constants.RocketMQConstant;
import com.taki.common.enums.AfterSaleTypeDetailEnum;
import com.taki.common.enums.AfterSaleTypeEnum;
import com.taki.common.enums.OrderStatusEnum;
import com.taki.common.message.ActualRefundMessage;
import com.taki.common.mq.MQMessage;
import com.taki.common.utli.ExJsonUtil;
import com.taki.common.utli.ParamCheckUtil;
import com.taki.order.converter.OrderConverter;
import com.taki.order.dao.*;
import com.taki.order.domain.dto.*;
import com.taki.order.domain.entity.*;
import com.taki.order.domain.request.LackItemRequest;
import com.taki.order.domain.request.LackRequest;
import com.taki.order.enums.*;
import com.taki.order.exception.OrderBizException;
import com.taki.order.exception.OrderErrorCodeEnum;
import com.taki.order.manager.OrderNoManager;
import com.taki.order.mq.producer.LackItemProducer;
import com.taki.order.remote.ProductRemote;
import com.taki.order.service.OrderLackService;
import com.taki.order.service.amount.AfterSaleAmountService;
import com.taki.product.domian.dto.ProductSkuDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName OrderLackServiceImpl
 * @Description 订单缺品 service
 * @Author Long
 * @Date 2022/3/9 15:29
 * @Version 1.0
 */
@Slf4j
@Service
public class OrderLackServiceImpl implements OrderLackService {


    @Autowired
    private OrderInfoDao orderInfoDao;

    @Autowired
    private OrderItemDao orderItemDao;


    @Autowired
    private OrderNoManager orderNoManager;

    @Autowired
    private AfterSaleInfoDao afterSaleInfoDao;

    @Autowired
    private AfterSaleAmountService afterSaleAmountService;

    @Autowired
    private  OrderLackProcessor orderLackProcessor;


    @Autowired
    private LackItemProducer lackItemProducer;

    @Autowired
    private ProductRemote productRemote;

    @Autowired
    private OrderConverter orderConverter;

    @Override
    public CheckLackDTO checkRequest(LackRequest lackRequest) {


        //1.查询订单
        OrderInfoDO order = orderInfoDao.getByOrderId(lackRequest.getOrderId());
        ParamCheckUtil.checkObjectNonNull(order,OrderErrorCodeEnum.ORDER_NOT_FOUND);


        //2、校验订单是否可以发起缺品
        // 可以发起缺品的前置条件：
        // （1) 订单的状态为："已出库"
        //  (2) 订单未发起过缺品

        // 解释一下为啥是"已库存"状态才能发起缺品：
        // 缺品的业务逻辑是这样的，当订单支付后，进入履约流程，仓库人员捡货当时候发现现有商品无法满足下单所需，即"缺品"了
        // 仓库人员首先会将通知订单系统将订单的状态变为"已出库"，然后会再来调用这个缺品的接口
        if (!OrderStatusEnum.canLack().contains(order.getOrderStatus()) ||isOrderLacked(order)){
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_NOT_ALLOW_TO_LACK);
        }

        // 3.查询订单 item
        List<OrderItemDO> orderItems  = orderItemDao.listByOrderId(lackRequest.getOrderId());

        //4.检查 具体缺品
        List<LackItemDTO> lackItems = new ArrayList<>();

        lackRequest.getLackItems().forEach(lackItemRequest -> {
            lackItems.add(checkLackItem(order,orderItems,lackItemRequest));

        });
        return new CheckLackDTO(order,lackItems);
    }

    /** 
     * @description: 检查缺品
     * @param order 订单
     * @param orderItems 订单条目
     * @param lackItemRequest 缺品条目请求
     * @return  void
     * @author Long
     * @date: 2022/6/9 19:37
     */ 
    private LackItemDTO checkLackItem(OrderInfoDO order, List<OrderItemDO> orderItems, LackItemRequest lackItemRequest) {
        String skuCode = lackItemRequest.getSkuCode();
        Integer lackNum = lackItemRequest.getLackNum();

        //1参数效验
        ParamCheckUtil.checkStringNonEmpty(skuCode,OrderErrorCodeEnum.SKU_CODE_IS_NULL);
        ParamCheckUtil.checkIntMin(lackNum,1,OrderErrorCodeEnum.LACK_NUM_IS_LT_0);

        //2.查询商品sku
        ProductSkuDTO productSkuDTO = productRemote.getProductSku(skuCode,order.getSellerId());

        ParamCheckUtil.checkObjectNonNull(productSkuDTO,OrderErrorCodeEnum.PRODUCT_SKU_CODE_ERROR);

        //3. 找到item中对应的缺品 sku item
        OrderItemDO lackItemDO = orderItems.stream().filter(item -> item.getSkuCode().equals(skuCode)).findFirst().orElse(null);
        ParamCheckUtil.checkObjectNonNull(lackItemDO,OrderErrorCodeEnum.LACK_ITEM_NOT_IN_ORDER);

        //4.缺品商品 数量不能 》= 下单商品数量
        if (lackItemDO.getSaleQuantity() <= lackItemRequest.getLackNum()){
            throw new OrderBizException(OrderErrorCodeEnum.LACK_NUM_IS_GE_SKU_ORDER_ITEM_SIZE);
        }
        //5.构造 参数
        return  new LackItemDTO(lackItemDO,lackNum,productSkuDTO);


    }

    @Override
    public Boolean isOrderLacked(OrderInfoDO orderInfoDO) {
        OrderExtJsonDTO orderExtJson = ExJsonUtil.parseJson(orderInfoDO.getExtJson(),OrderExtJsonDTO.class);
        if (ObjectUtils.isNotEmpty(orderExtJson)){
            return orderExtJson.getLackFlag();
        }


        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LackDTO executeLackRequest(LackRequest lackRequest, CheckLackDTO checkLack) throws MQClientException {
        OrderInfoDO orderInfo = checkLack.getOrderInfo();
        List<LackItemDTO> lackItems = checkLack.getOrderLackItems();

        //1.生成缺品售后单
        AfterSaleInfoDO lackAfterSaleInfo = buildLackAfterAfterSaleInfo(orderInfo);

        //2.生成缺品售后单item
        List<AfterSaleItemDO> afterSaleItems = new ArrayList<>();

        lackItems.forEach(item->{
            afterSaleItems.add(buildLackAfterSaleItem(orderInfo,lackAfterSaleInfo,item));
        });

        //3. 计算订单缺品退款金额
        BigDecimal lackApplyRefundAmount = afterSaleAmountService.calculateOrderLackApplyRefundAmount(afterSaleItems);
        BigDecimal lackRealRefundAmount = afterSaleAmountService.calculateOrderLackRealRefundAmount(afterSaleItems);
        lackAfterSaleInfo.setApplyRefundAmount(lackApplyRefundAmount);
        lackAfterSaleInfo.setRealRefundAmount(lackRealRefundAmount);

        // 4.构造售后退款单
        AfterSaleRefundDO afterSaleRefund = buildLackAfterSaleRefundDO(orderInfo,lackAfterSaleInfo);

        //5.构造订单缺品扩展信息
        OrderExtJsonDTO lackExJson = buildOrderLackExJson(lackRequest,orderInfo,lackAfterSaleInfo);


        // 6.存储售后单，item和退款单

        OrderLackInfo orderLackInfo = OrderLackInfo.builder()
                .lackAfterSaleOrder(lackAfterSaleInfo)
                .afterSaleItems(afterSaleItems)
                .afterSaleRefundDO(afterSaleRefund)
                .lackExJson(lackExJson)
                .orderId(orderInfo.getOrderId())
                .build();

        TransactionMQProducer transactionMQProducer = lackItemProducer.getTransactionMQProducer();

        setLackItemTransactionListener(transactionMQProducer);

        //7.发送缺品退款信息
        sendLackRefundMessage(orderLackInfo.getOrderId(),lackAfterSaleInfo.getAfterSaleId(),orderLackInfo,transactionMQProducer );
        return  new LackDTO(orderInfo.getOrderId(),lackAfterSaleInfo.getAfterSaleId());
    }

    /** 
     * @description: 设置 缺品消息 事务 监听
     * @param transactionMQProducer
     * @return  void
     * @author Long
     * @date: 2022/6/9 22:56
     */ 
    private void setLackItemTransactionListener(TransactionMQProducer transactionMQProducer) {
        transactionMQProducer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object o) {

                try {
                    OrderLackInfo orderLackInfo = (OrderLackInfo) o;

                    //保存缺品
                    orderLackProcessor.saveLackInfo(orderLackInfo);
                    return LocalTransactionState.COMMIT_MESSAGE;
                }catch (Exception e){
                    log.error("system error",e);
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }

            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {

                //检查缺品售后单是否已创建
                String body = new String(messageExt.getBody(), StandardCharsets.UTF_8);

                ActualRefundMessage actualRefundMessage = JSONObject.parseObject(body,ActualRefundMessage.class);

                String afterSaleId = actualRefundMessage.getAfterSaleId();

                AfterSaleInfoDO afterSaleInfoDO = afterSaleInfoDao.getByAfterSaleId(afterSaleId);

                if (!ObjectUtils.isEmpty(afterSaleInfoDO)){
                    return LocalTransactionState.COMMIT_MESSAGE;
                }

                return LocalTransactionState.ROLLBACK_MESSAGE;
            }
        });
    }

    /** 
     * @description: 发送却品退款请求
     * @param orderId 订单Id
     *
     * @param afterSaleId 售后单Id
     * @param orderLackInfo 订单缺品消息
     * @param producer 缺品退款消息生产者
     * @return  void
     * @author Long
     * @date: 2022/3/11 17:49
     */ 
    private void sendLackRefundMessage(String orderId, String afterSaleId
            , OrderLackInfo orderLackInfo, TransactionMQProducer producer) throws MQClientException {

        ActualRefundMessage actualRefundMessage = new ActualRefundMessage();
        actualRefundMessage.setOrderId(orderId);
        actualRefundMessage.setAfterSaleId(afterSaleId);
        String topic = RocketMQConstant.ACTUAL_REFUND_TOPIC;
        byte[] body = JSONObject.toJSONString(actualRefundMessage).getBytes(StandardCharsets.UTF_8);
        Message message = new MQMessage(topic,null,orderId,body);
        producer.sendMessageInTransaction(message,orderLackInfo);
    }


    /**
     * @description: 构造订单缺品扩展信息
     * @param lackRequest 缺品请求
     * @param orderInfo 订单
     * @param afterSaleInfo 售后订单
     * @return
     * @author Long
     * @date: 2022/3/11 15:12
     */
    private OrderExtJsonDTO buildOrderLackExJson(LackRequest lackRequest, OrderInfoDO orderInfo, AfterSaleInfoDO afterSaleInfo) {
        OrderExtJsonDTO orderExtJson = new OrderExtJsonDTO();
        orderExtJson.setLackFlag(true);
        OrderLackInfoDTO lackInfo  =  new OrderLackInfoDTO();
        lackInfo.setLackItems(lackRequest.getLackItems());
        lackInfo.setOrderId(orderInfo.getOrderId());
        lackInfo.setApplyRefundAmount(afterSaleInfo.getApplyRefundAmount());
        lackInfo.setRealRefundAmount(afterSaleInfo.getRealRefundAmount());
        orderExtJson.setLackInfoDTO(lackInfo);
        return orderExtJson;

    }

    /** 
     * @description: 构造缺品售后支付订单
     * @param orderInfo 订单
     * @param afterSaleInfo 售后订单信息
     * @return
     * @author Long
     * @date: 2022/3/11 14:58
     */ 
    private AfterSaleRefundDO buildLackAfterSaleRefundDO(OrderInfoDO orderInfo, AfterSaleInfoDO afterSaleInfo) {

        // 构造售后单
        AfterSaleRefundDO afterSaleRefund = AfterSaleRefundDO
                .builder()
                .afterSaleId(String.valueOf(afterSaleInfo.getAfterSaleId()))
                .orderId(orderInfo.getOrderId())
                .accountType(AccountTypeEnum.THIRD.getCode())
                .payType(orderInfo.getPayType())
                .refundAmount(afterSaleInfo.getRealRefundAmount())
                .refundStatus(RefundStatusEnum.UN_REFUND.getCode())
                .build();

        return afterSaleRefund;
    }

    /** 
     * @description: 构造缺品售后
     * @param orderInfo 订单
     * @param afterSaleInfo 售后订单
     * @param lackItem  缺品条目
     * @return  售后条目
     * @author Long
     * @date: 2022/3/11 11:35
     */ 
    private AfterSaleItemDO buildLackAfterSaleItem(OrderInfoDO orderInfo, AfterSaleInfoDO afterSaleInfo, LackItemDTO lackItem) {
        Integer lackNum = lackItem.getLackNum();
        ProductSkuDTO productSku = lackItem.getProductSkuDTO();
        OrderItemDO orderItem = lackItem.getOrderItem();

        AfterSaleItemDO afterSaleItemDO = new AfterSaleItemDO();
        afterSaleItemDO.setAfterSaleId(afterSaleItemDO.getAfterSaleId());
        afterSaleItemDO.setOrderId(orderInfo.getOrderId());
        afterSaleItemDO.setProductName(orderItem.getProductName());
        afterSaleItemDO.setSkuCode(productSku.getSkuCode());
        afterSaleItemDO.setReturnQuantity(lackNum);
        afterSaleItemDO.setProductImg(productSku.getProductImage());
        afterSaleItemDO.setOriginAmount(orderItem.getOriginAmount());

        // 计算sku缺品退款金额
        afterSaleItemDO.setApplyRefundAmount(orderItem.getSalePrice().multiply(new BigDecimal(lackNum
        )) );
        afterSaleItemDO.setRealRefundAmount(afterSaleAmountService.calculateOrderItemLackRealRefundAmount(orderItem,lackNum));

        return afterSaleItemDO;
    }

    /** 
     * @description:  构造售后单
     * @param orderInfo 订单
     * @return 售后单
     * @author Long
     * @date: 2022/3/10 17:48
     */ 
    private AfterSaleInfoDO buildLackAfterAfterSaleInfo(OrderInfoDO orderInfo) {

        // 构造售后单
        String userId = orderInfo.getUserId();
        String afterSaleId = orderNoManager.genOrderId(OrderNoTypeEnum.AFTER_SALE.getCode(),userId);

        AfterSaleInfoDO afterSaleInfoDO = AfterSaleInfoDO.builder()
                .afterSaleId(afterSaleId)
                .userId(userId)
                .orderSourceChannel(BusinessIdentifierEnum.SELF_MALL.getCode())
                .orderType(OrderTypeEnum.NORMAL.getCode())
                .afterSaleType(AfterSaleTypeEnum.RETURN_MONEY.getCode())
                .afterSaleTypeDetail(AfterSaleTypeDetailEnum.LACK_REFUND.getCode())
                .applySource(AfterSaleApplySourceEnum.SYSTEM.getCode())
                .afterSaleStatus(AfterSaleStatusEnum.REVIEW_PASS.getCode())
                .applyTime(LocalDateTime.now())
                .reviewTime(LocalDateTime.now())
                .build();
        return afterSaleInfoDO;
    }
}
