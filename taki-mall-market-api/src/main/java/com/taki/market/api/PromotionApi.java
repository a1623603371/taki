package com.taki.market.api;

import com.taki.common.utli.ResponseData;
import com.taki.market.domain.dto.ReceiveCouponDTO;
import com.taki.market.domain.dto.SaveOrUpdatePromotionDTO;
import com.taki.market.domain.dto.SendCouponDTO;
import com.taki.market.domain.request.ReceiveCouponRequest;
import com.taki.market.domain.request.SaveOrUpdatePromotionRequest;
import com.taki.market.domain.request.SendCouponRequest;

/**
 * @ClassName PromotionApi
 * @Description 营销系统促销 API
 * @Author Long
 * @Date 2022/9/25 21:05
 * @Version 1.0
 */
public interface PromotionApi {


    /***
     * @description:  保存 或更新 促销活动
     * @param saveOrUpdatePromotionRequest 保存 或更新 请求 参数
     * @return
     * @author Long
     * @date: 2022/9/25 21:22
     */
    ResponseData<SaveOrUpdatePromotionDTO> saveOrUpdatePromotion(SaveOrUpdatePromotionRequest saveOrUpdatePromotionRequest);


}

