package com.taki.fulfill.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.enums.OrderStatusChangEnum;
import com.taki.fulfill.domain.evnet.OrderEvent;
import com.taki.fulfill.domain.evnet.OrderOutStockWmsEvent;
import com.taki.fulfill.domain.request.TriggerOrderWmsShipEventRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName OrderOutStockWmsEventProcessor
 * @Description 订单已出库事件处理器
 * @Author Long
 * @Date 2022/5/15 15:25
 * @Version 1.0
 */
@Slf4j
@Service
public class OrderOutStockWmsEventProcessor extends AbstractWmsShipEventProcessor {

    @Override
    protected String buildMsgBody(TriggerOrderWmsShipEventRequest request) {

        String orderId = request.getOrderId();

        // 订单已出库
        OrderOutStockWmsEvent outStockWmsEvent = (OrderOutStockWmsEvent) request.getWmsShipEvent();

        outStockWmsEvent.setOrderId(orderId);

        // 构造订单已出库消息

        OrderEvent<OrderOutStockWmsEvent> orderEvent = buildOrderEvent(orderId, OrderStatusChangEnum.ORDER_OUT_STOCKED,outStockWmsEvent,
                OrderOutStockWmsEvent.class);





        return JSONObject.toJSONString(orderEvent);
    }




    @Override
    protected void doBizProcess(TriggerOrderWmsShipEventRequest request) {

        log.info("准备进行商品出库【订单已出库事件】");



    }
}
