package com.taki.market.domain.dto;

import lombok.Data;

/**
 * @ClassName ReceiveCouponDTO
 * @Description 领取优惠券返回值
 * @Author Long
 * @Date 2022/9/26 21:21
 * @Version 1.0
 */
@Data
public class ReceiveCouponDTO {

    /**
     *领取结果，是否超过
     */
    private Boolean success;

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     *优惠规则
     */
    private  String rule;

    /**
     * 领取失败时，返回信息
     */
    private String message;
}
