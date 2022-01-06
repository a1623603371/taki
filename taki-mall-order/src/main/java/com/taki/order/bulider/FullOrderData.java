package com.taki.order.bulider;

import com.taki.order.domin.entity.*;
import lombok.Data;

import java.util.List;

/**
 * @ClassName FullOrderData
 * @Description 全量订单数据
 * @Author Long
 * @Date 2022/1/6 11:04
 * @Version 1.0
 */
@Data
public class FullOrderData {

    /**
     * 订单信息
     */
    private OrderInfoDO orderInfo;

    /**
     *订单条目
     */
    private List<OrderItemDO> orderItems;

    /**
     * 订单配送信息
     */
    private OrderDeliveryDetailDO orderDeliveryDetailDO;


    /**
     * 订单支付信息
     */
    private List<OrderPaymentDetailDO> orderPaymentDetails;

    /**
     * 订单费用信息
     */
    private List<OrderAmountDO> orderAmounts;

    /**
     * 订单费用明细
     */
    private List<OrderAmountDetailDO> orderAmountDetails;

    /**
     * 订单操作日志
     */
    private OrderOperateLogDO orderOperateLog;

    /**
     * 订单快照数据
     */
    private List<OrderSnapshotDO> orderSnapshots;

}
