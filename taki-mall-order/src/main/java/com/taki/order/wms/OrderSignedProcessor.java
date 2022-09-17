package com.taki.order.wms;

import com.taki.common.enums.OrderStatusEnum;
import com.taki.order.dao.OrderDeliveryDetailDao;
import com.taki.order.domain.dto.WmsShipDTO;
import com.taki.order.domain.entity.OrderDeliveryDetailDO;
import com.taki.order.domain.entity.OrderInfoDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName OrderSignedProcessor
 * @Description 订单物流配送结果处理器
 * @Author Long
 * @Date 2022/4/6 15:04
 * @Version 1.0
 */
@Component
public class OrderSignedProcessor extends AbstractWmsShipResultProcessor {


    @Autowired
    private OrderDeliveryDetailDao orderDeliveryDetailDao;

    @Override
    protected void doExecute(WmsShipDTO wmsShipDTO, OrderInfoDO order) {
        // 增加订单配送表的签收时间
        OrderDeliveryDetailDO deliveryDetail = orderDeliveryDetailDao.getByOrderId(order.getOrderId());
        orderDeliveryDetailDao.save(deliveryDetail);
    }

    @Override
    protected boolean checkOrderStatus(OrderInfoDO order) {
        OrderStatusEnum orderStatusEnum  = OrderStatusEnum.getByCode(order.getOrderStatus());

        if (OrderStatusEnum.DELIVERY.equals(orderStatusEnum)){
            return false;
        }
        return true;
    }
}
