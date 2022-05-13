package com.taki.order.service;

import com.taki.fulfill.domain.request.ReceiveFulFillRequest;
import com.taki.order.domain.dto.WmsShipDTO;
import com.taki.order.domain.entity.OrderInfoDO;

/**
 * @ClassName OrderFulFillService
 * @Description 订单履约相关
 * @Author Long
 * @Date 2022/4/6 14:25
 * @Version 1.0
 */
public interface OrderFulFillService {

    /**
     * @description: 通知 物流中心进行配送商品
     * @param wmsShipDTO 物流配送结果请求信息
     * @return  void
     * @author Long
     * @date: 2022/4/6 14:51
     */
    void informOrderWmsShipResult(WmsShipDTO wmsShipDTO);

    /**
     * @description: 执行订单履约
     * @param orderId 订单Id
     * @return  void
     * @author Long
     * @date: 2022/4/6 16:20
     */
    void triggerOrderFulfill(String orderId);

    /**
     * @description:  构造 执行定履约 请求
     * @param orderInfoDO 订单信息
     * @return
     * @author Long
     * @date: 2022/5/13 17:31
     */
    ReceiveFulFillRequest builderReceiveFulFillRequest(OrderInfoDO orderInfoDO);
}
