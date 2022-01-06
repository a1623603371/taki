package com.taki.market.request;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName LockUserCouponRequest
 * @Description 锁定优惠券请求参数
 * @Author Long
 * @Date 2022/1/6 9:46
 * @Version 1.0
 */
@Data
public class LockUserCouponRequest extends AbstractObject implements Serializable {


    private static final long serialVersionUID = 9148178913181458119L;
    /**
     * 业务标识线
     */
    private Integer businessIdentifier;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 卖家Id
     */
    private String sellerId;

    /**
     * 优惠券Id
     */
    private  String couponId;
}
