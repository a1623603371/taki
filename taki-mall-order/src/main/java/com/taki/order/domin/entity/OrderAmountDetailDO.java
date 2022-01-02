package com.taki.order.domin.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
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
@Accessors(chain = true)
@TableName("order_amount_detail")
@ApiModel(value = "OrderAmountDetailDO对象", description = "订单费⽤明细	表")
public class OrderAmountDetailDO extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单编号")
    @TableField("order_id")
    private String orderId;

    @ApiModelProperty("产品类型")
    @TableField("product_type")
    private Integer productType;

    @ApiModelProperty("订单明细编号")
    @TableField("order_item_id")
    private String orderItemId;

    @ApiModelProperty("商品编号")
    @TableField("product_id")
    private String productId;

    @ApiModelProperty("sku编码")
    @TableField("sku_code")
    private String skuCode;

    @ApiModelProperty("销售数量")
    @TableField("sale_quantity")
    private Integer saleQuantity;

    @ApiModelProperty("销售单价")
    @TableField("sale_price")
    private Integer salePrice;

    @ApiModelProperty("收费类型")
    @TableField("amount_type")
    private Integer amountType;

    @ApiModelProperty("收费⾦额")
    @TableField("amount")
    private Integer amount;

    @ApiModelProperty("创建时间")
    @TableField("gmt_create")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField("gmt_modified")
    private LocalDateTime gmtModified;


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
