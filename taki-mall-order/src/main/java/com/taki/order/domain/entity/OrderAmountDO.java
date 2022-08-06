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
 * 订单费⽤表
 * </p>
 *
 * @author long
 * @since 2022-01-02
 */
@Getter
@Setter
@TableName("order_amount")
public class OrderAmountDO extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    //"订单编号")
    @TableField("order_id")
    private String orderId;

    //("收费类型")
    @TableField("amount_type")
    private Integer amountType;

    //("收费⾦额")
    @TableField("amount")
    private BigDecimal amount;




    public static final String ORDER_ID = "order_id";

    public static final String AMOUNT_TYPE = "amount_type";

    public static final String AMOUNT = "amount";



}
