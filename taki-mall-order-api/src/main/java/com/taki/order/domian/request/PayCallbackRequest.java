package com.taki.order.domian.request;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName PayCallbackRequest
 * @Description 支付回调
 * @Author Long
 * @Date 2022/1/17 16:09
 * @Version 1.0
 */
@Data
public class PayCallbackRequest extends AbstractObject implements Serializable {


    private static final long serialVersionUID = 290296152597375935L;


    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 支付账号
     */
    private String payAccount;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 支付系统交易Id
     */
    private String outTraderNo;

    /**
     * 支付类型
     */
    private Integer payType;

    /**
     * 商品Id
     */
    private String merchantId;

    /**
     * 支付渠道
     */
    private String payChannel;

    /**
     * 微信 平台 appid
     */
    private String appid;


}
