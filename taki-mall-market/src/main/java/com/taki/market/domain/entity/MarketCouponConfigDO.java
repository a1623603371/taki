package com.taki.market.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 优惠券表
 * </p>
 *
 * @author long
 * @since 2022-09-24
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("market_coupon_config")
public class MarketCouponConfigDO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 优惠券名称
     */
    @TableField("coupon_name")
    private String couponName;

    /**
     * 优惠券类型，1：现金券，2：满减券
     */
    @TableField("coupon_type")
    private Integer couponType;

    /**
     * 优惠券规则
     */
    @TableField("coupon_rule")
    private String couponRule;

    /**
     * 有效期开始时间
     */
    @TableField("valid_start_time")
    private LocalDateTime validStartTime;

    /**
     * 有效期结束时间
     */
    @TableField("valid_end_time")
    private LocalDateTime validEndTime;

    /**
     * 优惠券发行数量
     */
    @TableField("give_out_count")
    private Long giveOutCount;

    /**
     * 优惠券已经领取的数量
     */
    @TableField("received_count")
    private Long receivedCount;

    /**
     * 优惠券发放方式，1：可发放可领取，2：仅可发放，3：仅可领取
     */
    @TableField("give_out_type")
    private Integer giveOutType;

    /**
     * 优惠券状态，1：未开始；2：发放中，3：已发完；4：已过期
     */
    @TableField("`status`")
    private Integer status;

    /**
     * 通知类型：1：短信，2：app消息，3：邮箱
     */
    @TableField("inform_type")
    private Integer informType;


    public static final String COUPON_NAME = "coupon_name";

    public static final String COUPON_TYPE = "coupon_type";

    public static final String COUPON_RULE = "coupon_rule";

    public static final String VALID_START_TIME = "valid_start_time";

    public static final String VALID_END_TIME = "valid_end_time";

    public static final String GIVE_OUT_COUNT = "give_out_count";

    public static final String RECEIVED_COUNT = "received_count";

    public static final String GIVE_OUT_TYPE = "give_out_type";

    public static final String STATUS = "status";

    public static final String INFORM_TYPE = "inform_type";

}
