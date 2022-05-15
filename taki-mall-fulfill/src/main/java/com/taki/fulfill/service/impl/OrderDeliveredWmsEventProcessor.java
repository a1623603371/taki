package com.taki.fulfill.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.enums.OrderStatusChangEnum;
import com.taki.fulfill.dao.OrderFulfillDao;
import com.taki.fulfill.domain.evnet.OrderDeliveredWmsEvent;
import com.taki.fulfill.domain.evnet.OrderEvent;
import com.taki.fulfill.domain.request.TriggerOrderWmsShipEventRequest;
import lombok.extern.slf4j.Slf4j;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * @ClassName OrderDeliveredWmsEventProcessor
 * @Description 订单已配送事件处理器
 * @Author Long
 * @Date 2022/5/15 15:53
 * @Version 1.0
 */
@Slf4j
@Service
public class OrderDeliveredWmsEventProcessor extends AbstractWmsShipEventProcessor{

    @Autowired
    private OrderFulfillDao orderFulfillDao;

    @Override
    protected String buildMsgBody(TriggerOrderWmsShipEventRequest request) {

        String  orderId = request.getOrderId();

        // 订单已配送事件
        OrderDeliveredWmsEvent deliveredWmsEvent = (OrderDeliveredWmsEvent) request.getWmsShipEvent();

        deliveredWmsEvent.setOrderId(orderId);

        // 构造 订单已配送消息体

        OrderEvent<OrderDeliveredWmsEvent>   orderEvent = buildOrderEvent(orderId, OrderStatusChangEnum.ORDER_DELIVERED,deliveredWmsEvent,OrderDeliveredWmsEvent.class);

        return JSONObject.toJSONString(orderEvent);
    }

    @Override
    protected void doBizProcess(TriggerOrderWmsShipEventRequest request) {
        OrderDeliveredWmsEvent deliveredWmsEvent = (OrderDeliveredWmsEvent) request.getWmsShipEvent();

        String fulfillId = request.getFulfillId();

        // 更新配送员 信息

        orderFulfillDao.updateDelivery(fulfillId,deliveredWmsEvent.getDelivererNo(),deliveredWmsEvent.getDelivererName(),deliveredWmsEvent.getDelivererPhone());
    }
}
