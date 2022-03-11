package com.taki.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.constants.RocketMQConstant;
import com.taki.common.core.CloneDirection;
import com.taki.common.enums.AfterSaleTypeDetailEnum;
import com.taki.common.enums.AfterSaleTypeEnum;
import com.taki.common.enums.OrderStatusEnum;
import com.taki.common.message.ActualRefundMessage;
import com.taki.common.utlis.ObjectUtil;
import com.taki.common.utlis.ParamCheckUtil;
import com.taki.order.dao.*;
import com.taki.order.domain.dto.*;
import com.taki.order.domain.entity.*;
import com.taki.order.domain.request.LackRequest;
import com.taki.order.enums.*;
import com.taki.order.exception.OrderBizException;
import com.taki.order.exception.OrderErrorCodeEnum;
import com.taki.order.manager.OrderNoManager;
import com.taki.order.mq.producer.DefaultProducer;
import com.taki.order.service.OrderLackService;
import com.taki.order.service.amount.AfterSaleAmountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private AfterSaleItemDao afterSaleItemDao;

    @Autowired
    private AfterSaleAmountService afterSaleAmountService;

    @Autowired
    private AfterSaleRefundDAO afterSaleRefundDAO;


    @Autowired
    private DefaultProducer defaultProducer;

    @Override
    public CheckLackDTO checkRequest(LackRequest lackRequest) {
        //1.参数基本校应
        ParamCheckUtil.checkStringNonEmpty(lackRequest.getOrderId(), OrderErrorCodeEnum.ORDER_ID_IS_NULL);
        ParamCheckUtil.checkCollectionNonEmpty(lackRequest.getLackItems(),OrderErrorCodeEnum.LACK_ITEM_IS_NULL);

        //2.查询订单
        OrderInfoDO order = orderInfoDao.getByOrderId(lackRequest.getOrderId());
        ParamCheckUtil.checkObjectNonNull(order,OrderErrorCodeEnum.ORDER_NOT_FOUND);

        //3.校验订单是否可以发起却品
        if (!OrderStatusEnum.canLack().contains(order.getOrderStatus())){
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_NOT_ALLOW_TO_ADJUST_ADDRESS);
        }

        // 4.查询订单 item
        List<OrderItemDO> orderItems  = orderItemDao.listByOrderId(lackRequest.getOrderId());

        List<LackItemDTO> lackItems = ObjectUtil.convertList(orderItems,LackItemDTO.class, CloneDirection.OPPOSITE);
        return new CheckLackDTO(order,lackItems);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LackDTO executeLackRequest(LackRequest lackRequest, CheckLackDTO checkLack) {
        OrderInfoDO orderInfo = checkLack.getOrderInfo();
        List<LackItemDTO> lackItems = checkLack.getOrderLackItems();

        //1.生成缺品售后单
        AfterSaleInfoDO afterSaleInfo = buildLackAfterAfterSaleInfo(orderInfo);

        //2.生成缺品售后单item
        List<AfterSaleItemDO> afterSaleItems = new ArrayList<>();

        lackItems.forEach(item->{
            afterSaleItems.add(buildLackAfterSaleItem(orderInfo,afterSaleInfo,item));
        });

        //3. 计算订单缺品退款金额
        BigDecimal lackApplyRefundAmount = afterSaleAmountService.calculateOrderLackApplyRefundAmount(afterSaleItems);
        BigDecimal lackRealRefundAmount = afterSaleAmountService.calculateOrderLackRealRefundAmount(afterSaleItems);
        afterSaleInfo.setApplyRefundAmount(lackApplyRefundAmount);
        afterSaleInfo.setRealRefundAmount(lackRealRefundAmount);

        // 4.构造售后退款单
        AfterSaleRefundDO afterSaleRefund = buildLackAfterSaleRefundDO(orderInfo,afterSaleInfo);

        //5.构造订单缺品扩展信息
        OrderExtJsonDTO lackExJson = buildOrderLackExJson(lackRequest,orderInfo,afterSaleInfo);


        // 6.存储售后单，item和退款单
        afterSaleInfoDao.save(afterSaleInfo);
        afterSaleItemDao.saveBatch(afterSaleItems);
        afterSaleRefundDAO.save(afterSaleRefund);
        orderInfoDao.updateOrderExJson(orderInfo.getOrderId(),lackExJson);

        //7.发送缺品退款信息
        sendLackRefund(orderInfo,afterSaleInfo,afterSaleRefund.getId());
        return  new LackDTO(orderInfo.getOrderId(),afterSaleInfo.getAfterSaleId());
    }
    /** 
     * @description: 发送却品退款请求
     * @param orderInfo 订单
     * @param afterSaleInfo 售后信息
     * @param afterSaleRefundId 售后退款Id
     * @return  void
     * @author Long
     * @date: 2022/3/11 17:49
     */ 
    private void sendLackRefund(OrderInfoDO orderInfo, AfterSaleInfoDO afterSaleInfo, Long afterSaleRefundId) {
        ActualRefundMessage actualRefundMessage = new ActualRefundMessage();
        actualRefundMessage.setOrderId(orderInfo.getOrderId());
        actualRefundMessage.setAfterSaleRefundId(afterSaleRefundId);
        actualRefundMessage.setAfterSaleId(afterSaleInfo.getAfterSaleId());
        defaultProducer.sendMessage(RocketMQConstant.ACTUAL_REFUND_TOPIC, JSONObject.toJSONString(actualRefundMessage),"实际退款");
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
        OrderItemDTO orderItem = lackItem.getOrderItem();

        AfterSaleItemDO afterSaleItemDO = new AfterSaleItemDO();
        afterSaleItemDO.setAfterSaleId(afterSaleItemDO.getAfterSaleId());
        afterSaleItemDO.setOrderId(orderInfo.getOrderId());
        afterSaleItemDO.setProductName(orderItem.getProductName());
        afterSaleItemDO.setSkuCode(productSku.getSkuCode());
        afterSaleItemDO.setReturnQuantity(lackNum);
        afterSaleItemDO.setProductImg(productSku.getProductImg());
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
                .afterSaleId(Long.valueOf(afterSaleId))
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
