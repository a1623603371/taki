package com.taki.order.domin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@Accessors(chain = true)
@TableName("order_item")
@ApiModel(value = "OrderItemDO对象", description = "订单条目表")
public class OrderItemDO extends BaseEntity implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单编号")
    @TableField("order_id")
    private String orderId;

    @ApiModelProperty("订单明细编号")
    @TableField("order_item_id")
    private String orderItemId;

    @ApiModelProperty("商品类型 1:普通商品,2:预售商品")
    @TableField("product_type")
    private Integer productType;

    @ApiModelProperty("商品编号")
    @TableField("product_id")
    private String productId;

    @ApiModelProperty("商品图⽚")
    @TableField("product_img")
    private String productImg;

    @ApiModelProperty("商品名称")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty("sku编码")
    @TableField("sku_code")
    private String skuCode;

    @ApiModelProperty("销售数量")
    @TableField("sale_quantity")
    private Integer saleQuantity;

    @ApiModelProperty("销售单价")
    @TableField("sale_price")
    private BigDecimal salePrice;

    @ApiModelProperty("当前商品⽀付原总价")
    @TableField("origin_amount")
    private BigDecimal originAmount;

    @ApiModelProperty("交易⽀付⾦额")
    @TableField("pay_amount")
    private BigDecimal payAmount;

    @ApiModelProperty("商品单位")
    @TableField("product_unit")
    private String productUnit;

    @ApiModelProperty("采购成本价")
    @TableField("purchase_price")
    private BigDecimal purchasePrice;

    @ApiModelProperty("卖家编号")
    @TableField("seller_id")
    private String sellerId;

    @ApiModelProperty("创建时间")
    @TableField("gmt_create")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField("gmt_modified")
    private LocalDateTime gmtModified;


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

    public static final String GMT_CREATE = "gmt_create";

    public static final String GMT_MODIFIED = "gmt_modified";



}
