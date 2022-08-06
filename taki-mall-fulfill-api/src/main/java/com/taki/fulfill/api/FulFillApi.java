package com.taki.fulfill.api;

import com.taki.common.utli.ResponseData;
import com.taki.fulfill.domain.request.CancelFulfillRequest;
import com.taki.fulfill.domain.request.ReceiveFulFillRequest;
import com.taki.fulfill.domain.request.TriggerOrderWmsShipEventRequest;


/**
 * @ClassName FulFillApi
 * @Description 履约系统业务接口
 * @Author Long
 * @Date 2022/3/4 10:49
 * @Version 1.0
 */
public interface FulFillApi {
    
    /** 
     * @description:  接收订单履约
     * @param
     * @return request 接收订单履约请求参数
     *
     * @author Long
     * @date: 2022/3/4 10:51
     */ 
    ResponseData<Boolean> receiveOrderFulFill(ReceiveFulFillRequest request);


    /**
     * @description: 触发订单物流配送结果事件接口
     * @param request 订单物流配送结果事件请求参数

     * @return  结果
     * @author Long
     * @date: 2022/3/4 11:55
     */
    ResponseData<Boolean> triggerOrderWmsShipEvent(TriggerOrderWmsShipEventRequest request);


    /** 
     * @description:  履约通知停止配送
     * @param cancelFulfillRequest 履约通知停止配送 请求
     * @return
     * @author Long
     * @date: 2022/3/4 11:57
     */ 
    ResponseData<Boolean> cancelFulfill(CancelFulfillRequest cancelFulfillRequest);

}

