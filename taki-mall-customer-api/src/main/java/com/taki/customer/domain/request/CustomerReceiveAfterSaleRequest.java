package com.taki.customer.domain.request;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName CustomerReceiveAfterSaleRequest
 * @Description 客服接收订单系统的售后申请入参
 * @Author Long
 * @Date 2022/6/9 16:11
 * @Version 1.0
 */
@Data
public class CustomerReceiveAfterSaleRequest extends AbstractObject implements Serializable {

    private static final long serialVersionUID = 8918610925179848880L;

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 售后Id
     */
    private String afterSaleId;

    /**
     * 售后支付单Id
     */
    private Long  afterSaleRefundId;

    /**
     * 售后类型 1.退款 2.退货
     */
    private Integer afterSaleType;

    /**
     * 实际退款金额
     */
    private BigDecimal returnGoodsAmount;

    /**
     * 申请退款金额
     */
    private BigDecimal applyRefundAmount;
}
