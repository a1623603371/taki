package com.taki.market.api.impl;

import com.taki.common.utli.ResponseData;
import com.taki.market.api.PromotionApi;
import com.taki.market.domain.dto.SaveOrUpdatePromotionDTO;
import com.taki.market.domain.request.SaveOrUpdatePromotionRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @ClassName PromotionApiImpl
 * @Description 营销 促销活动 api
 * @Author Long
 * @Date 2022/9/26 21:58
 * @Version 1.0
 */
@DubboService
@Slf4j
public class PromotionApiImpl implements PromotionApi {


    @Override
    public ResponseData<SaveOrUpdatePromotionDTO> saveOrUpdatePromotion(SaveOrUpdatePromotionRequest saveOrUpdatePromotionRequest) {
        return null;
    }
}
