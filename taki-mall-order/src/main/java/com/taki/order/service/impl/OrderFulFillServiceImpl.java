package com.taki.order.service.impl;

import com.taki.common.bean.SpringApplicationContext;
import com.taki.common.enums.AmountTypeEnum;
import com.taki.common.enums.OrderStatusChangEnum;
import com.taki.common.enums.OrderStatusEnum;
import com.taki.common.utlis.ResponseData;
import com.taki.fulfill.api.FulFillApi;
import com.taki.fulfill.domain.request.ReceiveFulFillRequest;
import com.taki.fulfill.domain.request.ReceiveOrderItemRequest;
import com.taki.order.dao.*;
import com.taki.order.domain.dto.WmsShipDTO;
import com.taki.order.domain.entity.OrderAmountDO;
import com.taki.order.domain.entity.OrderDeliveryDetailDO;
import com.taki.order.domain.entity.OrderInfoDO;
import com.taki.order.domain.entity.OrderItemDO;
import com.taki.order.enums.OrderCancelTypeEnum;
import com.taki.order.exception.OrderBizException;
import com.taki.order.exception.OrderErrorCodeEnum;
import com.taki.order.service.OrderFulFillService;
import com.taki.order.wms.OrderDeliveryProcessor;
import com.taki.order.wms.OrderOutStockedProcessor;
import com.taki.order.wms.OrderSignedProcessor;
import com.taki.order.wms.OrderWmsShipResultProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName OrderFulFillServiceImpl
 * @Description 订单履约相关
 * @Author Long
 * @Date 2022/4/6 14:52
 * @Version 1.0
 */
@Service
@Slf4j
public class OrderFulFillServiceImpl implements OrderFulFillService {



    @Autowired
    private SpringApplicationContext springApplicationContext;

    @Autowired
    private OrderInfoDao orderInfoDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private OrderAmountDao orderAmountDao;

    @Autowired
    private OrderDeliveryDetailDao orderDeliveryDetailDao;

    @Autowired
    private OrderOperateLogDao orderOperateLogDao;

    @Autowired
    private OrderOperateLogFactory orderOperateLogFactory;

    @Override
    public void informOrderWmsShipResult(WmsShipDTO wmsShipDTO) {

        // 1.获取对应的订单物流配送结果处理器
        OrderWmsShipResultProcessor processor = getProcessor(wmsShipDTO.getStatusChang());

        if (!ObjectUtils.isEmpty(processor)){
            processor.execute(wmsShipDTO);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void triggerOrderFulfill(String orderId) {
        // 1.查询订单
        OrderInfoDO orderInfoDO = orderInfoDao.getByOrderId(orderId);

        if (ObjectUtils.isEmpty(orderInfoDO)){
            return;
        }

        //2. 效验订单是否支付
        OrderStatusEnum orderStatusEnum =  OrderStatusEnum.getByCode(orderInfoDO.getOrderStatus());
        if (OrderStatusEnum.PAID.equals(orderStatusEnum)){
            log.info("order  has  not  bean paid ,cannot fulfill,orderId={}",orderId);
            return;
        }
        //3.更新订单状态 为：“已履约”
        orderInfoDao.updateOrderStatus(orderId,OrderStatusEnum.PAID.getCode(), OrderStatusEnum.FULFILL.getCode());

        // 4. 并 插入一天 订单变更记录
        orderOperateLogDao.save(orderOperateLogFactory.get(orderInfoDO,OrderStatusChangEnum.ORDER_FULFILLED));

    }



    /** 
     * @description: 构造接受订单履约请求
     * @param orderInfoDO 订单信息
     * @return
     * @author Long
     * @date: 2022/4/6 16:34
     */ 
    @Override
    public ReceiveFulFillRequest builderReceiveFulFillRequest(OrderInfoDO orderInfoDO) {
        OrderDeliveryDetailDO orderDeliveryDetail = orderDeliveryDetailDao.getByOrderId(orderInfoDO.getOrderId());

        List<OrderItemDO> orderItems = orderItemDao.listByOrderId(orderInfoDO.getOrderId());

        OrderAmountDO deliveryAmount = orderAmountDao.getByIdAndAmountType(orderInfoDO.getOrderId(), AmountTypeEnum.SHIPPING_AMOUNT.getCode());

        //构造请求
        ReceiveFulFillRequest request = ReceiveFulFillRequest.builder()
                .orderId(orderInfoDO.getOrderId())
                .sellerId(orderInfoDO.getSellerId())
                .userId(orderInfoDO.getUserId())
                .deliveryType(orderDeliveryDetail.getDeliveryType())
                .receiverName(orderDeliveryDetail.getReceiverName())
                .receiverPhone(orderDeliveryDetail.getReceiverPhone())
                .receiverProvince(orderDeliveryDetail.getProvince())
                .receiverCity(orderDeliveryDetail.getCity())
                .receiverArea(orderDeliveryDetail.getArea())
                .receiverStreetAddress(orderDeliveryDetail.getDetailAddress())
                .receiverDetailAddress(orderDeliveryDetail.getDetailAddress())
                .receiverLat(orderDeliveryDetail.getLat())
                .receiverLon(orderDeliveryDetail.getLon())
                .payType(orderInfoDO.getPayType())
                .payAmount(orderInfoDO.getPayAmount())
                .totalAmount(orderInfoDO.getTotalAmount())
                .receiveOrderItems(buildReceiveOrderItemRequest(orderItems))
                .build();


        if (ObjectUtils.isNotEmpty(deliveryAmount)){
            request.setDeliveryAmount(deliveryAmount.getAmount());
        }

        return request;
    }
    /**
     * @description: 构造订单条目 消息
     * @param
     * @param orderItems 订单条目
     * @return
     * @author Long
     * @date: 2022/4/6 16:05
     */
    private List<ReceiveOrderItemRequest> buildReceiveOrderItemRequest( List<OrderItemDO> orderItems) {
        List<ReceiveOrderItemRequest> receiveOrderItemRequests = new ArrayList<>();

        orderItems.forEach(orderItemDO -> {
            ReceiveOrderItemRequest request = ReceiveOrderItemRequest.builder()
                    .skuCode(orderItemDO.getSkuCode())
                    .productName(orderItemDO.getProductName())
                    .salePrice(orderItemDO.getSalePrice())
                    .saleQuantity(orderItemDO.getSaleQuantity())
                    .payAmount(orderItemDO.getPayAmount())
                    .originAmount(orderItemDO.getOriginAmount())
                    .build();
            receiveOrderItemRequests.add(request);
        });

        return receiveOrderItemRequests;

    }

    /**
     * @description: 获取订单物流配送处理器
     * @param statusChang
     * @return
     * @author Long
     * @date: 2022/4/6 16:05
     */
    private OrderWmsShipResultProcessor getProcessor(OrderStatusChangEnum statusChang) {
        if (OrderStatusChangEnum.ORDER_OUT_STOCKED.equals(statusChang)){
            return springApplicationContext.getBean(OrderOutStockedProcessor.class);
        }else if (OrderStatusChangEnum.ORDER_DELIVERED.equals(statusChang)){
            return springApplicationContext.getBean(OrderDeliveryProcessor.class);

        }else if (OrderStatusChangEnum.ORDER_SIGNED.equals(statusChang)){
            return springApplicationContext.getBean(OrderSignedProcessor.class);
        }
        return null;
    }

}
