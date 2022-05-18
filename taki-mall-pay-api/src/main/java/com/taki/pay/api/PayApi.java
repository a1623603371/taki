package com.taki.pay.api;

import com.taki.common.utlis.ResponseData;
import com.taki.pay.domian.dto.PayOrderDTO;
import com.taki.pay.domian.rquest.PayOrderRequest;
import com.taki.pay.domian.rquest.PayRefundRequest;

/**
 * @ClassName PayApi
 * @Description  支付API
 * @Author Long
 * @Date 2022/1/16 14:01
 * @Version 1.0
 */
public interface PayApi {
    /**
     * @description: 支付订单
     * @param payOrderRequest 支付订单请求参数封装
     * @return  支付结果
     * @author Long
     * @date: 2022/1/16 15:28
     */
    ResponseData<PayOrderDTO> payOrder(PayOrderRequest payOrderRequest);





    /***
     * @description: 执行退款操作
     * @param payRefundRequest 支付退款请求
     * @return 执行结果
     * @author Long
     * @date: 2022/1/18 16:23
     */
    ResponseData< Boolean> executeRefund(PayRefundRequest payRefundRequest);


    /** 
     * @description:  查询支付订单 流水单好
     * @param orderId 订单Id
     * @param businessIdentifier 业务标识线
     * @return
     * @author Long
     * @date: 2022/5/18 18:19
     */ 
    ResponseData< Boolean> getTradeNoByRealTime(String orderId,Integer businessIdentifier);
}
