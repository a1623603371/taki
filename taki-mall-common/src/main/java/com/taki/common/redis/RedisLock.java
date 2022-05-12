package com.taki.common.redis;


import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisLock
 * @Description redis 分布式锁
 * @Author Long
 * @Date 2021/12/20 11:47
 * @Version 1.0
 */
public class RedisLock {

    RedissonClient redissonClient;


    public RedisLock(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;

    }


    /**
     * @description: 获取 分布式锁
     * @param key 键值
     * @param  seconds 自动失效时间
     * @return  boolean
     * @author Long
     * @date: 2021/12/20 11:50
     */
    public boolean lock(String key,int seconds){
    RLock redisLock = redissonClient.getLock(key);

    if (redisLock.isLocked()){
        return false;
    }
     redisLock.lock(seconds, TimeUnit.SECONDS);
    return true;

    }


    /**
     * @description: 获取 分布式锁
     * @param key 键值
     * @return  boolean
     * @author Long
     * @date: 2021/12/20 11:50
     */
    public boolean lock(String key){
        RLock redisLock = redissonClient.getLock(key);

        if (redisLock.isLocked()){
            return false;
        }
        redisLock.lock();
        return true;

    }


    /**
     * @description: 释放 分布式锁
     * @param
     * @return  void
     * @author Long
     * @date: 2021/12/20 11:56
     */
    public void unLock(String key){
        RLock redisLock = redissonClient.getLock(key);
        if (redisLock.isLocked()) {
            redisLock.unlock();
        }
    }

    /** 
     * @description:
     * @param redisKeyList
     * @return  boolean
     * @author Long
     * @date: 2022/5/12 20:55
     */ 
    public boolean multiLock(List<String> redisKeyList) {

        try {
            RLock multiLock = getMultiLock(redisKeyList);

            return multiLock.tryLock();
        }catch (Exception e){
            throw new UnsupportedOperationException();
        }
    }

    /*** 
     * @description:  联锁解锁
     * @param redisKeyList
     * @return  void
     * @author Long
     * @date: 2022/5/12 21:07
     */ 
    public void unMultiLock(List<String>  redisKeyList){
        RLock rLock = getMultiLock(redisKeyList);
        rLock.unlock();
    }
    
    /** 
     * @description:
     * @param redisKeyList
     * @return  org.redisson.api.RLock
     * @author Long
     * @date: 2022/5/12 20:59
     */ 
    private RLock getMultiLock(List<String> redisKeyList) {
        RLock[] locks = new RLock[redisKeyList.size()];

        for (int i = 0; i < redisKeyList.size(); i++) {
            RLock rLock = redissonClient.getLock(redisKeyList.get(i));
            locks[i] = rLock;
        }

        return redissonClient.getMultiLock(locks);
    }
}
