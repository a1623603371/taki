package com.taki.customer.domain.request;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName CustomReviewReturnGoodsRequest
 * @Description 客服审核退款申请入参
 * @Author Long
 * @Date 2022/3/9 14:55
 * @Version 1.0
 */
@Data
public class CustomReviewReturnGoodsRequest extends AbstractObject implements Serializable {


    private static final long serialVersionUID = 3753644664798072018L;


    /**
     * 售后Id
     */
    private Long afterSaleId;

    /**
     * 客服Id
     */
    private String customerId;

    /**
     * 审核结果 1 审核通过 2审核拒绝
     */
    private Integer auditResult;

    /**
     * 售后支付单Id
     */
    private String afterSaleRefundId;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 客户审核结果 描述信息
     */
    private String auditResultDesc;
}
