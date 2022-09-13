package com.taki.tms.converter;

import com.taki.tms.domain.entity.LogisticOrderDO;
import com.taki.tms.domain.request.SendOutRequest;
import org.mapstruct.Mapper;

/**
 * @ClassName TmsConverter
 * @Description TODO
 * @Author Long
 * @Date 2022/9/13 21:52
 * @Version 1.0
 */
@Mapper(componentModel = "spring")
public interface TmsConverter {
    
    /*** 
     * @description: 发送
     * @param request
     * @return  com.taki.tms.domain.entity.LogisticOrderDO
     * @author Long
     * @date: 2022/9/13 21:56
     */ 
    LogisticOrderDO converterLogisticOrderDO(SendOutRequest request);
}
