package com.taki.customer.converter;

import com.taki.customer.domain.enetity.CustomerReceivesAfterSaleInfoDO;
import com.taki.customer.domain.request.CustomerReceiveAfterSaleRequest;
import org.mapstruct.Mapper;

/**
 * @ClassName CustomerConverter
 * @Description TODO
 * @Author Long
 * @Date 2022/9/17 20:23
 * @Version 1.0
 */
@Mapper(componentModel = "spring")
public interface CustomerConverter {

    /*** 
     * @description: 对象转换
     * @param customerReceiveAfterSaleRequest
     * @return 对象
     * @author Long
     * @date: 2022/9/17 20:25
     */ 
    CustomerReceivesAfterSaleInfoDO converterCustomerReceivesAfterSaleInfoDO(CustomerReceiveAfterSaleRequest customerReceiveAfterSaleRequest);
}
