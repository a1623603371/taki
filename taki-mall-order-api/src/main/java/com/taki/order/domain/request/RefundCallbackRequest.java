package com.taki.order.domain.request;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName RefundCallbackRequest
 * @Description 支付系统 退款回调入参
 * @Author Long
 * @Date 2022/3/9 14:49
 * @Version 1.0
 */
@Data
public class RefundCallbackRequest  implements Serializable {


    private static final long serialVersionUID = -9008011726256609313L;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     *退款批次号
     */
    private String batchNo;

    /**
     * 支付接口返回 退款 结果 10未退款 20退款成功 30退款失败
     */
    private Integer refundStatus;


    /**
     * 退款费用
     */
    private Integer refundFee;

    /**
     * 退款总金额
     */
    private Integer totalFee;

    /**
     * 支付退款 签名
     */
    private String sign;

    /**
     * 交易号流水
     */
    private String tradeNo;

    /**
     * 退款时间
     */
    private LocalDateTime refundTime;

    /**
     * 售后单Id
     */
    private String afterSaleId;
}
