package com.taki.common.constants;

/**
 * @ClassName RedisLockKeyConstants
 * @Description redis 分布式锁key
 * @Author Long
 * @Date 2022/1/15 14:59
 * @Version 1.0
 */
public class RedisLockKeyConstants {

    /**
     * 订单支付 key
     */
    public static final String ORDER_PAY_KEY = "#ORDER_PAY_KEY:";

    /**
     * 订单履约 key
     */
    public static  final  String  ORDER_FULFILL_KEY = "#ORDER_FULFILL_KEY:";

    /**
     * 订单 WMS KEY
     */
    public static  final  String    ORDER_WMS_RESULT_KEY = "#ORDER_WMS_RESULT_KEY:";

    /**
     * 取消退款 KEY
     */
    public static  final  String CANCEL_REFUND_KEY = "#CANCEL_REFUND_KEY:";

    /**
     * 售后 订单 key
     */
    public static  final  String AFTER_SALE_ORDER_KEY = "#AFTER_SALE_ORDER_KEY:";
    /**
     * 退款 KEY
     */
    public static  final  String REFUND_KEY = "#REFUND_KEY:";
    /**
     * 取消 KEY
     */
    public static  final  String CANCEL_KEY = "#CANCEL_KEY:";



}
