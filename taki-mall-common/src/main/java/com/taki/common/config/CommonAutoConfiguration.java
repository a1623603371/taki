package com.taki.common.config;

import com.taki.common.bean.SpringApplicationContext;
import com.taki.common.redis.RedisLock;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @ClassName CommonAutoConfiguration
 * @Description  工具自动主人配置
 * @Author Long
 * @Date 2022/1/15 15:10
 * @Version 1.0
 */
@Configuration
@Import({RedisLock.class, SpringApplicationContext.class})
public class CommonAutoConfiguration {
}
