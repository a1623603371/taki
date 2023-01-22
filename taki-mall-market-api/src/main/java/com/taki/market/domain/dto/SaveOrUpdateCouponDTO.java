package com.taki.market.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName SaveOrUpdateCouponDTO
 * @Description 创建/修改活动返回结果
 * @Author Long
 * @Date 2022/10/3 23:48
 * @Version 1.0
 */
@Data
public class SaveOrUpdateCouponDTO implements Serializable {


    /**
     *新增/修改是否成功
     */
    private Boolean success;

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     * 活动规则
     */
    private String rule;


}
