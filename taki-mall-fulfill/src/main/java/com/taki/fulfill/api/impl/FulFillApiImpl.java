package com.taki.fulfill.api.impl;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.constants.RocketMQConstant;
import com.taki.common.enums.OrderStatusChangEnum;
import com.taki.common.utlis.ResponseData;
import com.taki.fulfill.api.FulFillApi;
import com.taki.fulfill.domain.evnet.*;
import com.taki.fulfill.domain.request.CancelFulfillRequest;
import com.taki.fulfill.domain.request.ReceiveFulFillRequest;
import com.taki.fulfill.mq.producer.DefaultProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @ClassName FulFillApiImpl
 * @Description TODO
 * @Author Long
 * @Date 2022/3/4 17:28
 * @Version 1.0
 */
@DubboService(version = "1.0.0",retries = 0)
@Slf4j
public class FulFillApiImpl implements FulFillApi {


    @Autowired
    private DefaultProducer defaultProducer;


    @Override
    public ResponseData<Boolean> receiveOrderFulFill(ReceiveFulFillRequest request) {
        log.info("接受到订单履约请求，request={}", JSONObject.toJSONString(request));
        return ResponseData.success(true);
    }

    @Override
    public ResponseData<Boolean> triggerOrderWmsShipEvent(String orderId, OrderStatusChangEnum orderStatusChang, BaseWmsShipEvent event) {
        log.info("触发订单物流配送结果事件，order={},orderStatusChange={},wmsEvent",orderId,orderStatusChang,JSONObject.toJSONString(event));
        Message message = null;
        String body = null;

        if (OrderStatusChangEnum.ORDER_OUT_STOCKED.equals(orderStatusChang)) {
            // 订单出库事件
            OrderOutStockWmsEvent outStockWmsEvent = (OrderOutStockWmsEvent) event;
            outStockWmsEvent.setOrderId(orderId);

            //构建 订单已出库消息体

            OrderEvent<OrderOutStockWmsEvent> orderEvent = buildOrderEvent(orderId,
                    OrderStatusChangEnum.ORDER_OUT_STOCKED, outStockWmsEvent, OrderOutStockWmsEvent.class);

            body = JSONObject.toJSONString(orderEvent);
        }else if (OrderStatusChangEnum.ORDER_DELIVERED.equals(orderStatusChang)){
            message = new Message();

            // 订单配送 事件
            OrderDeliveredWmsEvent deliveredWmsEvent = (OrderDeliveredWmsEvent) event;
            deliveredWmsEvent.setOrderId(orderId);

            // 构建订单已配送消息体
        OrderEvent<OrderDeliveredWmsEvent> orderEvent = buildOrderEvent(orderId,OrderStatusChangEnum.ORDER_DELIVERED,deliveredWmsEvent,OrderDeliveredWmsEvent.class);

        body = JSONObject.toJSONString(orderEvent);
        }else if (OrderStatusChangEnum.ORDER_SIGNED.equals(orderStatusChang)){
            message = new Message();
            //订单已签收
            OrderSignedWmsEvent signedWmsEvent = (OrderSignedWmsEvent) event;
            signedWmsEvent.setOrderId(orderId);

            // 构建 订单已签收消息体
            OrderEvent<OrderSignedWmsEvent> orderEvent = buildOrderEvent(orderId,OrderStatusChangEnum.ORDER_SIGNED,signedWmsEvent,OrderSignedWmsEvent.class);

            body = JSONObject.toJSONString(orderEvent);
        }
        if (ObjectUtils.isNotEmpty(message)){
            message.setTopic(RocketMQConstant.ORDER_WMS_SHIP_RESULT_TOPIC);
            message.setBody(body.getBytes(StandardCharsets.UTF_8));

            DefaultMQProducer producer = defaultProducer.getProducer();

            try {
                SendResult sendResult = producer.send(message, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> list, Message message, Object arg) {
                        // 根据订单Id 选择 发送的queue
                            String orderId = (String) arg;
                            long index = hash(orderId) % list.size();
                        return list.get((int) index);
                    }
                },orderId);
                log.info("send order wms  ship result message finished ,SendResult  status:%s ,queueId:%s,queueId:%d body:%s "
                ,sendResult.getSendStatus(),sendResult.getMessageQueue().getQueueId(),body);
            } catch (Exception e) {
             log.error("send order wms ship result message  error,orderId={},err={}",orderId,e.getMessage(),e);
            }

        }
        return ResponseData.success(true);
    }


    @Override
    public ResponseData<Boolean> cancelFulfill(CancelFulfillRequest cancelFulfillRequest) {
        log.info("告知仓库 不要配送，物流不要取货");
        return ResponseData.success(true);
    }
    /**
     * @description: 构造正向订单事件
     * @param orderId 订单Id
     * @param orderOutStocked 订单转换变化枚举
     * @return  订单事件
     * @author Long
     * @date: 2022/3/5 11:28
     */
    private <T> OrderEvent buildOrderEvent(String orderId, OrderStatusChangEnum orderOutStocked,T messageContent,Class<T> clazz) {

        OrderEvent<T> orderEvent = new OrderEvent<T>();
        orderEvent.setOrderId(orderId);
        orderEvent.setBusinessIdentifier(1);
        orderEvent.setOrderType(1);
        orderEvent.setOrderStatusChang(orderOutStocked);
        orderEvent.setMessageContent(messageContent);

        return orderEvent;

    }

    public int hash(String orderId){
        // 解决 取模 可能为负数 的请求
        return orderId.hashCode() & Integer.MAX_VALUE;
    }
}


