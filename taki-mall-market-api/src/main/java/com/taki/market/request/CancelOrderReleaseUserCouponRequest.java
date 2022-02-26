package com.taki.market.request;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName CancelOrderReleaseUserCouponRequest
 * @Description 取消订单 释放 用户 优惠券 参数
 * @Author Long
 * @Date 2022/2/26 16:57
 * @Version 1.0
 */
@Data
public class CancelOrderReleaseUserCouponRequest extends AbstractObject implements Serializable {


    private static final long serialVersionUID = -4846744440474398892L;


    /**
     * 用户ID
     */
    private String userId;


    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 优惠券Id
     */
    private String couponId;


    /**
     * 业务标识
     */
    private Integer businessIdentifier;
}
