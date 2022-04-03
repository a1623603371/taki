package com.taki.common.message;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName PayOrderTimeOutDelayMessage
 * @Description 订单支付超时 取消订单消息
 * @Author Long
 * @Date 2022/1/12 9:57
 * @Version 1.0
 */
@Data
@Builder
public class PayOrderTimeOutDelayMessage implements Serializable {


    private static final long serialVersionUID = -7970466152060431689L;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     *订单渠道来源
     */
    private Integer businessIdentifier;

    /**
     * 取消类型
     */
    private Integer cancelType;

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 订单类型
     */
    private Integer orderType;

    /**
     * 订单状态
     */
    private Integer orderStatus;
}
