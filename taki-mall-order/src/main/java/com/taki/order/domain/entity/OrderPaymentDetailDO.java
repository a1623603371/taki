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
 * 订单⽀付明细表
 * </p>
 *
 * @author long
 * @since 2022-01-02
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("order_payment_detail")
public class OrderPaymentDetailDO extends BaseEntity implements Serializable{

    private static final long serialVersionUID = 1L;

   //("订单编号")
    @TableField("order_id")
    private String orderId;

    //("账户类型")
    @TableField("account_type")
    private Integer accountType;

   //("⽀付类型 10:微信⽀付, 20:⽀付宝⽀	付")
    @TableField("pay_type")
    private Integer payType;

   //("⽀付状态 10:未⽀付,20:已⽀付")
    @TableField("pay_status")
    private Integer payStatus;

    //("⽀付⾦额")
    @TableField("pay_amount")
    private BigDecimal payAmount;

    //("⽀付时间")
    @TableField("pay_time")
    private LocalDateTime payTime;

    //("⽀付系统交易流⽔号")
    @TableField("out_trade_no")
    private String outTradeNo;

   //("⽀付备注信息")
    @TableField("pay_remark")
    private String payRemark;



    public static final String ORDER_ID = "order_id";

    public static final String ACCOUNT_TYPE = "account_type";

    public static final String PAY_TYPE = "pay_type";

    public static final String PAY_STATUS = "pay_status";

    public static final String PAY_AMOUNT = "pay_amount";

    public static final String PAY_TIME = "pay_time";

    public static final String OUT_TRADE_NO = "out_trade_no";

    public static final String PAY_REMARK = "pay_remark";





}
