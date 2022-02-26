package com.taki.market.domain.query;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName UserCouponQuery
 * @Description TODO
 * @Author Long
 * @Date 2022/1/8 14:32
 * @Version 1.0
 */
@Data
public class UserCouponQuery implements Serializable {


    private static final long serialVersionUID = 2884829415705800974L;

    /**
     * 优惠券Id
     */
    private String couponId;

    /**
     * 用户Id
     */
    private String userId;
}
