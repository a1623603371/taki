package com.taki.order.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单条目表
 * </p>
 *
 * @author long
 * @since 2022-01-02
 */
@Getter
@Setter
@TableName("order_item")
public class OrderItemDO extends BaseEntity implements Serializable{

    private static final long serialVersionUID = 1L;

   //("订单编号")
    @TableField("order_id")
    private String orderId;

  //("订单明细编号")
    @TableField("order_item_id")
    private String orderItemId;

  //"商品类型 1:普通商品,2:预售商品")
    @TableField("product_type")
    private Integer productType;

   //("商品编号")
    @TableField("product_id")
    private String productId;

   //("商品图⽚")
    @TableField("product_img")
    private String productImg;

    //("商品名称")
    @TableField("product_name")
    private String productName;

   //("sku编码")
    @TableField("sku_code")
    private String skuCode;

   //("销售数量")
    @TableField("sale_quantity")
    private Integer saleQuantity;

    //("销售单价")
    @TableField("sale_price")
    private BigDecimal salePrice;

    //("当前商品⽀付原总价")
    @TableField("origin_amount")
    private BigDecimal originAmount;

   //("交易⽀付⾦额")
    @TableField("pay_amount")
    private BigDecimal payAmount;

   //("商品单位")
    @TableField("product_unit")
    private String productUnit;

    //("采购成本价")
    @TableField("purchase_price")
    private BigDecimal purchasePrice;

   //("卖家编号")
    @TableField("seller_id")
    private String sellerId;




    public static final String ORDER_ID = "order_id";

    public static final String ORDER_ITEM_ID = "order_item_id";

    public static final String PRODUCT_TYPE = "product_type";

    public static final String PRODUCT_ID = "product_id";

    public static final String PRODUCT_IMG = "product_img";

    public static final String PRODUCT_NAME = "product_name";

    public static final String SKU_CODE = "sku_code";

    public static final String SALE_QUANTITY = "sale_quantity";

    public static final String SALE_PRICE = "sale_price";

    public static final String ORIGIN_AMOUNT = "origin_amount";

    public static final String PAY_AMOUNT = "pay_amount";

    public static final String PRODUCT_UNIT = "product_unit";

    public static final String PURCHASE_PRICE = "purchase_price";

    public static final String SELLER_ID = "seller_id";




}
