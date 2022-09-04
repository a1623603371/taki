package com.taki.consistency.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName TendConsistencyFallbackConfigProperties
 * @Description 一致性任务降级的相关配置
 * @Author Long
 * @Date 2022/9/4 13:48
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "taki.tend.consistency.action")
public class TendConsistencyFallbackConfigProperties {

    /**
     * 触发降级逻辑 任务执行次数 如果大于该值 就会进行降级
     */
    public Integer failCountThreshold = 0;

}
