package com.taki.order.service;

import com.taki.order.domin.dto.GenOrderIdDTO;
import com.taki.order.domin.request.GenOrderIdRequest;

import java.lang.reflect.GenericArrayType;

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

}
