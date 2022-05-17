package com.taki.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName DeliverOrderDO
 * @Description TODO
 * @Author Long
 * @Date 2022/5/16 20:26
 * @Version 1.0
 */
@Data
@TableName("deliver_order")
public class DeliverOrderDO extends BaseEntity implements Serializable {

    /**
     * 业务标识线 1 。 “自营商城”
     */
    private Integer businessIdentifier;

    /**
     *出库单Id
     */
    private String deliveryOrderId;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 商家Id
     */
    private String sellerId;

    /**
     * 用户ID
     *
     */
    private String userId;

    /**
     * 支付类型
     */
    private Integer payType;

    /**
     * 支付金额
     */
    private BigDecimal  payAmount;

    /**
     * 支付总金额
     */
    private BigDecimal totalAmount;

    /**
     * 运费
     */
    private BigDecimal deliveryAmount;

    public static  final  String ORDER_ID = "order_id";
}

