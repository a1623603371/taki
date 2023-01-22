package com.taki.market.api.impl;

import com.alibaba.fastjson.JSON;
import com.taki.common.utli.ResponseData;
import com.taki.market.api.PromotionApi;
import com.taki.market.domain.dto.SaveOrUpdatePromotionDTO;
import com.taki.market.domain.request.SaveOrUpdatePromotionRequest;
import com.taki.market.exception.MarketBizException;
import com.taki.market.service.PromotionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

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


    @Autowired
    private PromotionService promotionService;


    @Override
    public ResponseData<SaveOrUpdatePromotionDTO> saveOrUpdatePromotion(SaveOrUpdatePromotionRequest saveOrUpdatePromotionRequest) {

        try {

            return ResponseData.success(promotionService.saveOrUpdatePromotion(saveOrUpdatePromotionRequest));

        }catch (MarketBizException e){

            log.error("biz  error: request = {}", JSON.toJSONString(saveOrUpdatePromotionRequest),e );
            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());

        }catch (Exception e){
            log.error("system error: request = {}",JSON.toJSONString(saveOrUpdatePromotionRequest),e);

            return ResponseData.error(e.getMessage());
        }

    }
}
