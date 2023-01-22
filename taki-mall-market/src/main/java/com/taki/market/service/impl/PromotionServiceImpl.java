package com.taki.market.service.impl;


import cn.hutool.core.lang.UUID;
import com.taki.common.constants.RedisCacheKey;
import com.taki.common.constants.RocketMQConstant;
import com.taki.common.message.PlatformPromotionUserBucketMessage;
import com.taki.common.redis.RedisCache;
import com.taki.common.utli.JsonUtil;
import com.taki.common.utli.ResponseData;
import com.taki.market.converter.PromotionConverter;
import com.taki.market.dao.MarketPromotionDAO;
import com.taki.market.domain.dto.SaveOrUpdatePromotionDTO;
import com.taki.market.domain.entity.MarketPromotionDO;
import com.taki.market.domain.request.SaveOrUpdatePromotionRequest;
import com.taki.market.mq.event.SalesPromotionCreatedEvent;
import com.taki.market.mq.producer.DefaultProducer;
import com.taki.market.remote.MembershipRemote;
import com.taki.market.service.PromotionService;
import com.taki.user.api.MembershipApi;
import com.taki.user.domain.dto.SaveOrUpdateMembershipDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private MembershipRemote membershipRemote;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private DefaultProducer defaultProducer;


    @Autowired
    private PromotionConverter promotionConverter;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public SaveOrUpdatePromotionDTO saveOrUpdatePromotion(SaveOrUpdatePromotionRequest request) {

        String result = redisCache.get(RedisCacheKey.PROMOTION_CONCURRENCY_KEY +
                request.getName()
                + request.getCreateUser()
                + request.getStartTime().toInstant(ZoneOffset.of("+8")).toEpochMilli()
                + request.getEndTime().toInstant(ZoneOffset.of("+8")).toEpochMilli());
        if (StringUtils.isNotBlank(result)){
            return null;
        }
        log.info("活动内容：{}",request);

        //活动规则
        String rule = JsonUtil.object2Json(request.getRule());

        //构造促销活动实体

        MarketPromotionDO marketPromotion = promotionConverter.convertPromotionDO(request);
        marketPromotion.setRule(rule);

        //促销活动落库
        marketPromotionDAO.save(marketPromotion);

        redisCache.set(RedisCacheKey.PROMOTION_CONCURRENCY_KEY +
                request.getName() + request.getCreateUser()
                + request.getStartTime().toInstant(ZoneOffset.of("+8")).toEpochMilli()
                +request.getEndTime().toInstant(ZoneOffset.of("+8")).toEpochMilli(), UUID.fastUUID().toString(), 30 * 60);


        // 为所有用户推送促销活动，发MQ
        sendPlatformPromotionMessage(marketPromotion);

        publishSalesPromotionCreateCreatedEvent(marketPromotion);


        //构造响应数据
        SaveOrUpdatePromotionDTO  saveOrUpdatePromotionDTO = new SaveOrUpdatePromotionDTO();

        saveOrUpdatePromotionDTO.setName(request.getName());
        saveOrUpdatePromotionDTO.setRule(rule);
        saveOrUpdatePromotionDTO.setType(request.getType());
        saveOrUpdatePromotionDTO.setCreateUser( Long.valueOf(request.getCreateUser()));
        saveOrUpdatePromotionDTO.setSuccess(true);
        return saveOrUpdatePromotionDTO;
    }
    
    /*** 
     * @description:  发布促销活动创建事件
     * @param marketPromotion
     * @return  void
     * @author Long
     * @date: 2022/10/3 23:31
     */ 
    private void publishSalesPromotionCreateCreatedEvent(MarketPromotionDO marketPromotion) {

        SalesPromotionCreatedEvent salesPromotionCreatedEvent = new SalesPromotionCreatedEvent();

        salesPromotionCreatedEvent.setMarketPromotion(marketPromotion);

        String salesPromotionCreatedEventJSON = JsonUtil.object2Json(salesPromotionCreatedEvent);

        defaultProducer.sendMessage(RocketMQConstant.SALES_PROMOTION_CREATED_EVENT_TOPIC,salesPromotionCreatedEventJSON,"发布促销活动创建事件");


    }


    /**
     * @description:  为所有用户推送优惠券
     * @param marketPromotion 营销促销实体
     * @return  void
     * @author Long
     * @date: 2022/9/29 21:42
     */
    private void sendPlatformPromotionMessage(MarketPromotionDO marketPromotion) {

        final int userBucketSize = 1000;

        final int messageBatchSize = 100;

        // 1.查询出库里面最大的userId,作为用的总数量
       Long maxUserId = membershipRemote.queryMaxUserId();


       // 用于自己测试功能流程，百万数据测试一次成本太高
        //maxUserId = 1000L;
        // 2,分成m个捅，每个桶里面有n个用户，每个桶发送一条“批量发送优惠券用户桶消息”
        // 列： MaxUserId = 1000W userBucketSize = 1000；

        // userBucket1 =[1,1001]
        // userBucket2 = [2,2002]
         // userBucketCount = 1000

        Map<Long,Long> userBuckets = new LinkedHashMap<>();

        AtomicBoolean flagRef = new AtomicBoolean(false);

        Long startUserId = 1L;

        while (flagRef.get()){
            if (startUserId > maxUserId){
                flagRef.compareAndSet(true,false);
            }
            userBuckets.put(startUserId,startUserId + userBucketSize);

            startUserId += userBucketSize;
        }
            // 3.批量发送消息
            //列 userBucketCount = 1000; messageBatchSize = 100

            //批量发送次数 10次 经过 2次分桶 ，这里发送消息 次数从 1000w 降到10次

            int handledBucketCount = 0;

            List<String> jsonMessageBatch = new ArrayList<>();

            for (Map.Entry<Long, Long> userBucket : userBuckets.entrySet()) {

                 handledBucketCount ++ ;

                PlatformPromotionUserBucketMessage message = PlatformPromotionUserBucketMessage.builder()
                        .startUserId(userBucket.getKey())
                        .endUserId(userBucket.getValue())
                        .promotionId(marketPromotion.getId())
                        .promotionType(marketPromotion.getType())
                        .mainMessage(marketPromotion.getName())
                        .message("您已活动活动资格，打开APP进入活动页面")
                        .informType(marketPromotion.getInformType())
                        .build();

                String jsonMessage = JsonUtil.object2Json(message);
                jsonMessageBatch.add(jsonMessage);

                if (jsonMessageBatch.size() ==  messageBatchSize || handledBucketCount == userBuckets.size()){
                    defaultProducer.sendMessages(RocketMQConstant.PLATFORM_COUPON_SEND_USER_BUCKET_TOPIC,jsonMessageBatch,"平台发放优惠券用户桶消息");
                    jsonMessageBatch.clear();
                }

            }


    }
}
