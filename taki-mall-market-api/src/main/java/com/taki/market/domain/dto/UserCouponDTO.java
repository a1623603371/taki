package com.taki.market.domain.dto;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName UserCouponDTO
 * @Description 用户优惠券
 * @Author Long
 * @Date 2022/1/8 14:35
 * @Version 1.0
 */
@Data
public class UserCouponDTO extends AbstractObject implements Serializable {


    private static final long serialVersionUID = 6493181412536567634L;


    /**
     * 用户Id
     */
    private String userId;
    /**
     * 优惠券Id
     */
    private String couponId;

    /**
     *优惠券名称
     */
    private String couponName;

    /**
     * 优惠券类型 1.现金券 2.满减券
     */
    private Integer couponType;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     *优惠券使用限制金额
     */
    private BigDecimal conditionAmount;

    /**
     * 有效开始时间
     */
    private LocalDateTime validStartTime;

    /**
     * 有效结束时间
     */
    private LocalDateTime validEndTime;
}
