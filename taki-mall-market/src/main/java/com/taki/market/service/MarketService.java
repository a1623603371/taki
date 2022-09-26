package com.taki.market.service;

import com.taki.market.domain.dto.CalculateOrderAmountDTO;
import com.taki.market.domain.request.CalculateOrderAmountRequest;

/**
 * @ClassName MarketService
 * @Description 营销管理service接口
 * @Author Long
 * @Date 2022/2/18 15:45
 * @Version 1.0
 */
public interface MarketService {

    /** 
     * @description:  订单费用计算
     * @param calculateOrderAmountRequest 订单费用计算 请求
     * @return
     * @author Long
     * @date: 2022/2/18 15:46
     */ 
    CalculateOrderAmountDTO calculateOrderAmount(CalculateOrderAmountRequest calculateOrderAmountRequest);
}
