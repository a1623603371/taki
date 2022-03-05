package com.taki.fulfill.domain.evnet;

import com.taki.common.enums.OrderStatusChangEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;

/**
 * @ClassName OrderEvent
 * @Description 正向订单通同事件
 * @Author Long
 * @Date 2022/3/5 11:23
 * @Version 1.0
 */
@Data
@Builder
public class OrderEvent<T> implements Serializable {

    /**
     * 业务标识线
     */
    private Integer businessIdentifier;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 订单类型
     */
    private Integer orderType;

    /**
     * 卖家编码
     */
    private String sellerId;

    /**
     *订单变更事件
     */
    private OrderStatusChangEnum orderStatusChang;

    /**
     *消息体
     */
    private T messageContent;

    @Tolerate
    public OrderEvent() {
    }
}
