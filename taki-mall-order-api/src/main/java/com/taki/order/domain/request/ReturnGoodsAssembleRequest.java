package com.taki.order.domain.request;

import com.taki.common.core.AbstractObject;
import com.taki.order.domain.dto.AfterSaleOrderItemDTO;
import com.taki.order.domain.dto.OrderInfoDTO;
import com.taki.order.domain.dto.OrderItemDTO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName ReturnGoodsAssembleRequest
 * @Description 退款商品集合
 * @Author Long
 * @Date 2022/4/3 20:15
 * @Version 1.0
 */
@Data
public class ReturnGoodsAssembleRequest extends AbstractObject implements Serializable {


    private static final long serialVersionUID = -4875513362465338968L;


    /**
     * 实际退款 金额
     */
    private BigDecimal returnGoodsAmount;


    /**
     *sku编码
     */
    private String skuCode;

    /**
     * 退货数量
     */
    private Integer returnNum;

    /***
     * 申请退款金额
     */
    private    BigDecimal applyRefundAmount;

    /**
     * 订单条目列表
     */
    private List<AfterSaleOrderItemDTO> afterSaleOrderItems;

    /**
     * 用户Id
     */
    private String userId;
    /**
     * 当前订单是否是退最后一笔
     */
    private boolean lastReturnGoods = false;

    /**
     * 订单id
     */
    private String orderId;
    /**
     * 订单信息
     */
    private OrderInfoDTO orderInfoDTO;

    /**
     * 商品条目集合
     */
    private List<OrderItemDTO> orderItems;

    /**
     * 售后类型 1退款 2退货
     */
    private Integer afterSaleType;

    /**
     * 售后Id
     */
    private String afterSaleId;

    /**
     * 售后支付单Id
     */
    private Long afterSaleRefundId;

    /**
     * 售后单状态
     */
    private Integer afterSaleStatus;

    /**
     *执行售后退货时，本次售后退货的条目
     */
    private List<OrderItemDTO> refundOrderItems;
}
