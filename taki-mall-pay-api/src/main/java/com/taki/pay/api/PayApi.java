package com.taki.pay.api;

import com.taki.common.utlis.ResponseData;
import com.taki.pay.domian.dto.PayOrderDTO;
import com.taki.pay.domian.rquest.PayOrderRequest;

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
}
