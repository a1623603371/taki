package com.taki.order.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.core.AbstractObject;
import com.taki.common.domin.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName AfterSaleItemDO
 * @Description 售后单条目
 * @Author Long
 * @Date 2022/3/11 11:26
 * @Version 1.0
 */
@Data
@TableName("after_sale_item")
@EqualsAndHashCode(callSuper=false)
public class AfterSaleItemDO extends BaseEntity implements Serializable {


    private static final long serialVersionUID = 637389812323015444L;

    /**
     * 售后ID
     */
    private String afterSaleId;

    /**
     * 订单ID
     *
     */
    private String orderId;

    /**
     * sku code
     */
    private String skuCode;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品 图片
     */
    private String productImg;

    /**
     * 退款数量
     */
    private Integer returnQuantity;

    /**
     * 商品总金额
     */
    private BigDecimal originAmount;

    /**
     *申请退款金额
     */
    private BigDecimal applyRefundAmount;

    /**
     *实际退款金额
     */
    private BigDecimal realRefundAmount;


    public   static final  String ORDER_ID = "order_id";

    public   static  final  String SKU_CODE = "sku_code";

    public static  final  String  AFTER_SALE_ID = "after_Sale_id";
}
