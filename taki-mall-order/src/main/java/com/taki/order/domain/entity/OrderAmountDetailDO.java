package com.taki.order.domain.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单费⽤明细	表
 * </p>
 *
 * @author long
 * @since 2022-01-02
 */
@Data
@TableName("order_amount_detail")
@EqualsAndHashCode(callSuper=false)
public class OrderAmountDetailDO extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

  //("订单编号")
    @TableField("order_id")
    private String orderId;

    //("产品类型")
    @TableField("product_type")
    private Integer productType;

    //("订单明细编号")
    @TableField("order_item_id")
    private String orderItemId;

   //("商品编号")
    @TableField("product_id")
    private String productId;

   //("sku编码")
    @TableField("sku_code")
    private String skuCode;

    //("销售数量")
    @TableField("sale_quantity")
    private Integer saleQuantity;

  //("销售单价")
    @TableField("sale_price")
    private BigDecimal salePrice;

   //("收费类型")
    @TableField("amount_type")
    private Integer amountType;

    //("收费⾦额")
    @TableField("amount")
    private BigDecimal amount;




    public static final String ORDER_ID = "order_id";

    public static final String PRODUCT_TYPE = "product_type";

    public static final String ORDER_ITEM_ID = "order_item_id";

    public static final String PRODUCT_ID = "product_id";

    public static final String SKU_CODE = "sku_code";

    public static final String SALE_QUANTITY = "sale_quantity";

    public static final String SALE_PRICE = "sale_price";

    public static final String AMOUNT_TYPE = "amount_type";

    public static final String AMOUNT = "amount";

    public static final String GMT_CREATE = "gmt_create";

    public static final String GMT_MODIFIED = "gmt_modified";


}
