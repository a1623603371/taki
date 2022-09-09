package com.taki.consistency.config;

import com.taki.consistency.custom.query.TaskTimeRangeQuery;
import com.taki.consistency.custom.shard.ShardingKeyGenerator;
import com.taki.consistency.exception.ConsistencyException;
import com.taki.consistency.util.DefaultValueUtils;
import com.taki.consistency.util.ReflectTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName TendConsistencyAutoConfiguration
 * @Description 提供给SpringBoot的自动装配类 SPI 使用
 * @Author Long
 * @Date 2022/9/4 13:46
 * @Version 1.0
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(value = {
        TendConsistencyParallelTaskConfigProperties.class,
        TendConsistencyFallbackConfigProperties.class,
        ShardModeConfigProperties.class
})
public class TendConsistencyAutoConfiguration {

    /**
     * 执行调度任务的线程池的配置
     */
    @Autowired
    private TendConsistencyParallelTaskConfigProperties consistencyParallelTaskConfigProperties;

    /**
     * 降级逻辑相关参数配置
     */
    @Autowired
    private TendConsistencyFallbackConfigProperties consistencyFallbackConfigProperties;

    /**
     * 分片模式参数配置
     */
    @Autowired
    private ShardModeConfigProperties shardModeConfigProperties;

    /*** 
     * @description:  框架配置
     * @param 
     * @return  com.taki.consistency.config.TendConsistencyConfiguration
     * @author Long
     * @date: 2022/9/4 14:20
     */
    @Bean
    public TendConsistencyConfiguration tendConsistencyConfiguration() {
        //对配置进行检查
        doConfigCheck(consistencyParallelTaskConfigProperties,shardModeConfigProperties);


        return TendConsistencyConfiguration.builder()
                .threadCorePoolSize(DefaultValueUtils.getOrDefault(consistencyParallelTaskConfigProperties.getThreadCorePoolSize(),5))
                .threadMaxPoolSize(DefaultValueUtils.getOrDefault(consistencyParallelTaskConfigProperties.getThreadMaxPoolSize(),5))
                .threadPoolQueueSize(DefaultValueUtils.getOrDefault(consistencyParallelTaskConfigProperties.getThreadPoolQueueSize(),100))
                .threadPoolKeepAliveTime(DefaultValueUtils.getOrDefault(consistencyParallelTaskConfigProperties.getThreadPoolKeepAliveTime(),60L))
                .threadPoolKeepAliveUnit(DefaultValueUtils.getOrDefault(consistencyParallelTaskConfigProperties.getThreadPoolKeepAliveTimeUnit(),"SECONDS"))
                .failCountThreshold(DefaultValueUtils.getOrDefault(consistencyFallbackConfigProperties.getFailCountThreshold(),2))
                .taskSharded(DefaultValueUtils.getOrDefault(shardModeConfigProperties.taskShared,false))
                .shardingKeyGeneratorClassName(DefaultValueUtils.getOrDefault(shardModeConfigProperties.getShardingKeyGeneratorClassName(),"")).build();
    }


    /*** 
     * @description:  配置检查
     * @param consistencyParallelTaskConfigProperties 并发任务相关的配置
     * @param shardModeConfigProperties 分片模式相关配置
     * @return  void
     * @author Long
     * @date: 2022/9/4 14:21
     */ 
    private void doConfigCheck(TendConsistencyParallelTaskConfigProperties consistencyParallelTaskConfigProperties,  ShardModeConfigProperties shardModeConfigProperties) {

        TimeUnit timeUnit = null;

        if (!StringUtils.isEmpty(consistencyParallelTaskConfigProperties.getThreadPoolKeepAliveTimeUnit())) {
            try {
                timeUnit = TimeUnit.valueOf(consistencyParallelTaskConfigProperties.threadPoolKeepAliveTimeUnit);
            }catch (IllegalArgumentException e){
                log.error("检查threadPoolKeepAliveTimeUnit 配置时，发送异常:{}",e);
                String errMsg = "threadPoolKeepAliveTimeUnit 配置错误！注意：请在{SECONDS,MINUTES,HOURS,DAYS,NANOSECONDS,MICROSECONDS,MILLISECONDS}之间选择其一";
                throw new ConsistencyException(errMsg);
            }
        }

        if (!StringUtils.isEmpty(consistencyParallelTaskConfigProperties.getTaskScheduleTimeRangeClassName())){

            //校验是否存在该类
            Class<?> taskScheduleTimeRangeClass = ReflectTools.checkClassByName(consistencyParallelTaskConfigProperties.getTaskScheduleTimeRangeClassName());
            if (ObjectUtils.isEmpty(taskScheduleTimeRangeClass)){
                String errMsg = String.format("未找到 %s 类，请检查类路径是否正确",consistencyParallelTaskConfigProperties.getTaskScheduleTimeRangeClassName());
                throw new ConsistencyException(errMsg);
            }
            Boolean result = ReflectTools.isRealizeTargetInterface(taskScheduleTimeRangeClass, TaskTimeRangeQuery.class.getName());

            if (!result){
                String errMsg = String.format("%s 类，未实现TaskTimeRangeQuery接口",consistencyParallelTaskConfigProperties.getTaskScheduleTimeRangeClassName());
                throw new ConsistencyException(errMsg);
            }

        }

        if (!StringUtils.isEmpty(shardModeConfigProperties.getShardingKeyGeneratorClassName())){

            //校验是否存在该类
            Class<?> shardingKeyGeneratorClass =  ReflectTools.checkClassByName(shardModeConfigProperties.getShardingKeyGeneratorClassName());
            if (ObjectUtils.isEmpty(shardingKeyGeneratorClass)){
                String errMsg = String.format("未找到%s 类，请检查路径是否正确",shardModeConfigProperties.getShardingKeyGeneratorClassName());
                throw new ConsistencyException(errMsg);
            }

            // 用户自定义校验：校验是否实现了ShardingKeyGenerator接口
            Boolean result = ReflectTools.isRealizeTargetInterface(shardingKeyGeneratorClass, ShardingKeyGenerator.class.getName());

            if (!result){
                String errMsg = String.format("%s 类，未实现ShardingKeyGenerator 接口",shardModeConfigProperties.getShardingKeyGeneratorClassName());
                throw new ConsistencyException(errMsg);
            }
        }




    }
}
