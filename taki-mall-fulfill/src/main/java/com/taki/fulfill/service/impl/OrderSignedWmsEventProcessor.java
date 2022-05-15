package com.taki.fulfill.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.enums.OrderStatusChangEnum;
import com.taki.fulfill.domain.evnet.OrderEvent;
import com.taki.fulfill.domain.evnet.OrderSignedWmsEvent;
import com.taki.fulfill.domain.request.TriggerOrderWmsShipEventRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName OrderSignedWmsEventProcessor
 * @Description
 * @Author Long
 * @Date 2022/5/15 15:52
 * @Version 1.0
 */
@Service
@Slf4j
public class OrderSignedWmsEventProcessor extends AbstractWmsShipEventProcessor{
    @Override
    protected String buildMsgBody(TriggerOrderWmsShipEventRequest request) {

        String  orderId = request.getOrderId();

        // 订单已签收事件
        OrderSignedWmsEvent orderSignedWmsEvent = (OrderSignedWmsEvent) request.getWmsShipEvent();

        orderSignedWmsEvent.setOrderId(orderId);

        // 构造订单已签收消息体
        OrderEvent<OrderSignedWmsEvent> orderEvent = buildOrderEvent(orderId, OrderStatusChangEnum.ORDER_SIGNED,orderSignedWmsEvent,OrderSignedWmsEvent.class);


        return JSONObject.toJSONString(orderEvent);
    }

    @Override
    protected void doBizProcess(TriggerOrderWmsShipEventRequest request) {

        log.info("准备发送【订单已签收事件】");
    }
}
