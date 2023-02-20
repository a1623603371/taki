package com.taki.careerplan.cookbook.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.taki.careerplan.cookbook.converter.CookbookUserConverter;
import com.taki.careerplan.cookbook.converter.CookbookUserConverter;
import com.taki.careerplan.cookbook.dao.CookbookUserDAO;
import com.taki.careerplan.cookbook.domain.entity.CookbookDO;
import com.taki.careerplan.cookbook.domain.entity.CookbookUserDO;
import com.taki.careerplan.cookbook.exception.CookBookException;
import com.taki.careerplan.cookbook.service.CookbookUserService;
import com.taki.careerplan.domain.dto.CookbookUserDTO;
import com.taki.careerplan.domain.dto.SaveOrUpdateUserDTO;
import com.taki.careerplan.domain.request.CookbookUserQueryRequest;
import com.taki.careerplan.domain.request.SaveOrUpdateUserRequest;
import com.taki.common.cache.CacheSupport;
import com.taki.common.constants.RedisCacheKey;
import com.taki.common.constants.RedisLockKeyConstants;
import com.taki.common.redis.RedisCache;
import com.taki.common.redis.RedisLock;
import com.taki.common.utli.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 菜谱作者表 服务实现类
 * </p>
 *
 * @author long
 * @since 2023-02-18
 */
@Service
@Slf4j
public class CookbookUserServiceImpl implements CookbookUserService {


    private final  static Long  USER_UPDATE_LOCK_TIMEOUT = 200L;

    @Autowired
    private  CookbookUserDAO cookbookUserDAO;

    @Autowired
    private CookbookUserConverter cookbookUserConverter;




    @Autowired
    private RedisCache redisCache;


    @Autowired
    private RedisLock redisLock;



    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaveOrUpdateUserDTO saveOrUpdateUser(SaveOrUpdateUserRequest request) {
        //1.增对操作者设置 分布式锁
    String userUpdateLockKey = RedisLockKeyConstants.USER_UPDATE_LOCK_PREFIX + request.getOperator();
    Boolean lock = redisLock.tryLock(userUpdateLockKey);
    if (!lock){
    log.info("操作作者信息获取锁失败,operator:{}",request.getOperator());
    throw new CookBookException("新增/修改失败");
    }
    try {
        // 先更新DB
        CookbookUserDO cookbookUserDO = cookbookUserConverter.converterCookbookUserDO(request);
        cookbookUserDO.setUpdateUser(request.getOperator());
        if (Objects.isNull(cookbookUserDO.getId())){
            cookbookUserDO.setCreateUser(request.getOperator());
        }
        cookbookUserDAO.saveOrUpdate(cookbookUserDO);
        CookbookUserDTO cookbookUserDTO = cookbookUserConverter.converterCookbookUserDTO(cookbookUserDO);
        // 设置 用户数据到缓存中，设置 随机过期时间
        redisCache.setCache(RedisCacheKey.COOKBOOK_USER_INFO_PREFIX + cookbookUserDO.getId(),
                cookbookUserDTO, CacheSupport.generateCacheExpireSecond());
        SaveOrUpdateUserDTO saveOrUpdateUserDTO = SaveOrUpdateUserDTO.builder()
                .userId(cookbookUserDO.getId())
                .success(true)
                .build();
        return saveOrUpdateUserDTO;
    }finally {
        redisLock.unLock(userUpdateLockKey);
    }
    }

    @Override
    public CookbookUserDTO getUserInfo(CookbookUserQueryRequest request) {

        Long userId = request.getUserId();

        // 从缓存中获取数据
        CookbookUserDTO cookbookUserDTO = getUserFromCache(userId);

        if (Objects.nonNull(cookbookUserDTO)){
            return cookbookUserDTO;
        }
        // 从数据库获取 数据
        return getUserInfoDB(userId);
    }

    @Override
    public CookbookUserDO getById(Long userId) {
        return this.getById(userId);
    }

    /*** 
     * @description:
     * @param userId
     * @return  com.taki.careerplan.domain.dto.CookbookUserDTO
     * @author Long
     * @date: 2023/2/18 21:45
     */ 
    private CookbookUserDTO getUserInfoDB(Long userId) {
        //这里 可以 使用 2把 锁 load 锁 与 update 锁 ，应对不同场景的并发
        String userLockKey = RedisLockKeyConstants.USER_UPDATE_LOCK_PREFIX +userId;

        Boolean lock = false;

        try {
            lock = redisLock.tryLock(userLockKey,USER_UPDATE_LOCK_TIMEOUT);
        } catch (InterruptedException e) {
            CookbookUserDTO cookbookUserDTO = getUserFromCache(userId);
            if (Objects.nonNull(cookbookUserDTO)){
                return cookbookUserDTO;
            }
            log.error(e.getMessage(),e);
            throw new CookBookException("查询数据失败");
        }
        if (!lock){
            CookbookUserDTO cookbookUserDTO = getUserFromCache(userId);
            if (Objects.nonNull(cookbookUserDTO)){
                return cookbookUserDTO;
            }
            log.info("缓存数据为空，从数据库查询作者信息获取锁失败，userId:{}",userId);
            throw new CookBookException("查询数据失败");
        }

        try {
            CookbookUserDTO cookbookUserDTO = getUserFromCache(userId);
            if (Objects.nonNull(cookbookUserDTO)){
                return cookbookUserDTO;
            }
            log.info("缓存为空重数据库取数据：userId:{}",userId);
            String userInfoKey  = RedisCacheKey.COOKBOOK_USER_INFO_PREFIX +userLockKey;

            CookbookUserDO cookbookUserDO  = cookbookUserDAO.getById(userId);

            // 如果 数据为空 //设置空数据
            if (Objects.isNull(cookbookUserDO)){
                redisCache.setCache(userInfoKey,CacheSupport.EMPTY_CACHE,CacheSupport.generateCachePenetrationExpireSecond());
                return null;
            }
            cookbookUserDTO = cookbookUserConverter.converterCookbookUserDTO(cookbookUserDO);
            // 设置 到缓存中
            redisCache.setCache(userInfoKey,JsonUtil.object2Json(cookbookUserDO),CacheSupport.generateCacheExpireSecond());
            return cookbookUserDTO;

        }finally {
            redisLock.unLock(userLockKey);
        }



    }

    /*** 
     * @description: 从缓存中获取数据
     * @param userId 用户Id
     * @return  com.taki.careerplan.domain.dto.CookbookUserDTO
     * @author Long
     * @date: 2023/2/18 21:29
     */ 
    private CookbookUserDTO getUserFromCache(Long userId) {
        String userInfoKey = RedisCacheKey.COOKBOOK_USER_INFO_PREFIX + userId;
        Object cookbookUserValue = redisCache.get(userInfoKey);
        if (Objects.equals(CacheSupport.EMPTY_CACHE,cookbookUserValue)){
            // 如果 是空缓存 防止缓存穿透,返回空对象
            return new CookbookUserDTO();
        }
        if (cookbookUserValue instanceof CookbookUserDTO){
                //如果是对象,则直接返回对象
            return (CookbookUserDTO) cookbookUserValue;
        }
        if (cookbookUserValue instanceof  String) {
            // 如果是字符串 则是从缓存中获取的数据,转换成对象之后返回
            Long expire = redisCache.getExpire(userInfoKey, TimeUnit.SECONDS);
            /**
             * 对于临时缓存2钟做法
             * 1.临时再续期
             * 2.不续期,随机过期时间,过期直接加锁查库,然后放入缓存
             * 这里采用 第一种,如果缓存 过期时间小于 一小时,则重新设置缓存过期时间
             */
            if (expire  < CacheSupport.ONE_HOURS_SECONDS){
                redisCache.expire(userInfoKey,CacheSupport.generateCacheExpireSecond());
            }
            return JsonUtil.json2Object((String) userInfoKey,CookbookUserDTO.class);
        }
        return null;
    }
}
