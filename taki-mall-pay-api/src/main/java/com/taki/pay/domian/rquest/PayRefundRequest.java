package com.taki.pay.domian.rquest;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName PayRefundRequest
 * @Description TODO
 * @Author Long
 * @Date 2022/1/18 16:16
 * @Version 1.0
 */
@Data
public class PayRefundRequest implements Serializable {


    private static final long serialVersionUID = -5892989716957819346L;


    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     *售后 单号
     */
    private String afterSaleId;

    /**
     * 交易流水号
     */
    private String outTradeNo;

}
