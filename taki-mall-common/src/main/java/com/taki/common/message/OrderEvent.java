package com.taki.common.message;

import com.taki.common.enums.OrderStatusChangEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Tolerate;

import java.io.Serializable;

/**
 * @ClassName OrderEvent
 * @Description 正向订单相关
 * @Author Long
 * @Date 2022/6/9 12:35
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor

public class OrderEvent<T> implements Serializable {


    private static final long serialVersionUID = -4040695832697777598L;

    /**
     * 业务线标识
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
     * 卖家编号
     */
    private String sellerId;

    /**
     * 订单变更事件
     */
    private OrderStatusChangEnum orderStatusChang;

    /**
     * 消息体
     */
    private T messageContent;

    @Tolerate
    public OrderEvent() {
    }
}


