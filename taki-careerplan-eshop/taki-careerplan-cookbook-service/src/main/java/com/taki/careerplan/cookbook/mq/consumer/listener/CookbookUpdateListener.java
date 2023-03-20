package com.taki.careerplan.cookbook.mq.consumer.listener;

import com.taki.careerplan.cookbook.dao.CookbookDAO;
import com.taki.careerplan.cookbook.message.CookbookUpdateMessage;
import com.taki.careerplan.domain.dto.CookbookDTO;
import com.taki.common.cache.CacheSupport;
import com.taki.common.constants.RedisCacheKey;
import com.taki.common.constants.RedisLockKeyConstants;
import com.taki.common.mq.AbstractMessageListenerConcurrently;
import com.taki.common.redis.RedisCache;
import com.taki.common.redis.RedisLock;
import com.taki.common.utli.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName CookbookUpdateListener
 * @Description TODO
 * @Author Long
 * @Date 2023/3/5 17:20
 * @Version 1.0
 */
@Slf4j
@Component
public class CookbookUpdateListener extends AbstractMessageListenerConcurrently {

    private static  final  int PAGE_SIZE = 20;
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RedisLock redisLock;


    @Autowired
    private CookbookDAO cookbookDAO;

    @Override
    protected ConsumeConcurrentlyStatus omMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext) {

        try {
            msgs.forEach(msg->{
                log.info("执行作者菜谱缓存数据更新逻辑，消息内容：{}",msg.getBody());
                String message = new String(msg.getBody());

                CookbookUpdateMessage cookbookUpdateMessage = JsonUtil.json2Object(message,CookbookUpdateMessage.class);

                Long userId = cookbookUpdateMessage.getUserId();

                String cookbookUpdateLockKey = RedisLockKeyConstants.COOK_UPDATE_LOCK_PREFIX + userId;

                redisLock.lock(cookbookUpdateLockKey);

                try {
                    // 这里对userId的菜谱list页缓存都做一个重建

                    String userCookBookCountKey = RedisCacheKey.USER_COOKBOOK_COUNT_PREFIX + userId;

                    Integer count = Integer.valueOf(redisCache.get(userCookBookCountKey));

                    int pageNums = count / PAGE_SIZE + 1;

                    for (int pageNo = 1; pageNo <  pageNums; pageNo++) {
                        String userCookbookPageKey = RedisCacheKey.USER_COOKBOOK_PAGE_PREFIX + userId + ":" + pageNo;
                        String cookbookJson = redisCache.get(userCookbookPageKey);
                        if (cookbookJson == null || "".equals(cookbookJson)){
                            continue;
                        }
                        //雀氏有这一页数据，此时就需要对这一页数据进行更新
                        List<CookbookDTO> cookbooks = cookbookDAO.pageByUserId(userId,Long.valueOf(pageNo),Long.valueOf(PAGE_SIZE));

                        redisCache.set(userCookbookPageKey,JsonUtil.object2Json(cookbooks), CacheSupport.generateCacheExpireSecond());
                    }


                }finally {
                    redisLock.unLock(cookbookUpdateLockKey);
                }

            });
        }catch (Exception e){
            log.error("consume error,更新作者菜谱缓存数据消费失败",e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;

        }

        log.info("更新作者菜谱缓存数据消费成功，result:{}",ConsumeConcurrentlyStatus.CONSUME_SUCCESS);

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
