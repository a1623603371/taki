package com.taki.fulfill.service;

import com.taki.common.enums.OrderStatusChangEnum;
import com.taki.fulfill.domain.request.TriggerOrderWmsShipEventRequest;

/**
 * @ClassName OrderWmsShipEventProcessor
 * @Description 订单物流配送结果 处理器
 * @Author Long
 * @Date 2022/5/15 15:04
 * @Version 1.0
 */
public interface OrderWmsShipEventProcessor {

    /**
     * @description: 执行 业务流程
     * @param request 执行订单物流配送请求
     * @return  void
     * @author Long
     * @date: 2022/5/15 15:24
     */
    void execute(TriggerOrderWmsShipEventRequest request);

}
