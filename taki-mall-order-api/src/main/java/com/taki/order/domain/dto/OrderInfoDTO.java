package com.taki.order.domain.dto;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @ClassName OrderInfoDTO
 * @Description 订单 DTO
 * @Author Long
 * @Date 2022/3/2 22:14
 * @Version 1.0
 */
@Data
public class OrderInfoDTO extends AbstractObject implements Serializable {

    /**
     * 业务线标识线
     */
    private Integer businessIdentifier;

    /**
      * 主键Id
     */
    private Long id;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 父订单Id
     */
    private String parentOrderId;

    /**
     *业务线方 订单Id
     */
    private String businessOrderId;

    /**
     * 订单类型
     */
    private Integer orderType;

    /**
     * 订单状态 10 已创建 30 已履约 40出库 50配送中 60已签收 70 已取消 100 已拒收 255无效订单
     */
    private Integer orderStatus;

    /**
     * 取消类型
     */
    private String cancelType;
    /**
     * 取消时间
     */
    private Date cancelTime;

    /**
     * 卖家编号
     */
    private String sellerId;

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 支付类型
     */
    private Integer payType;

    /**
     * 交易总金额（以分为单位存储）
     */
    private BigDecimal totalAmount;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 优惠券Id
     */
    private String couponId;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 订单取消截止时间
     */
    private LocalDateTime cancelDeadlineTime;

    /**
     * 商家备注
     */
    private String sellerRemark;

    /**
     * 用户备注
     */
    private String userRemark;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 订单删除状态 ： 0未删除 2：已删除
     */
    private Integer deleteStatus;

    /**
     * 订单评论状态 0 ：未发布评论 1已发布评论
     */
    private Integer commentStatus;
}
