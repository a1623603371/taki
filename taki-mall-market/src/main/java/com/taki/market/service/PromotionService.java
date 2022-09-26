package com.taki.market.service;

import com.taki.market.domain.dto.SaveOrUpdatePromotionDTO;
import com.taki.market.domain.request.SaveOrUpdatePromotionRequest;

/**
 * @ClassName PromotionService
 * @Description 促销活动接口
 * @Author Long
 * @Date 2022/9/26 21:59
 * @Version 1.0
 */
public interface PromotionService {


    /*** 
     * @description: 新增/修改促销活动
     * @param saveOrUpdatePromotionRequest 新增/修改促销活动请求
     * @return  com.taki.market.domain.dto.SaveOrUpdatePromotionDTO
     * @author Long
     * @date: 2022/9/26 22:01
     */ 
    SaveOrUpdatePromotionDTO saveOrUpdatePromotion(SaveOrUpdatePromotionRequest  saveOrUpdatePromotionRequest);
}
