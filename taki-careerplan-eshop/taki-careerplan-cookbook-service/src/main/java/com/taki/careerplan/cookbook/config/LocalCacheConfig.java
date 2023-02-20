package com.taki.careerplan.cookbook.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName LocalCacheConfig
 * @Description TODO
 * @Author Long
 * @Date 2023/2/20 18:19
 * @Version 1.0
 */
@Configuration
public class LocalCacheConfig {

    /**
     */
    @Value("${localCache.expireSeconds}")
    private Long expireSeconds;

    @Value("${localCache.initialCapacity}")
    private Integer initialCapacity;

    @Value("${localCache.maxSize}")
    private Long maxSize;


    @Bean
    public Cache<String,Object> caffeineCache (){

        return Caffeine.newBuilder()
                // 初始缓存大小
                .initialCapacity(initialCapacity)
                // 设置最后一次写入或访问经过固定时间过期
                .expireAfterAccess(expireSeconds, TimeUnit.SECONDS)
                // 存储最大条数
                .maximumSize(maxSize).build();
    }
}
