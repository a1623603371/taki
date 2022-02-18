package com.taki.market.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName CouponDO
 * @Description 优惠券
 * @Author Long
 * @Date 2022/2/18 10:46
 * @Version 1.0
 */
@Data
@TableName("market_coupon")
public class CouponDO extends BaseEntity implements Serializable {


    private static final long serialVersionUID = -584356599395691814L;

    /**
     * 优惠券Id
     */
    private String couponId;

    /**
     * 优惠券配置Id
     */
    private String couponConfigId;


    /**
     * 用户Id
     */
    private String userId;

    /**
     * 是否使用
     */
    @TableField(value = "is_used")
    private Integer used;


    /**
     * 使用时间
     */
    private LocalDateTime  usedTime;

    /**
     * 抵扣价格
     */
    private Integer amount;



    public static final  String USER_ID = "user_id";

    public  static final  String COUPON_ID ="coupon_id";
}
