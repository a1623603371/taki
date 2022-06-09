package com.taki.order.mq.consumer.listener;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.enums.OrderStatusEnum;
import com.taki.common.message.PayOrderTimeOutDelayMessage;
import com.taki.common.mq.AbstractMessageListenerConcurrently;
import com.taki.order.dao.OrderInfoDao;
import com.taki.order.domain.entity.OrderInfoDO;
import com.taki.order.domain.request.CancelOrderRequest;
import com.taki.order.service.OrderAfterSaleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName PayOrderTimeoutListener
 * @Description 监听 支付订单超时延时消息
 * @Author Long
 * @Date 2022/4/6 17:06
 * @Version 1.0
 */
@Component
@Slf4j
public class PayOrderTimeoutListener  extends AbstractMessageListenerConcurrently {

    @Autowired
    private OrderAfterSaleService orderAfterSaleService;

    @Autowired
    private OrderInfoDao orderInfoDao;


    @Override
    protected ConsumeConcurrentlyStatus omMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try {
            for (MessageExt messageExt : msgs) {

                String message = new String(messageExt.getBody());
                PayOrderTimeOutDelayMessage payOrderTimeOutDelayMessage = JSONObject.parseObject(message,PayOrderTimeOutDelayMessage.class);

                // 消费延时消息，执行 取消订单逻辑
                CancelOrderRequest cancelOrderRequest = new CancelOrderRequest();

                cancelOrderRequest.setOrderId(payOrderTimeOutDelayMessage.getOrderId());
                cancelOrderRequest.setBusinessIdentifier(payOrderTimeOutDelayMessage.getBusinessIdentifier());
                cancelOrderRequest.setOrderType(payOrderTimeOutDelayMessage.getOrderType());
                cancelOrderRequest.setCancelType(payOrderTimeOutDelayMessage.getCancelType());
                cancelOrderRequest.setOrderStatus(payOrderTimeOutDelayMessage.getOrderStatus());
                cancelOrderRequest.setUserId(payOrderTimeOutDelayMessage.getUserId());

                //查询当前数据库的订单实时状态
                OrderInfoDO orderInfoDO = orderInfoDao.getByOrderId(payOrderTimeOutDelayMessage.getOrderId());
                Integer orderStatusDatabase = orderInfoDO.getOrderStatus();

                if (!OrderStatusEnum.CREATED.getCode().equals(orderStatusDatabase)){
                    // 订单实时状态不等于已创建
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }

                // 当时间 小于 订单实际 支付截止时间

                if (LocalDateTime.now().compareTo(orderInfoDO.getExpireTime()) > 0){
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }

                orderAfterSaleService.cancelOrder(cancelOrderRequest);
                log.info("关闭 订单，orderId:{}",cancelOrderRequest.getOrderId());

                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;



            }
        }catch (Exception e){
            log.error("consumer  error",e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }

        return null;
    }
}
