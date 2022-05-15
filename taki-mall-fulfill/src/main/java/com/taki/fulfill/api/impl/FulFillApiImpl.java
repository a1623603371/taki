package com.taki.fulfill.api.impl;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.bean.SpringApplicationContext;
import com.taki.common.constants.RocketMQConstant;
import com.taki.common.enums.OrderStatusChangEnum;
import com.taki.common.utlis.ResponseData;
import com.taki.fulfill.api.FulFillApi;
import com.taki.fulfill.domain.evnet.*;
import com.taki.fulfill.domain.request.CancelFulfillRequest;
import com.taki.fulfill.domain.request.ReceiveFulFillRequest;
import com.taki.fulfill.domain.request.TriggerOrderWmsShipEventRequest;
import com.taki.fulfill.exection.FulfillBizException;
import com.taki.fulfill.mq.producer.DefaultProducer;
import com.taki.fulfill.service.FulfillService;
import com.taki.fulfill.service.OrderWmsShipEventProcessor;
import com.taki.fulfill.service.impl.OrderDeliveredWmsEventProcessor;
import com.taki.fulfill.service.impl.OrderOutStockWmsEventProcessor;
import com.taki.fulfill.service.impl.OrderSignedWmsEventProcessor;
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



    @Autowired
    SpringApplicationContext springApplicationContext;

    @Autowired
    private FulfillService fulfillService;


    @Override
    public ResponseData<Boolean> receiveOrderFulFill(ReceiveFulFillRequest request) {
        log.info("接受到订单履约请求，request={}", JSONObject.toJSONString(request));

        try {
        Boolean result = fulfillService.receiveOrderFulFill(request);

        return ResponseData.success(result);
        }catch (FulfillBizException e){
            log.error("biz error",e);
        }catch (Exception e){
            log.error("system error",e);
        }

        return ResponseData.success(true);
    }

    @Override
    public ResponseData<Boolean> triggerOrderWmsShipEvent(TriggerOrderWmsShipEventRequest request) {
        log.info("触发订单物流配送结果事件，order={},orderStatusChange={},wmsEvent",request.getOrderId(),request.getOrderStatusChang(),JSONObject.toJSONString(request.getWmsShipEvent()));


    // 1获取处理器
    OrderStatusChangEnum orderStatusChang = request.getOrderStatusChang();

    OrderWmsShipEventProcessor processor = getWmsShipEventProcessor(orderStatusChang);

    // 2.执行
    if(ObjectUtils.isNotEmpty(processor)){

        processor.execute(request);
    }
        return ResponseData.success(true);
    }
    
    /** 
     * @description:  订单物流 配送结果处理器
     * @param orderStatusChang
     * @return  com.taki.fulfill.service.OrderWmsShipEventProcessor
     * @author Long
     * @date: 2022/5/15 18:13
     */ 
    private OrderWmsShipEventProcessor getWmsShipEventProcessor(OrderStatusChangEnum orderStatusChang) {

        if (OrderStatusChangEnum.ORDER_OUT_STOCKED.equals(orderStatusChang)){
            // 订单已出库事件
            return  springApplicationContext.getBean(OrderOutStockWmsEventProcessor.class);
        }else if (OrderStatusChangEnum.ORDER_DELIVERED.equals(orderStatusChang)){
            // 订单已配送

            return springApplicationContext.getBean(OrderDeliveredWmsEventProcessor.class);
        }else if (OrderStatusChangEnum.ORDER_SIGNED.equals(orderStatusChang)){

            return springApplicationContext.getBean(OrderSignedWmsEventProcessor.class);

        }

        return null;


    }


    @Override
    public ResponseData<Boolean> cancelFulfill(CancelFulfillRequest cancelFulfillRequest) {
        log.info("取消履约：request={}",JSONObject.toJSONString(cancelFulfillRequest));
        // 发送取消履约消息
        defaultProducer.sendMessage(RocketMQConstant.CANCEL_FULFILL_TOPIC,JSONObject.toJSONString(cancelFulfillRequest),"取消履约");

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


