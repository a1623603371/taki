package com.taki.order.bulider;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.taki.common.utli.ObjectUtil;
import com.taki.order.domain.dto.*;
import com.taki.order.domain.entity.*;
import org.apache.commons.lang3.ObjectUtils;


import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName OrderDetailBuilder
 * @Description 订单详情构造器
 * @Author Long
 * @Date 2022/3/3 22:37
 * @Version 1.0
 */
public class OrderDetailBuilder {

    private OrderDetailDTO orderDetail = new OrderDetailDTO();

    public OrderDetailBuilder orderInfo(OrderInfoDO orderInfoDO){
        if (ObjectUtils.isNotEmpty(orderInfoDO)){
            orderDetail = orderInfoDO.clone(OrderDetailDTO.class);
        }
        return this;
    }

    public OrderDetailBuilder orderItems(List<OrderItemDO> orderItemDOS){
        if (CollectionUtils.isNotEmpty(orderItemDOS)){
            orderDetail.setOrderItems(ObjectUtil.convertList(orderItemDOS, OrderItemDTO
            .class));
        }

        return this;
    }

    public OrderDetailBuilder  orderAmountDetails(List<OrderAmountDetailDO> orderAmountDetails){
        if (CollectionUtils.isNotEmpty(orderAmountDetails)){
            orderDetail.setOrderAmountDetails(ObjectUtil.convertList(orderAmountDetails, OrderAmountDetailDTO
                    .class));
        }

        return this;
    }

    public OrderDetailBuilder orderDeliveryDetail(OrderDeliveryDetailDO orderDeliveryDetail){
        if (ObjectUtils.isNotEmpty(orderDeliveryDetail)){
            orderDetail.setOrderDeliverDetail(orderDeliveryDetail.clone(OrderDeliverDetailDTO.class));
        }

        return this;
    }

    public  OrderDetailBuilder  orderPaymentDetail(List<OrderPaymentDetailDO> orderPaymentDetails){
        if (CollectionUtils.isNotEmpty(orderPaymentDetails)){
            orderDetail.setOrderPaymentDetails(ObjectUtil.convertList(orderPaymentDetails, OrderPaymentDetailDTO.class));
        }

        return this;
    }

    public OrderDetailBuilder   orderAmounts(List<OrderAmountDO> orderAmounts){
        if (CollectionUtils.isNotEmpty(orderAmounts)){
            orderDetail.setOrderAmounts(orderAmounts.stream().collect(
                    Collectors.toMap(OrderAmountDO::getAmountType, OrderAmountDO::getAmount, (v1, v2) -> v1)));
        }

        return this;
    }

    public OrderDetailBuilder orderOperateLogs(List<OrderOperateLogDO> orderOperateLogs){
        if (CollectionUtils.isNotEmpty(orderOperateLogs)){
            orderDetail.setOrderOperateLogs(ObjectUtil.convertList(orderOperateLogs,OrderOperateLogDTO.class));
        }

        return this;

    }

    public OrderDetailBuilder orderSnapshots(List<OrderSnapshotDO> orderSnapshots){
        if (CollectionUtils.isNotEmpty(orderSnapshots)){
            orderDetail.setOrderSnapshots(ObjectUtil.convertList(orderSnapshots,OrderSnapshotDTO.class));
        }

        return this;


    }

    public OrderDetailBuilder lackItems(List<OrderLackItemDTO> lackItems) {
        if (CollectionUtils.isNotEmpty(lackItems)){
            orderDetail.setOrderLackItems(lackItems);
        }
        return this;
    }

    public OrderDetailDTO build(){
        return orderDetail;
    }
}
