package com.taki.common.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Tolerate;

import java.io.Serializable;

/**
 * @ClassName AfterSaleEvent
 * @Description 逆向订单 通用事件
 * @Author Long
 * @Date 2022/6/9 12:25
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
public class AfterSaleEvent<T> implements Serializable {


    private static final long serialVersionUID = -3910446673150457713L;
    /**
     * 业务标识线
     */
    private Integer businessIdentifier;

    /**
     * 售后单id
     */
    private Long afterSaleId;

    /**
     * 售后单类型
     */
    private Integer afterSaleType;

    /**
     * 申请售后来源
     */
    private Integer applySource;

    /**
     * 订单Id
     */
    private String orderId;


    /**
     * 消息体
     */
    private T messageContent;

    @Tolerate
    public AfterSaleEvent() {
    }
}
