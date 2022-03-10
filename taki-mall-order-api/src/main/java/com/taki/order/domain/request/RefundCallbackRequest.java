package com.taki.order.domain.request;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName RefundCallbackRequest
 * @Description 支付系统 退款回调入参
 * @Author Long
 * @Date 2022/3/9 14:49
 * @Version 1.0
 */
@Data
public class RefundCallbackRequest extends AbstractObject implements Serializable {


    private static final long serialVersionUID = -9008011726256609313L;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     *退款批次号
     */
    private String batchOn;

    /**
     * 支付接口返回 退款 结果 10未退款 20退款成功 30退款失败
     */
    private Integer refundStatus;
}
