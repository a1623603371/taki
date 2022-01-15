package com.taki.order.api;

import com.taki.common.utlis.ResponseData;
import com.taki.order.domian.dto.CreateOrderDTO;
import com.taki.order.domian.dto.GenOrderIdDTO;
import com.taki.order.domian.request.CreateOrderRequest;
import com.taki.order.domian.request.GenOrderIdRequest;


/**
 * @ClassName OrderApi
 * @Description 订单中心 - 正向订单接口
 * @Author Long
 * @Date 2022/1/15 13:21
 * @Version 1.0
 */
public interface OrderApi {

    /**
     * @description: 生成订单Id
     * @param genOrderIdRequest 生成订单Id
     * @return  订单Id 信息
     * @author Long
     * @date: 2022/1/15 14:08
     */
    ResponseData<GenOrderIdDTO> genOrderId(GenOrderIdRequest genOrderIdRequest);

    /**
     * @description: 创建订单
     * @param createOrderRequest 创建订单请求
     * @return  订单信息
     * @author Long
     * @date: 2022/1/15 14:11
     */
    ResponseData<CreateOrderDTO> createOrder(CreateOrderRequest createOrderRequest);
}
