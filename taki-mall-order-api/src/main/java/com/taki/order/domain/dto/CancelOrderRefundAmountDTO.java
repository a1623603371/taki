package com.taki.order.domain.dto;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName CancelOrderRefundAmountDTO
 * @Description 取消订单 退款金额 DTO
 * @Author Long
 * @Date 2022/3/9 10:49
 * @Version 1.0
 */
@Data
public class CancelOrderRefundAmountDTO extends AbstractObject implements Serializable {

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;


    /**
     * 实际退款金额
     */
    private BigDecimal returnGoodAmount;


    /**
     * 申请退款金额
     */
    private BigDecimal applyRefundAmount;

    /**
     * sku编号
     */
    private  String skuCode;

    /**
     * 退货数量
     */
    private Integer returnNum;
}
