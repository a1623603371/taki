package com.taki.order.domain.dto;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName AfterSaleOrderItemDTO
 * @Description 售后订单条目
 * @Author Long
 * @Date 2022/4/3 20:18
 * @Version 1.0
 */
@Data
public class AfterSaleOrderItemDTO extends AbstractObject implements Serializable {


    private static final long serialVersionUID = -1445538931572036550L;
    /**
     * 售后Id
     */
    private Long afterSaleId;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * SKU 编码
     */
    private String skuCode;

    /**
     * 商品图片
     */
    private String productImg;


    /**
     * 退货数量
     */
    private Integer returnQuantity;

    /**
     * 商品总金额
     */
    private BigDecimal originAmount;

    /**
     * 申请退款金额
     */
    private BigDecimal applyRefundAmount;

    /**
     * 商品名
     */
    private String itemName;

    /**
     * 商品类型
     */
    private Integer itemType;

    /**
     * 商品图片
     */
    private String itemImg;
}
