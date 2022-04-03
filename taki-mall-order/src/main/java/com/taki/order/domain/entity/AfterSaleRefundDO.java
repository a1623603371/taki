package com.taki.order.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.core.AbstractObject;
import com.taki.common.domin.BaseEntity;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName AfterSaleRefundDO
 * @Description 售后退款
 * @Author Long
 * @Date 2022/3/11 14:48
 * @Version 1.0
 */
@Data
@TableName("after_sale_refund")
@Builder
public class AfterSaleRefundDO extends BaseEntity implements Serializable {


    private static final long serialVersionUID = 7386675197453886213L;

    /**
     * 售后Id
     */
    private Long afterSaleId;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     *售后批次号
     */
    private String afterSaleBatchNo;

    /**
     * 账号类型
     */
    private Integer accountType;

    /**
     * 支付类型
     */
    private Integer payType;

    /**
     * 退款状态
     */
    private Integer refundStatus;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 退款支付时间
     */
    private LocalDateTime refundPayTime;

    /**
     *交易单号
     */
    private String outTradeNo;

    /**
     * 备注
     */
    private String remark;


    public static  final String AFTER_SALE_ID = "after_sale_id";



}
