package com.taki.pay.constants;

/**
 * @ClassName PayTransactionStatus
 * @Description 支付交易状态
 * @Author Long
 * @Date 2022/5/18 16:40
 * @Version 1.0
 */
public class PayTransactionStatus {

    /**
     * 未支付
     */
    public static final  Integer UNPAYED = 1;

    /**
     *
     * 支付成功
     */
    public static final  Integer SUCCESS = 2;

    /**
     * 支付失败
     */
    public static  final    Integer FAILURE = 3;

    /**
     * 支付交易关闭
     */
    public static  final    Integer CLOSED = 4;

    /**
     * 支付退款
     */
    public  static  final   Integer REFUND = 5;

}
