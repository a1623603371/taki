package com.taki.market.domain.dto;

import lombok.Data;

/**
 * @ClassName SendCouponDTO
 * @Descriptio 领取优惠券返回值
 * @Author Long
 * @Date 2022/9/26 21:30
 * @Version 1.0
 */
@Data
public class SendCouponDTO {


    /**
     * 发送结果 是否成功
     */
    private Boolean success;

    /**
     * 发送数量
     */
    private Integer sendCount;

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     * 优惠规则
     */
    private String rule;
}
