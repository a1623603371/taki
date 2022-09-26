package com.taki.market.converter;

import com.taki.market.domain.dto.CalculateOrderAmountDTO;
import com.taki.market.domain.request.CalculateOrderAmountRequest;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @ClassName MarketConverter
 * @Description TODO
 * @Author Long
 * @Date 2022/9/17 18:07
 * @Version 1.0
 */
@Mapper(componentModel = "spring")
public interface MarketConverter {

    /*** 
     * @description:  转换对象
     * @param orderAmountRequest 对象
     * @return
     * @author Long
     * @date: 2022/9/17 18:11
     */ 
    CalculateOrderAmountDTO.OrderAmountDTO converterOrderAmountRequest(CalculateOrderAmountRequest.OrderAmountRequest orderAmountRequest);

    /***
     * @description:  转换对象
     * @param orderAmountRequests 对象
     * @return
     * @author Long
     * @date: 2022/9/17 18:11
     */
    List<CalculateOrderAmountDTO.OrderAmountDTO> converterOrderAmountRequest(List<CalculateOrderAmountRequest.OrderAmountRequest> orderAmountRequests);
}
