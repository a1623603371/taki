package com.taki.pay.domian.rquest;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName PayOrderRequest
 * @Description 支付订单请求
 * @Author Long
 * @Date 2022/1/16 14:04
 * @Version 1.0
 */
@Data
public class PayOrderRequest extends AbstractObject implements Serializable {


    private static final long serialVersionUID = -4981057846117876028L;

    /**
     * 用户Id
     */
    private String userId;
    /**
     * 业务标识
     */
    private String businessIdentifier;

    /**
     * 支付类型
     */
    private Integer payType;
    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 订单支付金额
     */
    private BigDecimal payAmount;

    /**
     * 支付成功跳地址
     */
    private String callbackUrl;

    /**
     * 支付失败跳转地址
     */
    private  String callbackFailUrl;


    /**
     * 微信 openId
     */
    private String  openId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 订单 摘要
     */
    private String subject;
    /**
     * 商品明细JSON
     */
    private String itemJson;

}
