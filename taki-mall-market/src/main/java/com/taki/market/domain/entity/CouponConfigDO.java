package com.taki.market.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.core.AbstractObject;
import com.taki.common.domin.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName CouponConfigDO
 * @Description 优惠券配置
 * @Author Long
 * @Date 2022/2/18 10:33
 * @Version 1.0
 */
@Data
@TableName("market_coupon_config")
public class CouponConfigDO extends BaseEntity implements Serializable {


    private static final long serialVersionUID = -3721803486028522695L;


    /**
     * 优惠券配置Id
     */
    private String couponConfigId;


    /**
     * 优惠券名称
     */
    private String name;


    /**
     * 优惠券类型
     */
    private Integer type;


    /**
     *优惠券抵扣价格
     */
    private BigDecimal amount;


    /**
     *优惠券使用条件
     */
    private Integer conditionAmount;


    /**
     * 有效开始时间
     */
    private LocalDateTime validStartTime;


    /**
     * 有效结束时间
     */
    private LocalDateTime validEndTime;

    /**
     * 优惠券 发行数量
     */
    private Long giveOutCount;

    /**
     * 优惠券领取数量
     */
    private Long receivedCount;


    /**
     * 优惠券发放方式 1. 可发放领取 2.仅可发放 3.仅可领取
     */
    private Integer giveOutType;


    /**
     * 优惠券 状态，1.未开始 2.发放中 3.已发放 4.已过期
     */
    private Integer status;


    public static final String COUPON_CONFIG_ID = "coupon_Config_Id";

}
