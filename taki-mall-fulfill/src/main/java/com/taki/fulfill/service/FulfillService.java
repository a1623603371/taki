package com.taki.fulfill.service;

import com.taki.fulfill.domain.request.ReceiveFulfillRequest;

/**
 * @ClassName FulfillService
 * @Description 履约 service
 * @Author Long
 * @Date 2022/5/15 17:35
 * @Version 1.0
 */
public interface FulfillService {


    /**
     * 创建 履约单
     * @param receiveFulFillRequest
     */
    void createFulfillOrder(ReceiveFulfillRequest receiveFulFillRequest);


    /**
     * 取消 履约单
     * @param orderId
     */
    void cancelFulfillOrder(String orderId);


    /**
     * 触发履约单
     * @param receiveFulFillRequest
     * @return
     */
    Boolean receiveOrderFulFill(ReceiveFulfillRequest receiveFulFillRequest);
}
