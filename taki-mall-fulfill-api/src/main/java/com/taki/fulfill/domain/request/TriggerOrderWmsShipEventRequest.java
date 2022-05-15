package com.taki.fulfill.domain.request;

import com.taki.common.enums.OrderStatusChangEnum;
import com.taki.fulfill.domain.evnet.BaseWmsShipEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName TriggerOrderWmsShipEventRequest
 * @Description 触发订单物流配送结果事件
 * @Author Long
 * @Date 2022/5/15 13:39
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TriggerOrderWmsShipEventRequest implements Serializable {


   private static final long serialVersionUID = 1218864243855747172L;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 履约单Id
     */
    private String fulfillId;


    /**
     * 订单状态变更
     */
    private OrderStatusChangEnum orderStatusChang;


    /**
     * 物流配送结果事件消息体
     */
    private BaseWmsShipEvent wmsShipEvent;


}
