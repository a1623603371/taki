package com.taki.market.service.impl;

import com.alibaba.fastjson.JSON;
import com.taki.common.constants.RedisCacheKey;
import com.taki.common.constants.RedisLockKeyConstants;
import com.taki.common.constants.RocketMQConstant;
import com.taki.common.enums.CouponSendTypeEnum;
import com.taki.common.message.PlatformCouponUserBucketMessage;
import com.taki.common.message.PlatformPromotionUserBucketMessage;
import com.taki.common.redis.RedisCache;
import com.taki.common.redis.RedisLock;
import com.taki.common.utli.JsonUtil;
import com.taki.common.utli.ParamCheckUtil;
import com.taki.market.converter.CouponConverter;
import com.taki.market.dao.MarketCouponConfigDAO;
import com.taki.market.dao.MarketCouponItemDAO;
import com.taki.market.domain.dto.ReceiveCouponDTO;
import com.taki.market.domain.dto.SaveOrUpdateCouponDTO;
import com.taki.market.domain.dto.SendCouponDTO;
import com.taki.market.domain.dto.UserCouponDTO;
import com.taki.market.domain.entity.MarketCouponConfigDO;
import com.taki.market.domain.entity.MarketCouponItemDO;
import com.taki.market.domain.request.*;
import com.taki.market.enums.CouponStatusEnum;
import com.taki.market.enums.CouponUsedEnum;
import com.taki.market.exception.MarketBizException;
import com.taki.market.exception.MarketErrorCodeEnum;
import com.taki.market.domain.query.UserCouponQuery;
import com.taki.market.mq.producer.DefaultProducer;
import com.taki.market.remote.MembershipRemote;
import com.taki.market.service.CouponService;
import com.taki.user.domain.request.SaveOrUpdateMembershipRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ClassName CouponServiceImpl
 * @Description 优惠券 service组件
 * @Author Long
 * @Date 2022/2/18 14:18
 * @Version 1.0
 *
 */
@Service
@Slf4j
public class CouponServiceImpl implements CouponService {

    @Autowired
    private MarketCouponItemDAO marketCouponItemDAO;


    @Autowired
    private MarketCouponConfigDAO marketCouponConfigDAO;

    @Autowired
    private CouponConverter couponConverter;


    @Autowired
    private MembershipRemote membershipRemote;


    @Autowired
    private DefaultProducer defaultProducer;


    @Autowired
    private RedisLock redisLock;


    @Autowired
    private RedisCache redisCache;

    @Override
    public UserCouponDTO getUserCoupon(UserCouponQuery userCouponQuery) {
    String userId = userCouponQuery.getUserId();
    ParamCheckUtil.checkStringNonEmpty(userId);
    String couponId = userCouponQuery.getCouponId();
    ParamCheckUtil.checkStringNonEmpty(couponId);

    MarketCouponItemDO marketCouponItem = marketCouponItemDAO.getUserCoupon(userId,couponId);

    if (ObjectUtils.isEmpty(marketCouponItem)){
        throw new MarketBizException(MarketErrorCodeEnum.USER_COUPON_IS_NULL);
    }
    Long couponConfigId = marketCouponItem.getCouponId();

    // 判断优惠券活动配置信息是否存在
    MarketCouponConfigDO couponConfig = marketCouponConfigDAO.getByCouponConfigId(couponConfigId);

    if (ObjectUtils.isEmpty(couponConfig)){
        throw new MarketBizException(MarketErrorCodeEnum.USER_COUPON_CONFIG_IS_NULL);
    }

    String  rule = couponConfig.getCouponRule();
    Map<String,String> ruleMap = JSON.parseObject(rule,Map.class);

    UserCouponDTO userCouponDTO = new UserCouponDTO();
    userCouponDTO.setUserId(userId);
    userCouponDTO.setCouponId(couponId);
    userCouponDTO.setCouponName(couponConfig.getCouponName());
    userCouponDTO.setCouponType(couponConfig.getCouponType());
    userCouponDTO.setAmount(new BigDecimal(ruleMap.get("amount")));
    userCouponDTO.setConditionAmount(new BigDecimal(ruleMap.get("conditionAmount")));
    userCouponDTO.setValidStartTime(couponConfig.getValidStartTime());
    userCouponDTO.setValidEndTime(couponConfig.getValidEndTime());
        return  userCouponDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean lockUserCoupon(LockUserCouponRequest lockUserCouponRequest) {
        // 检查入参
        checkLockUserCouponRequest(lockUserCouponRequest);

        String userId = lockUserCouponRequest.getUserId();
        String couponId = lockUserCouponRequest.getCouponId();

        MarketCouponItemDO marketCouponItem = marketCouponItemDAO.getUserCoupon( userId,couponId);
        if (ObjectUtils.isEmpty(marketCouponItem)){
            throw new MarketBizException(MarketErrorCodeEnum.USER_COUPON_IS_NULL);
        }

        // 判断优惠券是否已使用
        if (CouponUsedEnum.USED.getCode().equals(marketCouponItem.getUsed())){
            throw new MarketBizException(MarketErrorCodeEnum.USER_COUPON_IS_USED);
        }
        marketCouponItem.setUsed(CouponUsedEnum.USED.getCode());
        marketCouponItem.setUsedTime(LocalDateTime.now());
        marketCouponItemDAO.updateById(marketCouponItem);
        return true;
    }

    /**
     * @description:  检查锁定用户优惠券 入参
     * @param lockUserCouponRequest 锁定用户优惠券 请求
     * @return  void
     * @author Long
     * @date: 2022/2/18 15:14
     */
    private void checkLockUserCouponRequest(LockUserCouponRequest lockUserCouponRequest) {
        String userId = lockUserCouponRequest.getUserId();
        String couponId = lockUserCouponRequest.getCouponId();
        ParamCheckUtil.checkStringNonEmpty(userId);
        ParamCheckUtil.checkStringNonEmpty(couponId);
    }

    @Override
    public Boolean releaseUserCoupon(ReleaseUserCouponRequest releaseUserCouponRequest) {
        String userId = releaseUserCouponRequest.getUserId();
        String couponId = releaseUserCouponRequest.getCouponId();
        MarketCouponItemDO marketCouponItem = marketCouponItemDAO.getUserCoupon(userId,couponId);
        marketCouponItem.setUsed(CouponUsedEnum.UN_USED.getCode());
        marketCouponItem.setUsedTime(null);
       return marketCouponItemDAO.updateById(marketCouponItem);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SaveOrUpdateCouponDTO saveOrUpdateCoupon(SaveOrUpdateCouponRequest saveOrUpdateCouponRequest) {

        MarketCouponConfigDO marketCoupon =  couponConverter.converterCouponDO(saveOrUpdateCouponRequest);

        marketCoupon.setReceivedCount(0L);

        marketCouponConfigDAO.saveCoupon(marketCoupon);

        //判断优惠券类型，系统发放类型，写到redis中，然后用户登录触发领取优惠券

        if(CouponSendTypeEnum.PLATFORM_SEND.getCode().equals(saveOrUpdateCouponRequest.getCouponType())){
            sendPlatformCouponMessage(marketCoupon);
            writeCouponToRedis(marketCoupon);

        }else { //SELF_RECEIVE 自己领取的优惠券类型，则设置一个缓存，在用户登录的时候，自己领取（注 ：因为全平台可以领取，所有没有数量限制）

            List<MarketCouponConfigDO> marketCouponConfigDOList = new ArrayList<>();

            String lockKey = RedisLockKeyConstants.PROMOTION_COUPON_LOCK_KEY;

            try {
                redisLock.tryLock(lockKey,60);

                String promotions  = redisCache.get(RedisCacheKey.PROMOTION_COUPON_KEY);

                if (StringUtils.isNotBlank(promotions)){
                    marketCouponConfigDOList = JsonUtil.json2Object(promotions,List.class);
                }
                // 判断优惠券是否过期，并清理过期优惠券

                marketCouponConfigDOList = validatePromotionEnd(marketCouponConfigDOList);

                marketCouponConfigDOList.add(marketCoupon);


                String promotionCouponDOListRedis = JsonUtil.object2Json(marketCouponConfigDOList);

                // 设置优惠券缓存，默认 30 天过期，具体时长设置 具体看活动设置
                //  也可以设置永不过期，下次活动在清除

                redisCache.set(RedisCacheKey.PROMOTION_COUPON_KEY,promotionCouponDOListRedis,30 * 24 * 60 *  60);
            }finally {
                redisLock.unLock(lockKey);

            }

        }
        SaveOrUpdateCouponDTO saveOrUpdateCouponDTO = new SaveOrUpdateCouponDTO();
        saveOrUpdateCouponDTO.setCouponName(saveOrUpdateCouponRequest.getCouponName());
        saveOrUpdateCouponDTO.setRule(saveOrUpdateCouponRequest.getCouponRule());
        saveOrUpdateCouponDTO.setSuccess(true);
        return saveOrUpdateCouponDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ReceiveCouponDTO receiveCoupon(ReceiveCouponRequest receiveCouponRequest) {
        // 获取优惠券信息
        MarketCouponConfigDO marketCouponConfigDO = marketCouponConfigDAO.getById(receiveCouponRequest.getCouponId());

        // 检查优惠券状态
        ReceiveCouponDTO receiveCoupon = checkCouponStatus(marketCouponConfigDO);

        if (!receiveCoupon.getSuccess()) return receiveCoupon;


        // 检查用户是否已经领取过改优惠券了，如果领取过，直接返回

        MarketCouponItemDO marketCouponItemDO =   marketCouponItemDAO.getUserCoupon(receiveCouponRequest.getUserId(),receiveCouponRequest.getCouponId());

        if (!ObjectUtils.isEmpty(marketCouponItemDO)){

            receiveCoupon.setSuccess(false);
            receiveCoupon.setMessage("已经领取过该优惠券，不要重复领取");
            return receiveCoupon;
        }

        marketCouponConfigDO.setReceivedCount(marketCouponConfigDO.getReceivedCount() +1);

        // 如果领取数量与发放数量相同，将优惠券状态设置为发放完

        if (Objects.equals(marketCouponConfigDO.getGiveOutCount(),marketCouponConfigDO.getReceivedCount())){

                marketCouponConfigDO.setStatus(CouponStatusEnum.USED.getCode());
        }

        marketCouponConfigDAO.updateById(marketCouponConfigDO);

        // 领取 一张优惠券

        MarketCouponItemDO couponItem = buildMarketCouponItemDO(marketCouponConfigDO,receiveCouponRequest.getUserId());

        // 添加一张优惠券记录
        marketCouponItemDAO.save(couponItem);

        return receiveCoupon;

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean receiveCouponAvailable(Long userId) {

        String promotions = redisCache.get(RedisCacheKey.PROMOTION_COUPON_KEY);

        List<MarketCouponConfigDO> marketCouponConfigDOList = new ArrayList<>();

        if (StringUtils.isNotBlank(promotions)){

            marketCouponConfigDOList = JSON.parseObject(promotions,List.class);
        }
        log.info("缓存中的优惠券：{}",marketCouponConfigDOList.get(0));

        // 判断优惠券是否过期，并清理过期优惠券

        marketCouponConfigDOList = validatePromotionEnd(marketCouponConfigDOList);

        log.info("缓存有效的优惠券：{}",marketCouponConfigDOList);

        //每次查询数量
        Integer batchSize = 200;
        // 每次 查询起始位置
        Integer  startIndex = 0;

        //分批从数据库中读取已保存的优惠券id

        List<Long> couponIds = new ArrayList<>(100);

        while (true){

            // 因为不需要整个优惠券数据对象，所以，自己定义一个分页查询
            List<Long> couponIdsLimits = marketCouponItemDAO.queryAvailableCoupon(userId,LocalDateTime.now(),startIndex,batchSize);

            if (couponIdsLimits.size() == 0){
                break;
            }
            couponIds.addAll(couponIdsLimits);

            startIndex += batchSize;

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
              new MarketBizException("分批获取用户优惠券id异常");
            }
        }

        log.info("获取到优惠券数据：{}",couponIds);

        //循环判断用户是否已经领取过该优惠券，如果领取过，直接返回，因为是循环操作数据

        //所以这里对这个地方做一个优化，根据用户Id，查询出所有其数据库中的有效优惠券id
        //在内存里判断是否存在与可领取的优惠券中、
        List<MarketCouponItemDO> filteredCouponItem = new ArrayList<>();

        marketCouponConfigDOList.forEach(marketCouponConfigDO -> {


            if (couponIds.contains(marketCouponConfigDO.getId())){
                return;
            }

            MarketCouponItemDO couponItemDO = buildMarketCouponItemDO(marketCouponConfigDO,userId.toString());

            filteredCouponItem.add(couponItemDO);

        });

        if (!filteredCouponItem.isEmpty()){
        log.info("领取到的优惠券：{}",filteredCouponItem);

        marketCouponItemDAO.saveBatch(filteredCouponItem);
        }

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SendCouponDTO sendCoupon(SendCouponRequest sendCouponRequest) {
        return sendCouponByConditions(sendCouponRequest);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SendCouponDTO sendCouponByConditions(SendCouponRequest sendCouponRequest) {
        // 保存优惠券信息
        MarketCouponConfigDO marketCouponConfig = couponConverter.converterCouponDO(sendCouponRequest);

        marketCouponConfig.setReceivedCount(0L);
        marketCouponConfig.setStatus(CouponStatusEnum.NORMAL.getCode());
        marketCouponConfig.setGiveOutType(CouponSendTypeEnum.SELF_RECEIVE.getCode());
        marketCouponConfigDAO.saveCoupon(marketCouponConfig);

        // 构造 messageResult
     //   SaveOrUpdateMessageRequest
        return null;
    }

    /*** 
     * @description: 构造 用户优惠券信息
     * @param marketCouponConfigDO 优惠券 信息
     * @param userId 用户Id
     * @return
     * @author Long
     * @date: 2022/10/4 22:13
     */ 
    private MarketCouponItemDO buildMarketCouponItemDO(MarketCouponConfigDO marketCouponConfigDO, String userId) {

            MarketCouponItemDO marketCouponItemDO = MarketCouponItemDO.builder()
                    .couponId(marketCouponConfigDO.getId())
                    .userId(userId)
                    .couponType(marketCouponConfigDO.getCouponType())
                    .used(CouponUsedEnum.UN_USED.getCode())
                    .activityStartTime(marketCouponConfigDO.getValidStartTime())
                    .activityEndTime(marketCouponConfigDO.getValidEndTime())
                    .build();

            return marketCouponItemDO;

    }

    /*** 
     * @description: 检查优惠券状态，并返回 领取优惠券结果对象
     * @param marketCouponConfigDO
     * @return  com.taki.market.domain.dto.ReceiveCouponDTO
     * @author Long
     * @date: 2022/10/4 21:57
     */ 
    private ReceiveCouponDTO checkCouponStatus(MarketCouponConfigDO marketCouponConfigDO) {

        if(Objects.isNull(marketCouponConfigDO)){
            throw new MarketBizException("优惠券存在");
        }

        ReceiveCouponDTO receiveCoupon = new ReceiveCouponDTO();

        Integer couponStatus = marketCouponConfigDO.getStatus();

        //领取完或过期

        if(!Objects.equals(couponStatus,CouponStatusEnum.NORMAL.getCode())){
            receiveCoupon.setSuccess(false);

        CouponStatusEnum statusEnum = CouponStatusEnum.getByCode(couponStatus);

        if (Objects.isNull(statusEnum)){
            throw new MarketBizException("优惠券领取失败");
        }

        receiveCoupon.setMessage("优惠券" +  statusEnum.getMsg() + "下次早点领取");

        return receiveCoupon;
        }

        //发行 数量小于 或者 等于领取数量，优惠券已经领取完

        if (marketCouponConfigDO.getGiveOutCount() <= marketCouponConfigDO.getReceivedCount()){

            //修改 coupon

            marketCouponConfigDO.setStatus(CouponStatusEnum.USED.getCode());

            marketCouponConfigDAO.updateById(marketCouponConfigDO);

            receiveCoupon.setSuccess(false);
            receiveCoupon.setMessage("优惠券已发放完成，下次早点来");
        }

        // 优惠券过期
        if (marketCouponConfigDO.getValidStartTime().isBefore(LocalDateTime.now())){

            // 修改 coupon

            marketCouponConfigDO.setStatus(CouponStatusEnum.EXPIRED.getCode());

            marketCouponConfigDAO.updateById(marketCouponConfigDO);

            receiveCoupon.setSuccess(false);
            receiveCoupon.setMessage("优惠券已过期，下次早点来");

            return receiveCoupon;
        }

        receiveCoupon.setSuccess(true);

        return receiveCoupon;
    }

    /*** 
     * @description:  判断是否存在过期优惠券
     * @param marketCouponConfigDOList
     * @return  java.util.List<com.taki.market.domain.entity.MarketCouponConfigDO>
     * @author Long
     * @date: 2022/10/4 19:11
     */ 
    private List<MarketCouponConfigDO> validatePromotionEnd(List<MarketCouponConfigDO> marketCouponConfigDOList) {

        // 重新 搞一个 list返回值
        //这么做的目的是，尽量避免上下游方法对同一个list进行修改后直接返回
        //尤其是遇到类似list，sublist这种方式，会造成数据问题
        /**
         *  public List<E> subList(int fromIndex, int toIndex) {
         *         subListRangeCheck(fromIndex, toIndex, size);
         *         return new SubList(this, 0, fromIndex, toIndex);
         *     }
         */
        List result = new ArrayList();

        marketCouponConfigDOList.forEach(coupon ->{

            if (coupon.getValidEndTime().isBefore(LocalDateTime.now()) || !Objects.equals(coupon.getStatus(), CouponStatusEnum.NORMAL.getCode())){
                return;
            }
            result.add(coupon);
        });

        return result;

    }

    /*** 
     * @description:  写入redis 缓存
     * @param marketCoupon 优惠券消息
     * @return  void
     * @author Long
     * @date: 2022/10/4 18:30
     */ 
    private void writeCouponToRedis(MarketCouponConfigDO marketCoupon) {

        // 需要用resdission基于redis做一个分布式的加锁，再去维护一个数据结构

        String  lockKey  = RedisLockKeyConstants.PROMOTION_COUPON_ID_LIST_LOCK;

        try {
            redisLock.tryLock(lockKey,60);

            List<Long> couponIds = null;

            String couponIdsJSON = redisCache.get(RedisCacheKey.PROMOTION_COUPON_ID_LIST_KEY);

            if (couponIdsJSON == null || couponIdsJSON.equals("")){
                couponIds = new ArrayList<>();
            }else {
                couponIds = JSON.parseObject(couponIdsJSON,List.class);
            }

            // 检查每个优惠券，时间是否过期，如果过期，或者已经发完券，把他从list里删除，以及从redis里做一个删除
            //如果是全量发券，没有说所谓的发完，他可以发给所有人，如果超过时间，就不能发券

            if (couponIds.size() > 0){
                Iterator<Long> couponIdIterator = couponIds.iterator();

                while (couponIdIterator.hasNext()){
                    Long tempCouponId = couponIdIterator.next();

                    String tempCouponJSON = redisCache.get(RedisCacheKey.PROMOTION_COUPON_KEY +tempCouponId);

                    MarketCouponConfigDO tempCoupon  = JSON.parseObject(tempCouponJSON,MarketCouponConfigDO.class);

                    LocalDateTime now = LocalDateTime.now();

                    if (now.isAfter(tempCoupon.getValidEndTime())){
                        couponIdIterator.remove();
                        redisCache.delete(RedisCacheKey.PROMOTION_COUPON_KEY +tempCouponId);
                    }
                }
            }

            couponIds.add(marketCoupon.getId());

            couponIdsJSON = JsonUtil.object2Json(couponIds);

            redisCache.set(RedisCacheKey.PROMOTION_COUPON_ID_LIST_KEY,couponIdsJSON,-1);

            String couponJSON = JsonUtil.object2Json(marketCoupon);

            redisCache.set(RedisCacheKey.PROMOTION_COUPON_KEY + marketCoupon.getId(),couponJSON,-1);

        }finally {
            redisLock.unLock(lockKey);

        }




    }


    /*** 
     * @description:  为所有用户发放优惠券
     * @param marketCouponConfigDO 优惠券信息
     * @return  void
     * @author Long
     * @date: 2022/10/4 18:08
     */ 
    private void sendPlatformCouponMessage(MarketCouponConfigDO marketCouponConfigDO){

        // 桶大小
        final int userBucketSize = 1000;

        final int messageBatchSize = 100;


        Long maxUserId = membershipRemote.queryMaxUserId();


        Map<Long,Long> userBuckets = new LinkedHashMap<>();

        AtomicBoolean flagRef = new AtomicBoolean(true);

        long startUserId = 1L;

        while (flagRef.get()){

            if (startUserId > maxUserId){
                flagRef.compareAndSet(true,false);
            }

            userBuckets.put(startUserId,startUserId + userBucketSize);

            startUserId += userBucketSize;
        }

            //3.批量发送消息
            //列 ： userBucketCount
            int handledBucketCount = 0;

            List<String> jsonMessageBatch = new ArrayList<>(messageBatchSize);

            for (Map.Entry<Long, Long> entry : userBuckets.entrySet()) {
               handledBucketCount ++ ;

                PlatformCouponUserBucketMessage platformCouponUserBucketMessage = PlatformCouponUserBucketMessage.builder()
                        .startUserId(entry.getKey())
                        .endUserId(entry.getValue())
                        .informType(marketCouponConfigDO.getInformType())
                        .couponId(marketCouponConfigDO.getId())
                        .activityStartTime(marketCouponConfigDO.getValidStartTime())
                        .activityEndTime(marketCouponConfigDO.getValidEndTime())
                        .couponType(marketCouponConfigDO.getCouponType())
                        .build();

                String jsonMessage = JsonUtil.object2Json(platformCouponUserBucketMessage);

                jsonMessageBatch.add(jsonMessage);

                if (jsonMessageBatch.size() == messageBatchSize || handledBucketCount ==  userBuckets.size()){

                    defaultProducer.sendMessages(RocketMQConstant.PLATFORM_COUPON_SEND_USER_BUCKET_TOPIC,jsonMessageBatch,"平台发放优惠券用户桶的消息");

                    jsonMessageBatch.clear();
                }
            }





        
    }
}
