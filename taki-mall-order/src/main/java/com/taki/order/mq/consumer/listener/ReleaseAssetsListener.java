package com.taki.order.mq.consumer.listener;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.constants.RocketMQConstant;
import com.taki.common.enums.OrderStatusEnum;
import com.taki.common.mq.AbstractMessageListenerConcurrently;
import com.taki.inventory.domain.request.ReleaseProductStockRequest;
import com.taki.market.request.CancelOrderReleaseUserCouponRequest;
import com.taki.market.request.ReleaseUserCouponRequest;
import com.taki.order.converter.OrderConverter;
import com.taki.order.dao.OrderItemDao;
import com.taki.order.domain.dto.OrderInfoDTO;
import com.taki.order.domain.entity.OrderInfoDO;
import com.taki.order.domain.entity.OrderItemDO;
import com.taki.order.domain.request.CancelOrderAssembleRequest;
import com.taki.order.domain.request.CancelOrderRequest;
import com.taki.order.mq.producer.DefaultProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName ReleaseAssetsListener
 * @Description 监听释放资产
 * @Author Long
 * @Date 2022/4/6 17:21
 * @Version 1.0
 */
@Component
@Slf4j
public class ReleaseAssetsListener extends AbstractMessageListenerConcurrently {

    @Autowired
    private DefaultProducer defaultProducer;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private OrderConverter orderConverter;


    @Override
    protected ConsumeConcurrentlyStatus omMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try {
            for (MessageExt messageExt : msgs) {
                String message = new String(messageExt.getBody());
                log.info("ReleaseAssetsConsumer message:{}",message);

                CancelOrderAssembleRequest cancelOrderAssembleRequest = JSONObject.parseObject(message,CancelOrderAssembleRequest.class);

                OrderInfoDTO orderInfo = cancelOrderAssembleRequest.getOrderInfo();

                if(orderInfo.getOrderStatus() > OrderStatusEnum.CREATED.getCode()){
                    // 发送取消 订单退款 请求MQ

                    defaultProducer.sendMessage(RocketMQConstant.CANCEL_REFUND_REQUEST_TOPIC,
                            JSONObject.toJSONString(cancelOrderAssembleRequest),"取消订单退款",null,orderInfo.getOrderId());

                }
                //3. 发送释放库存 请求MQ
                ReleaseProductStockRequest releaseProductStockRequest = buildReleaseProductStock(orderInfo,orderItemDao);
                defaultProducer.sendMessage(RocketMQConstant.CANCEL_RELEASE_INVENTORY_TOPIC,
                        JSONObject.toJSONString(releaseProductStockRequest),"取消订单释放库存",null,orderInfo.getOrderId());

                // 4.发送 释放优惠券 MQ
                if (StringUtils.isNotBlank(orderInfo.getCouponId())) {
                    ReleaseUserCouponRequest releaseUserCouponRequest = buildReleaseUserCoupon(orderInfo);
                    defaultProducer.sendMessage(RocketMQConstant.CANCEL_RELEASE_PROPERTY_TOPIC, JSONObject.toJSONString(releaseUserCouponRequest), "取消订单释放优惠券",null,orderInfo.getOrderId());
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

        }catch (Exception e){

            log.error("consumer error",e);

            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }

    }

    /**
     * @description: 构造 取消订单 释放库存 请求
     * @param orderInfo 订单 信息
     * @param orderItemDao 订单条目 DAO 条目
     * @return 取消订单 释放库存 请求
     * @author Long
     * @date: 2022/4/6 17:32
     */
    private ReleaseProductStockRequest buildReleaseProductStock(OrderInfoDTO orderInfo, OrderItemDao orderItemDao) {
        String orderId = orderInfo.getOrderId();

        List<ReleaseProductStockRequest.OrderItemRequest> orderItemRequests = new ArrayList<>();

        //查询订单条目
        ReleaseProductStockRequest.OrderItemRequest orderItemRequest;

        List<OrderItemDO> orderItems = orderItemDao.listByOrderId(orderId);

        for (OrderItemDO orderItem : orderItems) {
        orderItemRequest = new ReleaseProductStockRequest.OrderItemRequest();
        orderItemRequest.setSkuCode(orderItem.getSkuCode());
        orderItemRequest.setSaleQuantity(orderItem.getSaleQuantity());
        orderItemRequests.add(orderItemRequest);
        }

        ReleaseProductStockRequest releaseProductStockRequest = new ReleaseProductStockRequest();
        releaseProductStockRequest.setOrderId(orderId);
        releaseProductStockRequest.setOrderItemRequests(orderItemRequests);
        return releaseProductStockRequest;

    }

    /***
     * @description: 组装释放优惠券数据
     * @param orderInfo 订单数据
     * @return
     * @author Long
     * @date: 2022/9/16 21:25
     */
    private ReleaseUserCouponRequest buildReleaseUserCoupon(OrderInfoDTO orderInfo) {
        ReleaseUserCouponRequest request = new ReleaseUserCouponRequest();
        request.setCouponId(orderInfo.getCouponId());
        request.setUserId(orderInfo.getUserId());
        request.setOrderId(orderInfo.getOrderId());
        return request;
    }



}
