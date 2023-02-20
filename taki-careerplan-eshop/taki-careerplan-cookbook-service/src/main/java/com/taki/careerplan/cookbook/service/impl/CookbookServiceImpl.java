package com.taki.careerplan.cookbook.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.jd.platform.hotkey.client.callback.JdHotKeyStore;
import com.taki.careerplan.cookbook.converter.CookBookConverter;
import com.taki.careerplan.cookbook.dao.CookbookDAO;
import com.taki.careerplan.cookbook.domain.entity.CookbookDO;
import com.taki.careerplan.cookbook.domain.entity.CookbookSkuRelationDO;
import com.taki.careerplan.cookbook.domain.entity.CookbookUserDO;
import com.taki.careerplan.cookbook.exception.CookBookException;
import com.taki.careerplan.cookbook.message.CookbookUpdateMessage;
import com.taki.careerplan.cookbook.mq.producer.DefaultProducer;
import com.taki.careerplan.cookbook.service.CookbookService;
import com.taki.careerplan.cookbook.service.CookbookSkuRelationService;
import com.taki.careerplan.cookbook.service.CookbookUserService;
import com.taki.careerplan.cookbook.service.SkuInfoService;
import com.taki.careerplan.domain.dto.CookbookDTO;
import com.taki.careerplan.domain.dto.Food;
import com.taki.careerplan.domain.dto.SaveOrUpdateCookbookDTO;
import com.taki.careerplan.domain.dto.StepDetail;
import com.taki.careerplan.domain.request.CookbookQueryRequest;
import com.taki.careerplan.domain.request.SaveOrUpdateCookbookRequest;
import com.taki.common.cache.CacheSupport;
import com.taki.common.constants.CoreConstants;
import com.taki.common.constants.RedisCacheKey;
import com.taki.common.constants.RedisLockKeyConstants;
import com.taki.common.constants.RocketMQConstant;
import com.taki.common.enums.DeleteStatusEnum;
import com.taki.common.redis.RedisCache;
import com.taki.common.redis.RedisLock;
import com.taki.common.utli.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜谱表 服务实现类
 * </p>
 *
 * @author long
 * @since 2023-02-18
 */
@Service
@Slf4j
public class CookbookServiceImpl  implements CookbookService {

    /**
     * 商品修改锁
     */
    private static final Long COOKBOOK_UPDATE_LOCK_TIMEOUT = 200L;

    @Autowired
    private CookbookDAO cookbookDAO;

    @Autowired
    private CookbookUserService cookbookUserService;


    @Autowired
    private CookbookSkuRelationService cookbookSkuRelationService;


    @Autowired
    private RedisLock redisLock;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private DefaultProducer defaultProducer;


    @Autowired
    private CookBookConverter cookBookConverter;

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private Cache<String,Object> caffeine;


    private Lock localLock = new ReentrantLock();


    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaveOrUpdateCookbookDTO saveOrUpdateCookbook(SaveOrUpdateCookbookRequest request) {

        String cookbookUpdateLockKey = RedisLockKeyConstants.COOK_UPDATE_LOCK_PREFIX + request.getOperator();

        Boolean lock = null;

        if (Objects.nonNull(request.getId()) && request.getId() > 0){
            try {
                lock = redisLock.tryLock(cookbookUpdateLockKey,200L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (lock != null && !lock){
            log.info("操作菜谱获取锁失败，operate：{}",request.getOperator());
            throw new CookBookException("新增/修改失败");
        }

        try {
            // 构建菜谱信息
            CookbookDO cookbookDO = buildCookbookDO(request);

            //保存菜谱信息 菜谱 = 美食分享，关于美食，菜品，图，视频，如何做，原材料，信息
            cookbookDAO.saveOrUpdate(cookbookDO);

            // 菜谱商品关联信息，一个菜谱可以种草多个商品，可以保存
            List<CookbookSkuRelationDO> cookbookSkuRelationList = buildCookbookSkuRelationDOS(cookbookDO,request);

            // 保存菜谱与商品关联信息
            cookbookSkuRelationService.saveBatch(cookbookSkuRelationList);

            // 跟新缓存信息
            updateCookbookCache(cookbookDO,request);

            // 发布菜谱数据更新事件消息
            publishCookbookUpdatedEvent(cookbookDO);

            // 返回信息
            SaveOrUpdateCookbookDTO saveOrUpdateCookbookDTO = SaveOrUpdateCookbookDTO.builder()
                    .cookbookId(cookbookDO.getId()).success(true).build();

            return saveOrUpdateCookbookDTO;

        }finally {
            if (lock != null){
                redisLock.unLock(cookbookUpdateLockKey);
            }
        }

    }

    @Override
    public CookbookDTO getCookbookInfo(CookbookQueryRequest request) {

        Long cookbookId = request.getCookbookId();

        CookbookDTO cookbookDTO = getCookbookFormCache(cookbookId);

        if (Objects.nonNull(cookbookDTO)){
            return cookbookDTO;
        }

        return getCookbookFormDB(cookbookId);
    }

    /***
     * @description: 从本地 获取菜谱 数据
     * @param cookbookId 菜谱Id
     * @return  com.taki.careerplan.domain.dto.CookbookDTO
     * @author Long
     * @date: 2023/2/20 18:26
     */
    private CookbookDTO getCookbookFormDB(Long cookbookId) {
        // 我们主要针对的是菜谱数据的更新操作
        // 对某个菜谱进行更新操作，同时在读取这个菜谱的详情，缓存过期，锁粒度，其实cookbookId
        String cookbookLockKey = RedisLockKeyConstants.COOK_UPDATE_LOCK_PREFIX +cookbookId;
        String cookbookKey = RedisCacheKey.COOKBOOK_PREFIX + cookbookId;

        // 如果 redis连接失败 ，降级为本地缓存

        if (Objects.nonNull(JdHotKeyStore.get(CoreConstants.REDIS_CONNECTION_FAILED))){
            return  getCookbookFromLocalCache(cookbookId);
        }

        // 上面是降级流程，这里是正常流程
        Boolean lock = false;

        try {
            redisLock.tryLock(cookbookLockKey,COOKBOOK_UPDATE_LOCK_TIMEOUT);
        } catch (InterruptedException e) {
          CookbookDTO cookbook = getCookbookFormCache(cookbookId);
          if (Objects.nonNull(cookbook)){
              return cookbook;
          }
          log.error(e.getMessage(),e);
        }

        if (!lock){
            CookbookDTO cookbook = getCookbookFormCache(cookbookId);
            if (Objects.nonNull(cookbook)){
                return cookbook;
            }
            log.info("缓存数据为空，从数据库获取菜谱信息获取锁失败:cookbookId:{}",cookbookId);
            throw new CookBookException("查询失败");
        }

        try {
            CookbookDTO cookbook = getCookbookFormCache(cookbookId);
            if (Objects.nonNull(cookbook)){
                return cookbook;
            }
            log.info("缓存数据为空，从数据库获取菜谱信息:cookbookId:{}",cookbookId);

            cookbook = cookbookDAO.getCookbookInfoById(cookbookId);

            if (Objects.isNull(cookbook)){
                redisCache.setCache(cookbookKey,CacheSupport.EMPTY_CACHE,CacheSupport.generateCachePenetrationExpireSecond());
            }
            redisCache.setCache(cookbookKey,cookbook,CacheSupport.generateCacheExpireSecond());
            return  cookbook;
        }finally {
            redisLock.unLock(cookbookLockKey);
        }

    }

    /***
     * @description: 从本地缓存中获取 数据
     * @param cookbookId
     * @return  com.taki.careerplan.domain.dto.CookbookDTO
     * @author Long
     * @date: 2023/2/20 18:37
     */
    private CookbookDTO getCookbookFromLocalCache(Long cookbookId) {

        String cookbookKey = RedisCacheKey.COOKBOOK_PREFIX + cookbookId;
        // 先 查看本地缓存是否有，有就返回，没有则需要获取锁，然后查询
        Object value =  caffeine.getIfPresent(cookbookKey);
        log.warn("redis 连接失败，降级处理，从本地缓存中获取数据 key {}，value {}", cookbookKey, value);
        if (Objects.nonNull(value)){

            if (Objects.equals(CacheSupport.EMPTY_CACHE,value)){
                return null;
            }

            return (CookbookDTO) value;

        }

        // 缓存中没有，则查询数据，并将结果放入本地缓存

        if(localLock.tryLock()){
            try {
                log.info("缓存数据为空，从数据库中获取数据，cookbookId:{}", cookbookId);
                CookbookDTO cookbookDTO = cookbookDAO.getCookbookInfoById(cookbookId);
                caffeine.put(cookbookKey,Objects.isNull(cookbookDTO));

                return  cookbookDTO;
            }finally {
                localLock.unlock();
            }
        }

        throw new CookBookException("系统繁忙,请稍后重试");

    }


    /***
     * @description: 从缓存中获取 菜谱信息
     * @param cookbookId 菜谱信息Id
     * @return  com.taki.careerplan.domain.dto.CookbookDTO
     * @author Long
     * @date: 2023/2/20 17:40
     */
    private CookbookDTO getCookbookFormCache(Long cookbookId) {

        String cookbookKey = RedisCacheKey.COOKBOOK_PREFIX + cookbookId;

        // 从内存中 获取缓存中 获取数据

        Object cookbookValue = redisCache.getCache(cookbookKey);
        if (Objects.equals(CacheSupport.EMPTY_CACHE,cookbookValue)){
            return new CookbookDTO();
        }
        if (cookbookValue instanceof  CookbookDTO) {

            return (CookbookDTO) cookbookValue;
        }

        if (cookbookValue instanceof String) {

            CookbookDTO cookbookDTO = JSONObject.parseObject((String)cookbookValue,CookbookDTO.class);

            Long expire = redisCache.getExpire(cookbookKey, TimeUnit.SECONDS);

            if (expire <=  CacheSupport.ONE_HOURS_SECONDS){
                // 缓存续期
                redisCache.expire(cookbookKey,CacheSupport.generateCacheExpireSecond());
            }
            return cookbookDTO;

        }
        return null;
    }

    /***
     * @description:
     * @param cookbookDO
     * @return  void
     * @author Long
     * @date: 2023/2/20 16:55
     */ 
    private void publishCookbookUpdatedEvent(CookbookDO cookbookDO) {
        //发送通知作者的菜谱的变更通知
        CookbookUpdateMessage message = CookbookUpdateMessage.builder()
                .cookbookId(cookbookDO.getId())
                .userId(cookbookDO.getUserId())
                .build();
        defaultProducer.sendMessage(RocketMQConstant.COOKBOOK_UPDATE_MESSAGE_TOPIC,JsonUtil.object2Json(message),"作者菜谱更新通知",null,null);


    }

    /***
     * @description:  修改菜谱缓存
     * @param cookbookDO 菜谱数据
     * @param  request 修改或新增菜谱数据 请求数据
     * @return  void
     * @author Long
     * @date: 2023/2/20 17:31
     */
    private void updateCookbookCache(CookbookDO cookbookDO, SaveOrUpdateCookbookRequest request) {
        CookbookDTO cookbookDTO =buildCookbookDTO(cookbookDO,request.getSkuIds());

        //修改 菜谱信息缓存数据
        String cookbookKey = RedisCacheKey.COOKBOOK_PREFIX + cookbookDO.getId();

        redisCache.setCache(cookbookKey,cookbookDTO, CacheSupport.generateCacheExpireSecond());

        // 将作者的菜谱总数信息存储到缓存中
        String userCookbookCountKey = RedisCacheKey.USER_COOKBOOK_COUNT_PREFIX + cookbookDO.getUserId();

        redisCache.increment(userCookbookCountKey,1);
    }

    /***
     * @description: 构造 菜谱DTO
     * @param cookbookDO
     * @param  skuIds
     * @return  com.taki.careerplan.domain.dto.CookbookDTO
     * @author Long
     * @date: 2023/2/20 16:30
     */
    private CookbookDTO buildCookbookDTO(CookbookDO cookbookDO, List<Long> skuIds) {
        CookbookDTO cookbookDTO = cookBookConverter.convertCookBookDTO(cookbookDO);
        CookbookUserDO cookbookUserDO = cookbookUserService.getById(cookbookDO.getUserId());
        cookbookDTO.setUserName(cookbookUserDO.getUserName());
        cookbookDTO.setCookbookDetail(JSON.parseArray(cookbookDO.getCookbookDetail(), StepDetail.class));
        cookbookDTO.setFoods(JSON.parseArray(cookbookDO.getFoods(), Food.class));
        cookbookDTO.setSkuIds(skuIds);
        return cookbookDTO;


    }

    /***
     * @description:  构建 菜谱 对应商品 关系
     * @param cookbookDO
     * @param  request
     * @return  java.util.List<com.taki.careerplan.cookbook.domain.entity.CookbookSkuRelationDO>
     * @author Long
     * @date: 2023/2/20 15:57
     */
    private List<CookbookSkuRelationDO> buildCookbookSkuRelationDOS(CookbookDO cookbookDO, SaveOrUpdateCookbookRequest request) {

        if (Objects.isNull(request.getSkuIds())){
        List<String> tags = request.getFoods().stream().map(food -> food.getTag()).collect(Collectors.toList());
        List<Long> skuIds = skuInfoService.getSkuIdsByTags(tags);
        request.setSkuIds(skuIds);
        }

        List<CookbookSkuRelationDO> cookbookSkuRelationList = new ArrayList<>();

        request.getSkuIds().forEach(skuId ->{
            CookbookSkuRelationDO cookbookSkuRelationDO = buildCookbookSkuRelationDO(cookbookDO.getId(),skuId,request.getOperator());
            cookbookSkuRelationList.add(cookbookSkuRelationDO);
        });

        return cookbookSkuRelationList;
    }

    /**
     *  构建 菜谱与商品 关系
     * @param id
     * @param skuId
     * @param operator
     * @return
     */
    private CookbookSkuRelationDO buildCookbookSkuRelationDO(Long id, Long skuId, Integer operator) {

        CookbookSkuRelationDO cookbookSkuRelationDO =CookbookSkuRelationDO.builder()
                .skuId(skuId).cookbookId(id).delFlag(DeleteStatusEnum.NO.getCode()).createUser(operator)
                .build();

        return cookbookSkuRelationDO;
    }

    /*** 
     * @description: 构建 菜谱
     * @param request
     * @return  com.taki.careerplan.cookbook.domain.entity.CookbookDO
     * @author Long
     * @date: 2023/2/20 15:41
     */ 
    private CookbookDO buildCookbookDO(SaveOrUpdateCookbookRequest request) {
        CookbookDO cookbookDO = cookBookConverter.convertCookBookDO(request);
        cookbookDO.setFoods(JsonUtil.object2Json(request.getFoods()));
        cookbookDO.setCookbookDetail(JsonUtil.object2Json(request.getCookbookDetail()));
        cookbookDO.setUpdateUser(request.getOperator());

        //新增数据
        if (Objects.isNull(cookbookDO.getId())){

            //菜谱 状态为空，则设置删除
            if ( Objects.isNull(cookbookDO.getCookbookStatus())){
                cookbookDO.setCookbookStatus(DeleteStatusEnum.NO.getCode());
            }
            //
        }

        return cookbookDO;
    }
}
