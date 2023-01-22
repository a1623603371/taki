package com.taki.market.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 优惠券领取记录表
 * </p>
 *
 * @author long
 * @since 2022-09-25
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("market_coupon_item")
@Builder
public class MarketCouponItemDO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 优惠券ID
     */
    @TableField("coupon_id")
    private Long couponId;

    /**
     * 优惠券类型：1：现金券，2：满减券
     */
    @TableField("coupon_type")
    private Integer couponType;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 是否使用过这个优惠券，1：使用了，0：未使用
     */
    @TableField("is_used")
    private Integer used;

    /**
     * 使用优惠券的时间
     */
    @TableField("used_time")
    private LocalDateTime usedTime;

    /**
     * 有效期开始时间
     */
    @TableField("activity_start_time")
    private LocalDateTime activityStartTime;

    /**
     * 有效期结束时间
     */
    @TableField("activity_end_time")
    private LocalDateTime activityEndTime;


    public static final String COUPON_ID = "coupon_id";

    public static final String COUPON_TYPE = "coupon_type";

    public static final String USER_ID = "user_id";

    public static final String IS_USED = "is_used";

    public static final String USED_TIME = "used_time";

    public static final String ACTIVITY_START_TIME = "activity_start_time";

    public static final String ACTIVITY_END_TIME = "activity_end_time";

}
