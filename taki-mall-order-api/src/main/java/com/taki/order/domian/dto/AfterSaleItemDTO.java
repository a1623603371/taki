package com.taki.order.domian.dto;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName AfterSaleItemDTO
 * @Description 订单售后详情
 * @Author Long
 * @Date 2022/3/2 23:16
 * @Version 1.0
 */
@Data
public class AfterSaleItemDTO extends AbstractObject implements Serializable {
    /**
     * 售后id
     */
    private Long afterSaleId;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * sku code
     */
    private String skuCode;

    /**
     * 商品图片地址
     */
    private String productImg;

    /**
     * 商品退货数量
     */
    private Integer returnQuantity;

    /**
     * 商品总金额
     */
    private Integer originAmount;

    /**
     * 申请退款金额
     */
    private Integer applyRefundAmount;

    /**
     * 实际退款金额
     */
    private Integer realRefundAmount;

    /**
     * 商品名
     */
    private String itemName;

    /**
     * 商品类型
     */
    private Integer itemType;

    /**
     * 商品退货数量
     */
    private Integer itemNum;

    /**
     * 商品图片地址
     */
    private String itemImg;
}
