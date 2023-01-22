package com.taki.market.converter;

import com.taki.market.domain.entity.MarketPromotionDO;
import com.taki.market.domain.request.SaveOrUpdatePromotionRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @ClassName PromotionConverter
 * @Description 促销 转换类
 * @Author Long
 * @Date 2022/9/29 21:33
 * @Version 1.0
 */
@Mapper(componentModel = "spring")
public interface PromotionConverter {

    /*** 
     * @description: 转换对象 
     * @param request  保存或修改促销请求
     * @return 营销促销实体
     * @author Long
     * @date: 2022/9/29 21:35
     */ 
    @Mapping(target = "rule",ignore = true)
    MarketPromotionDO convertPromotionDO(SaveOrUpdatePromotionRequest request);


}
