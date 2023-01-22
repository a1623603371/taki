package com.taki.market.domain.dto;

import com.taki.common.domin.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

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

public class MarketCouponItemDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 优惠券ID
     */

    private String couponId;

    /**
     * 优惠券类型：1：现金券，2：满减券
     */

    private Integer couponType;

    /**
     * 用户ID
     */

    private String userId;

    /**
     * 是否使用过这个优惠券，1：使用了，0：未使用
     */

    private Integer used;

    /**
     * 使用优惠券的时间
     */

    private LocalDateTime usedTime;

    /**
     * 有效期开始时间
     */

    private LocalDateTime activityStartTime;

    /**
     * 有效期结束时间
     */

    private LocalDateTime activityEndTime;




}
