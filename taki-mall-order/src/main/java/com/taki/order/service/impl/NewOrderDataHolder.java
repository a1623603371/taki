package com.taki.order.service.impl;

import com.taki.order.bulider.FullOrderData;
import com.taki.order.domin.entity.*;
import lombok.Data;

import java.awt.font.TextHitInfo;
import java.util.ArrayList;
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
    private List<OrderInfoDO> orderInfos = new ArrayList<>();

    /**
     * 订单条目信息
     */
    private List<OrderItemDO> orderItems = new ArrayList<>();

    /**
     *订单配送信息
     */
    private List<OrderDeliveryDetailDO> orderDeliveryDetails = new ArrayList<>();

    /**
     * 订单支付信息
     */
    private List<OrderPaymentDetailDO> orderPaymentDetails = new ArrayList<>();
    /**
     * 订单费用信息
     */
    private List<OrderAmountDO> orderAmounts = new ArrayList<>();

    /**
     * 订单费用详情信息
     */
    private List<OrderAmountDetailDO> orderAmountDetails = new ArrayList<>();

    /**
     * 订单操作日志
     */
    private List<OrderOperateLogDO> orderOperateLogs = new ArrayList<>();

    /**
     *订单快照数据
     */
    private List<OrderSnapshotDO> orderSnapshots = new ArrayList<>();


    public void appendOrderData(FullOrderData fullOrderData) {
        this.orderInfos.add(fullOrderData.getOrderInfo());
        this.orderItems.addAll(fullOrderData.getOrderItems());
        this.orderDeliveryDetails.add(fullOrderData.getOrderDeliveryDetailDO());
        this.orderPaymentDetails.addAll(fullOrderData.getOrderPaymentDetails());
        this.orderAmounts.addAll(fullOrderData.getOrderAmounts());
        this.orderAmountDetails.addAll(fullOrderData.getOrderAmountDetails());
        this.orderOperateLogs.add(fullOrderData.getOrderOperateLog());
        this.orderSnapshots.addAll(fullOrderData.getOrderSnapshots());

    }
}
