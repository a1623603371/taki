package com.taki.order.wms;

import com.taki.order.dao.OrderDeliveryDetailDao;
import com.taki.order.domain.dto.WmsShipDTO;
import com.taki.order.domain.entity.OrderDeliveryDetailDO;
import com.taki.order.domain.entity.OrderInfoDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName OrderDeliveryProcessor
 * @Description TODO
 * @Author Long
 * @Date 2022/4/6 15:59
 * @Version 1.0
 */
@Component
public class OrderDeliveryProcessor extends AbstractWmsShipResultProcessor{

    @Autowired
    private OrderDeliveryDetailDao orderDeliveryDetailDao;

    @Override
    protected void doExecute(WmsShipDTO wmsShipDTO, OrderInfoDO order) {
    // 增加 订单配送 的配送人员 信息
        OrderDeliveryDetailDO deliveryDetail = orderDeliveryDetailDao.getByOrderId(order.getOrderId());

        orderDeliveryDetailDao.updateDelivery(deliveryDetail.getId(),wmsShipDTO.getDelivererNo(),wmsShipDTO.getDeliverName(),wmsShipDTO.getDelivererPhone());


    }

    @Override
    protected boolean checkOrderStatus(OrderInfoDO order) {
        return false;
    }
}
