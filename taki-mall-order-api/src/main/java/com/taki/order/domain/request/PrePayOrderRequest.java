package com.taki.order.domain.request;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName PrePayOrderRequest
 * @Description 预支付订单请求
 * @Author Long
 * @Date 2022/1/15 14:38
 * @Version 1.0
 */
@Data
public class PrePayOrderRequest extends AbstractObject implements Serializable {


    private static final long serialVersionUID = 4105893410648718910L;

    /**
     * 用户Id
     */
    private String userId;
    /**
     * 业务方标识
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
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 支付成功跳转地址
     */
    private String callbackUrl;

    /**
     * 支付失败跳转
     */
    private String callbackFailUrl;

    /**
     * 微信openId
     */
    private String openId;

    /**
     * 商品明显JSON
     */
    private String itemInfo;

}
