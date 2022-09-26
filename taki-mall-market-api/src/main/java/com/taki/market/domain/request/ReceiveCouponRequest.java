package com.taki.market.domain.request;

import lombok.Builder;
import lombok.Data;

import javax.print.DocFlavor;
import java.time.LocalDateTime;

/**
 * @ClassName ReceiveCouponRequest
 * @Description 领取优惠券请求实体
 * @Author Long
 * @Date 2022/9/26 21:25
 * @Version 1.0
 */
@Data
@Builder
public class ReceiveCouponRequest {


    /**
     * 用户Id
     */
    private String userId;

    /**
     * 优惠券Id
     */
    private String couponId;

    /**
     * 优惠券类型 1 现金券 ，2 满减券
     */
    private Integer couponType;

    /**
     * 优惠券规则
     */
    private String couponRule;

    /**
     *优惠券生效开始时间
     */
    private LocalDateTime activityStartTime;

    /**
     * 优惠券生效结束时间
     */
    private LocalDateTime activityEndTime;

}
