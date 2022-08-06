package com.taki.order.api;

import com.taki.common.utli.ResponseData;
import com.taki.order.domain.dto.*;
import com.taki.order.domain.request.RemoveOrderRequest;
import com.taki.order.domain.request.*;


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
    
    
    /** 
     * @description: 预支付
     * @param payOrderRequest 预支付订单请求
     * @return 预支付 信息
     * @author Long
     * @date: 2022/2/16 15:46
     */ 
    ResponseData<PrePayOrderDTO> prePayOrder(PrePayOrderRequest payOrderRequest);


    /**
     * @description: 支付回调
     * @param payCallbackRequest 支付回调请求
     * @return  支付结果
     * @author Long
     * @date: 2022/2/16 16:05
     */
    ResponseData<Boolean> payCallback(PayCallbackRequest payCallbackRequest);

    /**
     * @description: 移除订单
     * @param removeOrderRequest 移除订单 请求
     * @return 移除订单信息
     * @author Long
     * @date: 2022/2/26 17:45
     */
    ResponseData<RemoveOrderDTO> removeOrder(RemoveOrderRequest removeOrderRequest);


    /**
     * @description:  调整订单地址
     * @param adjustDeliveryAddressRequest 调整订单地址请求
     * @return  调整订单地址请求结果
     * @author Long
     * @date: 2022/2/26 19:48
     */
    ResponseData<AdjustDeliveryAddressDTO> adjustDeliveryAddress(AdjustDeliveryAddressRequest adjustDeliveryAddressRequest);

}
