package com.taki.common.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName PlatformCouponUserBucketMessage
 * @Description TODO
 * @Author Long
 * @Date 2022/10/4 18:17
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlatformCouponUserBucketMessage  implements Serializable {


    private static final long serialVersionUID = -6270716998940002536L;


    /**
     * 开始桶内起始 用户id （包含）
     */
    private Long startUserId;


    /**
     * 结束桶内起始 用户id （包含）
     */
    private Long endUserId;

    /**
     *  优惠券id
     */
    private Long couponId;


    /**
     * 用户Id
     */
    private Long userId;


    /**
     * 优惠券类型 1.现金券， 2满减券
     */
    private Integer couponType;


    /**
     * 消息类型 1.短信 2.app 3.email
     */
    private Integer informType;


    /**
     * 生效开始时间
     */
    private LocalDateTime activityStartTime;

    /**
     * 生效结束时间
     */
    private LocalDateTime activityEndTime;
}


