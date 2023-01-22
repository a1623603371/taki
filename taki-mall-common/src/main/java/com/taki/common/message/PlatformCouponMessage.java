package com.taki.common.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName PlatformCouponMessage
 * @Description TODO
 * @Author Long
 * @Date 2022/10/7 17:23
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlatformCouponMessage implements Serializable {


    private static final long serialVersionUID = 6316675219112162621L;


    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 用户id
     */





    private Long userId;



    private Integer couponType;
}
