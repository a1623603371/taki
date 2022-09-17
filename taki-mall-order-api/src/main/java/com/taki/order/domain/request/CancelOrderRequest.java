package com.taki.order.domain.request;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName CancelOrderRequest
 * @Description 取消订单请求
 * @Author Long
 * @Date 2022/3/5 15:03
 * @Version 1.0
 */
@Data
public class CancelOrderRequest  implements Serializable  {


    private static final long serialVersionUID = 2723268840083209556L;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 业务标识线
     */
    private Integer businessIdentifier;

    /**
     * 取消类型
     */
    private Integer cancelType;

    /**
     * 用户iD
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

    /**
     * 原订单状态
     */
    private Integer oldOrderStatus;


}
