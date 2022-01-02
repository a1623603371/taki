package com.taki.order.domin.request;

import com.taki.common.core.AbstractObject;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName CreateOrderRequest
 * @Description 创建订单请求
 * * @Author Long
 * @Date 2022/1/3 0:16
 * @Version 1.0
 */
public class CreateOrderRequest extends AbstractObject implements Serializable {


    private static final long serialVersionUID = -397550716343729406L;

    /**
     *订单号
     */
    private String orderId;

    /**
     * 业务线标识
     */
    private Integer businessIdentifier;

    /**
     *用户信息
     * 微信opendId
     */
    private String openId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 订单类型
     */
    private Integer orderType;

    /**
     *卖家Id
     */
    private String sellerId;

    /**
     * 用户备注
     */
    private String userRemark;


    /**
     *  优惠券Id
     */
    private String couponId;


    /**
     *送货方式
     */
    private Integer deliveryType;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     *区
     */
    private String area;

    /**
     * 地址详情
     */
    private String detailAddress;

    /**
     *精度
     */
    private BigDecimal lon;

    /**
     *维度
     */
    private BigDecimal lat;



}

