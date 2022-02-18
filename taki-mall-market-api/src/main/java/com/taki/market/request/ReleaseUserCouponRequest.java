package com.taki.market.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ReleaseUserCouponRequest
 * @Description TODO
 * @Author Long
 * @Date 2022/2/18 13:55
 * @Version 1.0
 */
@Data
public class ReleaseUserCouponRequest implements Serializable {


    private static final long serialVersionUID = 2414746834460222671L;


    /**
     * 用户Id
     */
    private String userId;


    /**
     *
     *优惠券Id
     */
    private String couponId;
}
