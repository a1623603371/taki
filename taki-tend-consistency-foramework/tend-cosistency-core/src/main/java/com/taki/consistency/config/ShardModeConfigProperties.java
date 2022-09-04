package com.taki.consistency.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName ShardModeConfigProperties
 * @Description 任务分库相关的配置
 * @Author Long
 * @Date 2022/9/4 13:42
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "taki.tend.consistency.shard")
public class ShardModeConfigProperties {


    /**
     * 任务表是否进行分库
     */
    public Boolean taskShared = false;


    /**
     *生成任务表分片key的ClassName 这里 配置类型全路径且类要 实现 com.taki.consistency.custom.shard.ShardingKeyGenerator 接口
     */
    private String shardingKeyGeneratorClassName = "";
}
