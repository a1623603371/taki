package com.taki.order.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
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
@Data
@Accessors(chain = true)
@TableName("order_info")
public class OrderInfoDO extends BaseEntity implements Serializable{

    private static final long serialVersionUID = 1L;

   //("接入方业务标识 1，自营商城")
    @TableField("business_identifier")
    private Integer businessIdentifier;

   //("订单编号")
    @TableField("order_id")
    private String orderId;

   //("订单状态 10：已创建，30 ：已履约，40：已出库 	50 ：已配送 60：已签收 70：已取消 100： 已拒收	255 ：无效订单")
    @TableField("order_status")
    private Integer orderStatus;

   //("订单取消类型")
    @TableField("cancel_type")
    private Integer cancelType;

   //("取消时间")
    @TableField("cancel_time")
    private LocalDateTime cancelTime;

  //("卖家编号")
    @TableField("seller_id")
    private String sellerId;

    //("父订单编号")
    @TableField("parent_order_id")
    private String parentOrderId;

    //("接入系统id")
    @TableField("business_order_id")
    private String businessOrderId;

   //("订单类型：1 一般类型 255 其他类型")
    @TableField("order_type")
    private Integer orderType;

    //("用户id")
    @TableField("user_id")
    private String userId;

    //("订单总金额")
    @TableField("total_amount")
    private BigDecimal totalAmount;

    //("支付金额")
    @TableField("pay_amount")
    private BigDecimal payAmount;

   //("支付方式 1.支付宝，2微信，3银联")
    @TableField("pay_type")
    private Integer payType;

   //("使用优惠券编号")
    @TableField("coupon_id")
    private String couponId;

    //("支付时间")
    @TableField("pay_time")
    private LocalDateTime payTime;

    //("支付订单截止时间")
    @TableField("expire_time")
    private LocalDateTime expireTime;


   //("订单备注")
    @TableField("user_remark")
    private String userRemark;

   //("订单删除状态：0：未删除 1 已删除")
    @TableField("delete_status")
    private Integer deleteStatus;

    //("是否评论 0 未评论 1 已评论")
    @TableField("comment_status")
    private Integer commentStatus;

   //("扩展信息")
    @TableField("ext_json")
    private String extJson;


    public static final String BUSINESS_IDENTIFIER = "business_identifier";

    public static final String ORDER_ID = "order_id";

    public static final String ORDER_STATUS = "order_status";

    public static final String CANCEL_TYPE = "cancel_type";

    public static final String CANCEL_TIME = "cancel_time";

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

    public static final String COMMENT_STATUS = "comment_status";

    public static final String FREIGTH = "freigth";

    public static final String EXT_JSON = "ext_json";


}
