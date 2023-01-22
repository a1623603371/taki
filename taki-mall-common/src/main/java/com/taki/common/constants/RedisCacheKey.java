package com.taki.common.constants;

/**
 * @ClassName RedisCacheKey
 * @Description redis 缓存key
 * @Author Long
 * @Date 2021/12/20 13:41
 * @Version 1.0
 */
public class RedisCacheKey {


    public final static String  REGISTER_CODE_KEY = "#REGISTER_CODE:";

    /**
     * 促销优惠券key
     */
    public final static String PROMOTION_COUPON_KEY = "#PROMOTION_COUPON_KEY:";

    /**
     * 促销优惠券状态key
     */
    public final static String PROMOTION_CONDITION_COUPON_KEY = "#PROMOTION_CONDITION_COUPON_KEY:";



    /**
     * 促销用户接受优惠券key
     */
    public final static String  PROMOTION_USER_RECEIVE_COUPON_KEY = "#PROMOTION_USER_RECEIVE_COUPON_KEY:";



    /**
     * 促销优惠券id集合key
     */
    public  final static String PROMOTION_COUPON_ID_LIST_KEY = "#PROMOTION_COUPON_ID_LIST_KEY:";

    /**
     * 优惠券幂等key
     */
    public  final static String COUPON_NX_KEY = "#COUPON_NX_KEY:";

    /**
     * 促销 并发 KEY
     */
    public final static String PROMOTION_CONCURRENCY_KEY = "#PROMOTION_CONCURRENCY_KEY:";
}
