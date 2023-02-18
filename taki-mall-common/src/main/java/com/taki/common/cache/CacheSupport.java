package com.taki.common.cache;

import com.taki.common.utli.RandomUtil;

/**
 * @ClassName CacheSupportUtils
 * @Description TODO
 * @Author Long
 * @Date 2023/2/18 20:53
 * @Version 1.0
 */
public interface CacheSupport {

    /**
     * 缓存空数据
     */
    String  EMPTY_CACHE = "{}";

     Integer TWO_DAYS_SECONDS = 2 * 24 * 60 * 60;

       Integer ONE_HOURS_SECONDS = 1 * 60 * 60 ;


    /*** 
     * @description: 生成缓存穿透 过期时间 单位 秒
     * @param 
     * @return  java.lang.Integer
     * @author Long
     * @date: 2023/2/18 20:57
     */ 
    static Integer generateCachePenetrationExpireSecond() {

        return RandomUtil.genRandInt(30,100);

    }

    static Integer generateCacheExpireSecond(){
        return TWO_DAYS_SECONDS + RandomUtil.genRandInt(0,10) * 60 * 10;
    }
}
