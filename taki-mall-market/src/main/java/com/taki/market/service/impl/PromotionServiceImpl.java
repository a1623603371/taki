package com.taki.market.service.impl;

import com.taki.common.redis.RedisCache;
import com.taki.market.dao.MarketPromotionDAO;
import com.taki.market.domain.dto.SaveOrUpdatePromotionDTO;
import com.taki.market.domain.request.SaveOrUpdatePromotionRequest;
import com.taki.market.mq.producer.DefaultProducer;
import com.taki.market.service.PromotionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName PromotionServiceImpl
 * @Description 营销 促销活动service 组件
 * @Author Long
 * @Date 2022/9/26 22:02
 * @Version 1.0
 */
@Service
@Slf4j
public class PromotionServiceImpl implements PromotionService {


    @Autowired
    private MarketPromotionDAO marketPromotionDAO;

    @Autowired
    private RedisCache redisCache;

    private DefaultProducer defaultProducer;

    @Override
    public SaveOrUpdatePromotionDTO saveOrUpdatePromotion(SaveOrUpdatePromotionRequest saveOrUpdatePromotionRequest) {
        return null;
    }
}
