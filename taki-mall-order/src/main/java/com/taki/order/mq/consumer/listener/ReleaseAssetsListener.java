package com.taki.order.mq.consumer.listener;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.constants.RocketMQConstant;
import com.taki.common.enums.OrderStatusEnum;
import com.taki.common.mq.AbstractMessageListenerConcurrently;
import com.taki.inventory.domain.request.CancelOrderReleaseProductStockRequest;
import com.taki.market.request.CancelOrderReleaseUserCouponRequest;
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
                            JSONObject.toJSONString(cancelOrderAssembleRequest),"取消订单退款",null,null);

                }

                //3. 发送释放库存 请求MQ

                CancelOrderReleaseProductStockRequest cancelOrderReleaseProductStockRequest = buildSkuList(orderInfo,orderItemDao);

                defaultProducer.sendMessage(RocketMQConstant.CANCEL_RELEASE_INVENTORY_TOPIC,
                        JSONObject.toJSONString(cancelOrderReleaseProductStockRequest),"取消订单释放库存",null,null);

                // 4.发送 释放优惠券 MQ

                if (StringUtils.isNotBlank(orderInfo.getCouponId())) {
                    CancelOrderReleaseUserCouponRequest cancelOrderReleaseUserCouponRequest = orderInfo.clone(CancelOrderReleaseUserCouponRequest.class);

                    defaultProducer.sendMessage(RocketMQConstant.CANCEL_RELEASE_PROPERTY_TOPIC, JSONObject.toJSONString(cancelOrderReleaseUserCouponRequest), "取消订单释放优惠券",null,null);
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }




        }catch (Exception e){

            log.error("consumer error",e);

            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }




        return null;
    }

    /**
     * @description: 构造 取消订单 释放库存 请求
     * @param orderInfoDTO 订单 信息
     * @param orderItemDao 订单条目 DAO 条目
     * @return 取消订单 释放库存 请求
     * @author Long
     * @date: 2022/4/6 17:32
     */
    private CancelOrderReleaseProductStockRequest buildSkuList(OrderInfoDTO orderInfoDTO, OrderItemDao orderItemDao) {

        List<OrderItemDO> orderItems = orderItemDao.listByOrderId(orderInfoDTO.getOrderId());

        List skuList = orderItems.stream().map(OrderItemDO::getSkuCode).collect(Collectors.toList());

        CancelOrderReleaseProductStockRequest cancelOrderReleaseProductStockRequest = new CancelOrderReleaseProductStockRequest();

        cancelOrderReleaseProductStockRequest.setOrderId(orderInfoDTO.getOrderId());
        cancelOrderReleaseProductStockRequest.setSkuCodeList(skuList);
        cancelOrderReleaseProductStockRequest.setBusinessIdentifier(orderInfoDTO.getBusinessIdentifier());

        return cancelOrderReleaseProductStockRequest;



    }
}
