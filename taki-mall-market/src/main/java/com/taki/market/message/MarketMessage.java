package com.taki.market.message;

import lombok.Data;

/**
 * @ClassName MarketMessage
 * @Description 营销消息
 * @Author Long
 * @Date 2022/2/26 16:43
 * @Version 1.0
 */
@Data
public class MarketMessage {

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 优惠券Id
     */
    private String couponId;
}
