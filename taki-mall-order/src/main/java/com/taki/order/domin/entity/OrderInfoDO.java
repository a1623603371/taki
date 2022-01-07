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
 * 
 * </p>
 *
 * @author long
 * @since 2022-01-02
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("order_info")
@ApiModel(value = "OrderInfoDO对象", description = "")
public class OrderInfoDO extends BaseEntity implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("接入方业务标识 1，自营商城")
    @TableField("business_identifier")
    private Integer businessIdentifier;

    @ApiModelProperty("订单编号")
    @TableField("order_id")
    private String orderId;

    @ApiModelProperty("订单状态 10：已创建，30 ：已履约，40：已出库 	50 ：已配送 60：已签收 70：已取消 100： 已拒收	255 ：无效订单")
    @TableField("order_status")
    private Integer orderStatus;

    @ApiModelProperty("订单取消类型")
    @TableField("cannel_type")
    private Integer cannelType;

    @ApiModelProperty("取消时间")
    @TableField("cannel_time")
    private LocalDateTime cannelTime;

    @ApiModelProperty("卖家编号")
    @TableField("seller_id")
    private String sellerId;

    @ApiModelProperty("父订单编号")
    @TableField("parent_order_id")
    private String parentOrderId;

    @ApiModelProperty("接入系统id")
    @TableField("business_order_id")
    private String businessOrderId;

    @ApiModelProperty("订单类型：1 一般类型 255 其他类型")
    @TableField("order_type")
    private Integer orderType;

    @ApiModelProperty("用户id")
    @TableField("user_id")
    private String userId;

    @ApiModelProperty("订单总金额")
    @TableField("total_amount")
    private BigDecimal totalAmount;

    @ApiModelProperty("支付金额")
    @TableField("pay_amount")
    private BigDecimal payAmount;

    @ApiModelProperty("支付方式 1.支付宝，2微信，3银联")
    @TableField("pay_type")
    private Integer payType;

    @ApiModelProperty("使用优惠券编号")
    @TableField("coupon_id")
    private String couponId;

    @ApiModelProperty("支付时间")
    @TableField("pay_time")
    private LocalDateTime payTime;

    @ApiModelProperty("支付订单截止时间")
    @TableField("expire_time")
    private LocalDateTime expireTime;

    @ApiModelProperty("支付方式代码")
    @TableField("payment_code")
    private String paymentCode;

    @ApiModelProperty("折扣金额")
    @TableField("discount_amount")
    private BigDecimal discountAmount;

    @ApiModelProperty("优惠券金额")
    @TableField("coupon_amount")
    private BigDecimal couponAmount;

    @ApiModelProperty("应付金额，订单总金额 - 促销活动折扣金额 - 优惠券抵扣金额 + 运费")
    @TableField("payable_amount")
    private BigDecimal payableAmount;

    @ApiModelProperty("发票抬头")
    @TableField("invoice_title")
    private String invoiceTitle;

    @ApiModelProperty("纳税人号")
    @TableField("taxpayer_id")
    private String taxpayerId;

    @ApiModelProperty("确认收货时间")
    @TableField("confirm_receipt_time")
    private LocalDateTime confirmReceiptTime;

    @ApiModelProperty("订单备注")
    @TableField("user_remark")
    private String userRemark;

    @ApiModelProperty("订单删除状态：0：未删除 1 已删除")
    @TableField("delete_status")
    private Integer deleteStatus;

    @ApiModelProperty("是否评论 0 未评论 1 已评论")
    @TableField("comment_stauts")
    private Integer commentStauts;

    @ApiModelProperty("运费")
    @TableField("freigth")
    private BigDecimal freigth;

    @ApiModelProperty("扩展信息")
    @TableField("ext_json")
    private String extJson;


    public static final String BUSINESS_IDENTIFIER = "business_identifier";

    public static final String ORDER_ID = "order_id";

    public static final String ORDER_STATUS = "order_status";

    public static final String CANNEL_TYPE = "cannel_type";

    public static final String CANNEL_TIME = "cannel_time";

    public static final String SELLER_ID = "seller_id";

    public static final String PARENT_ORDER_ID = "parent_order_id";

    public static final String BUSINESS_ORDER_ID = "business_order_id";

    public static final String ORDER_TYPE = "order_type";

    public static final String USER_ID = "user_id";

    public static final String TOTAL_AMOUNT = "total_amount";

    public static final String PAY_AMOUNT = "pay_amount";

    public static final String PAY_TYPE = "pay_type";

    public static final String COUPON_ID = "coupon_id";

    public static final String PAY_TIME = "pay_time";

    public static final String EXPIRE_TIME = "expire_time";

    public static final String PAYMENT_CODE = "payment_code";

    public static final String DISCOUNT_AMOUNT = "discount_amount";

    public static final String COUPON_AMOUNT = "coupon_amount";

    public static final String PAYABLE_AMOUNT = "payable_amount";

    public static final String INVOICE_TITLE = "invoice_title";

    public static final String TAXPAYER_ID = "taxpayer_id";

    public static final String CONFIRM_RECEIPT_TIME = "confirm_receipt_time";

    public static final String USER_REMARK = "user_remark";

    public static final String DELETE_STATUS = "delete_status";

    public static final String COMMENT_STAUTS = "comment_stauts";

    public static final String FREIGTH = "freigth";

    public static final String EXT_JSON = "ext_json";


}
