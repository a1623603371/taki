package com.taki.order.domain.request;

import com.taki.common.core.AbstractObject;
import com.taki.order.domain.dto.CancelOrderRefundAmountDTO;
import com.taki.order.domain.dto.OrderInfoDTO;
import com.taki.order.domain.dto.OrderItemDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName CancelOrderAssembleRequest
 * @Description 取消订单
 * @Author Long
 * @Date 2022/3/9 10:45
 * @Version 1.0
 */
@Data
public class CancelOrderAssembleRequest extends AbstractObject implements Serializable {

    // 订单信息
    private OrderInfoDTO orderInfo;

    // 订单条目
    private List<OrderItemDTO> orderItems;


    /**
     * 售后类型 1 退款 2， 退货
     */
    private Integer afterSaleType;

    /**
     *取消订单 退款金额 DTO
     */
    private CancelOrderRefundAmountDTO cancelOrderRefundAmount;

    /**
     * 售后 Id
     */
    private String afterSaleId;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 取消类型
     */
    private Integer cancelType;

    /**
     *售后支付单Id
     */
    private Long afterSaleRefundId;

    /**
     *当前订单是否退款最后一笔
     */
    private boolean lastReturnGoods = true;

}
