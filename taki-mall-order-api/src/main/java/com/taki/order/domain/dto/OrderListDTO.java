package com.taki.order.domain.dto;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName OrderListDTO
 * @Description 订单列表
 * @Author Long
 * @Date 2022/2/27 18:48
 * @Version 1.0
 */
@Data
public class OrderListDTO  implements Serializable {


    private static final long serialVersionUID = 6864863571709495207L;

    /**
     * 业务标识线
     */
    private Integer businessIdentifier;


    /**
     * 订单Id
     */
    private String orderId;
    /**
     * 主订单号
     */
    private String parentOrderId;

    /**
     * 接入方订单Id
     */
    private String businessOrderId;

    /**
     * 订单类型
     */
    private Integer orderType;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 卖家Id
     */
    private String sellerId;

    /**
     * 买家Id
     */
    private String userId;

    /**
     * 支付方式
     */
    private Integer payType;

    /**
     * 优惠券Id
     */
    private String couponId;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     *用户备注
     */
    private String userRemark;

    /**
     *订单评论状态 0:未发布评论 1.一发表评论
     */
    private Integer commentStatus;

    /**
     * 商品图片
     */
    private String productImg;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品编码
     */
    private String skuCode;

    /**
     *销量数量
     */
    private Integer saleQuantity;

    /**
     *交易支付金额
     */
    private Integer payAmount;
}
