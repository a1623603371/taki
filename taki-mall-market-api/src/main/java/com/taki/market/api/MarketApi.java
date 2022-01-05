package com.taki.market.api;

import com.taki.common.utlis.ResponseData;
import com.taki.market.domain.dto.CalculateOrderAmountDTO;
import com.taki.market.request.CalculateOrderAmountRequest;

/**
 * @ClassName MarketApi
 * @Description 营销系统 API
 * @Author Long
 * @Date 2022/1/5 10:01
 * @Version 1.0
 */
public interface MarketApi {


    /** 
     * @description: 计算订单费用
     * @param calculateOrderAmountRequest 计算订单费用请求
     * @return 订单费用结果
     * @author Long
     * @date: 2022/1/5 10:19
     */ 
    ResponseData<CalculateOrderAmountDTO> calculateOrderAmount(CalculateOrderAmountRequest calculateOrderAmountRequest);
}
