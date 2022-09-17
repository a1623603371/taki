package com.taki.order.domain.dto;

import com.taki.common.core.AbstractObject;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * @ClassName OrderDetailDTO
 * @Description 订单详情
 * @Author Long
 * @Date 2022/3/2 22:13
 * @Version 1.0
 */
@Data
@Builder
public class OrderDetailDTO  implements Serializable {


    private static final long serialVersionUID = 5003298129747250976L;

    /**
     * 订单
     */
    private OrderInfoDTO orderInfo;

    /**
     * 订单条目
     */
    private List<OrderItemDTO> orderItems;

    /**
     * 订单费用详情
     */
    private List<OrderAmountDetailDTO> orderAmountDetails;


    /**
     * 订单配送 信息表
     */
    private OrderDeliveryDetailDTO orderDeliveryDetail;


    /**
     * 订单支付信息
     */
    private List<OrderPaymentDetailDTO> orderPaymentDetails;

    /**
     * 费用类型
     */
    private Map<Integer, BigDecimal> orderAmounts;

    /**
     * 订单操作日志
     */
    private List<OrderOperateLogDTO> orderOperateLogs;

    /**
     * 订快照
     */
    private List<OrderSnapshotDTO> orderSnapshots;

    /**
     * 订单缺品信息
     */
    private List<OrderLackItemDTO> orderLackItems;


}
