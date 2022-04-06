package com.taki.order.wms;

import com.taki.common.enums.OrderStatusEnum;
import com.taki.order.dao.OrderDeliveryDetailDao;
import com.taki.order.domain.dto.WmsShipDTO;
import com.taki.order.domain.entity.OrderDeliveryDetailDO;
import com.taki.order.domain.entity.OrderInfoDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName OrderOutStockedProcessor
 * @Description 订单已出库物流结果 处理器
 * @Author Long
 * @Date 2022/4/6 15:36
 * @Version 1.0
 */
@Component
public class OrderOutStockedProcessor extends AbstractWmsShipResultProcessor{

    @Autowired
    private OrderDeliveryDetailDao orderDeliveryDetailDao;

    @Override
    protected void doExecute(WmsShipDTO wmsShipDTO, OrderInfoDO order) {
        //增加 订单配送的出库时间
        OrderDeliveryDetailDO orderDeliveryDetailDO = orderDeliveryDetailDao.getByOrderId(order.getOrderId());
        orderDeliveryDetailDao.updateOutStockTime(orderDeliveryDetailDO.getId(),wmsShipDTO.getOutStockTime());

    }

    @Override
    protected boolean checkOrderStatus(OrderInfoDO order) {
        OrderStatusEnum orderStatus = OrderStatusEnum.getByCode(order.getOrderStatus());

        if (!OrderStatusEnum.FULFILL.equals(orderStatus)){
            return false;
        }

        return true;
    }
}
