package com.taki.pay.service;

/**
 * @ClassName PayService
 * @Description 支付 service 组件
 * @Author Long
 * @Date 2022/5/18 18:12
 * @Version 1.0
 */
public interface PayService {
    /*** 
     * @description:  实时 查询支付交易流水号
     * @param orderId 订单号Id
     * @param  businessIdentifier 业务标识线
     * @return
     * @author Long
     * @date: 2022/5/18 18:29
     */ 
    Boolean getTradeNoByRealTime(String orderId, Integer businessIdentifier);
}
