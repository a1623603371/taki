package com.taki.order.service.impl;

import com.taki.order.domin.entity.*;
import lombok.Data;

import java.util.List;

/**
 * @ClassName NewOrderDataHolder
 * @Description 新订单信息数据（包含主订单和子订单）
 * @Author Long
 * @Date 2022/1/6 10:47
 * @Version 1.0
 */
@Data
public class NewOrderDataHolder {

    /**
     * 订单信息
     */
    private List<OrderInfoDO> orderInfos;

    /**
     * 订单条目信息
     */
    private List<OrderItemDO> orderItems;

    /**
     *订单配送信息
     */
    private List<OrderDeliveryDetailDO> orderDeliveryDetails;

    /**
     * 订单支付信息
     */
    private List<OrderPaymentDetailDO> orderPaymentDetails;
    /**
     * 订单费用信息
     */
    private List<OrderAmountDO> orderAmounts;

    /**
     * 订单费用详情信息
     */
    private List<OrderAmountDetailDO> orderAmountDetails;

    /**
     * 订单操作日志
     */
    private List<OrderOperateLogDO> orderOperateLogs;

    /**
     *订单快照数据
     */
    private List<OrderSnapshotDO> orderSnapshots;






}
