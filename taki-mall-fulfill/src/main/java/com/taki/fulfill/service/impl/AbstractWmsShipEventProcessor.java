package com.taki.fulfill.service.impl;

import com.taki.common.constants.RocketMQConstant;
import com.taki.common.enums.OrderStatusChangEnum;
import com.taki.common.mq.MQMessage;
import com.taki.fulfill.domain.evnet.OrderEvent;
import com.taki.fulfill.domain.evnet.OrderOutStockWmsEvent;
import com.taki.fulfill.domain.request.TriggerOrderWmsShipEventRequest;
import com.taki.fulfill.mq.producer.DefaultProducer;
import com.taki.fulfill.service.OrderWmsShipEventProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @ClassName AbstractWmsShipEventProcessor
 * @Description TODO
 * @Author Long
 * @Date 2022/5/15 15:05
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractWmsShipEventProcessor implements OrderWmsShipEventProcessor {

    @Autowired
    private DefaultProducer producer;

    @Override
    public void execute(TriggerOrderWmsShipEventRequest request) {
        //执行业务流程
        doBizProcess(request);
        // 构造消息体
        String body = buildMsgBody(request);

        // 发送消息
        sendMessage(body,request.getOrderId());
    }

    /** 
     * @description: 发送 消息
     * @param body 消息体
     * @param orderId 订单Id
     * @return  void
     * @author Long
     * @date: 2022/5/15 15:13
     */ 
    protected  void sendMessage(String body, String orderId){
        if(StringUtils.isNotBlank(body)){
            Message message = new MQMessage();

            message.setTopic(RocketMQConstant.ORDER_WMS_SHIP_RESULT_TOPIC);
            message.setBody(body.getBytes(StandardCharsets.UTF_8));

            try {
                DefaultMQProducer defaultMQProducer = producer.getProducer();

                SendResult sendResult = defaultMQProducer.send(message, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> list, Message message, Object arg) {
                        // 根据订单id选择queue
                        String orderId = (String) arg;
                        int index = hash(orderId) % list.size();
                        return list.get(index);
                    }
                },orderId);

            }catch (Exception e){
                log.error("send order wms ship result  message error orderId ={},err={}",orderId,e.getMessage(),e);
            }

        }
    }
    /**
     * @description:  构造订单事件
     * @param orderId 订单Id
     * @param  orderStatusChang 订单转换变化枚举
     * @param  messageContent 订单出库事件消息体
     * @param  clazz
     * @return
     * @author Long
     * @date: 2022/5/15 15:46
     */
    protected <T> OrderEvent<T> buildOrderEvent(String orderId, OrderStatusChangEnum orderStatusChang, T messageContent, Class<T> clazz) {

        OrderEvent<T> orderEvent = new OrderEvent<T>();
        orderEvent.setOrderId(orderId);
        orderEvent.setBusinessIdentifier(1);
        orderEvent.setOrderType(1);
        orderEvent.setOrderStatusChang(orderStatusChang);
        orderEvent.setMessageContent(messageContent);

        return orderEvent;


    }


    /**
     * @description: hash
     * @param orderId
     * @return  int
     * @author Long
     * @date: 2022/5/15 15:22
     */
    private int hash(String orderId){

        return orderId.hashCode() & Integer.MAX_VALUE;
    }



    /** 
     * @description:  构造消息铁
     * @param request
     * @return  java.lang.String
     * @author Long
     * @date: 2022/5/15 15:13
     */ 
    protected abstract String buildMsgBody(TriggerOrderWmsShipEventRequest request);

    /** 
     * @description:  执行业务流程
     * @param request
     * @return  void
     * @author Long
     * @date: 2022/5/15 15:12
     */ 
    protected abstract void doBizProcess(TriggerOrderWmsShipEventRequest request);
}
