package com.taki.order.service;

import com.taki.order.domain.dto.CreateOrderDTO;
import com.taki.order.domain.dto.GenOrderIdDTO;
import com.taki.order.domain.dto.PrePayOrderDTO;
import com.taki.order.domain.request.*;

import java.util.List;

/**
 * @ClassName OrderInfoService
 * @Description 订单 接口
 * @Author Long
 * @Date 2022/1/2 16:50
 * @Version 1.0
 */
public interface OrderInfoService {



    /**
     * @description: 生成订单Id
     * @param genOrderIdRequest 生成订单请求
     * @return  GenOrderIdDTO
     * @author Long
     * @date: 2022/1/2 19:15
     */
    GenOrderIdDTO getGenOrderIdDTO(GenOrderIdRequest genOrderIdRequest);

    /** 
     * @description: 创建订单
     * @param createOrderRequest 创建订单请求
     * @return  CreateOrderDTO
     * @author Long
     * @date: 2022/1/3 0:13
     */ 
    CreateOrderDTO createOrder(CreateOrderRequest createOrderRequest);

    /**
     * @description: 预支付订单
     * @param prePayOrderRequest 预支付订单请求
     * @return 预支付订单信息
     * @author Long
     * @date: 2022/1/15 14:44
     */
    PrePayOrderDTO preOrder(PrePayOrderRequest prePayOrderRequest);

    /**
     * @description: 支付回调
     * @param payCallbackRequest 支付回调请求
     * @return  void
     * @author Long
     * @date: 2022/1/17 16:08
     */
    void payCallback(PayCallbackRequest payCallbackRequest);


    /** 
     * @description: 移除订单
     * @param orderIds 订单Id 集合
     * @return  操作结果
     * @author Long
     * @date: 2022/2/18 9:56
     */ 
    Boolean removeOrders(List<String>  orderIds);


    /** 
     * @description:  调整订单配送地址
     * @param 
     * @return
     * @author Long
     * @date: 2022/2/18 9:57
     */ 
    Boolean adjustDeliveryAddress(AdjustDeliveryAddressRequest adjustDeliveryAddressRequest);

}
