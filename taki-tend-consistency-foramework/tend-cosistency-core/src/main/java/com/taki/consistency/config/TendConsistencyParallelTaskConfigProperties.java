package com.taki.consistency.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName TendConsistencyParallelTaskConfigProperties
 * @Description 控制同时可以有个几个线程进行任务的执行的配置类
 * @Author Long
 * @Date 2022/9/4 13:51
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "taki.tend.consistency.parallel.pool")
public class TendConsistencyParallelTaskConfigProperties {

    /**
     * 调度型任务线程池的核心线程数
     */
    public Integer threadCorePoolSize = 5;

    /**
     * 调度型任务线程的最多线程数
     */
    public Integer threadMaxPoolSize = 5;

    /**
     * 调度任务线程的队列大小
     */
    public Integer threadPoolQueueSize = 100;

    /**
     * 线程池中无任务时线程存活时间
     */
    public  Long threadPoolKeepAliveTime = 60L;

    /**
     * 可选值【SECONDS,MINUTES,HOURS,DAYS,NANOSECONDS,MICROSECONDS,MILLISECONDS】线程池中有无任务时线程存活时间单位
     */
    public String threadPoolKeepAliveTimeUnit ="SECONDS";

    /**
     * 这里要配置全路径且类要实现 com.taki.consistency.custom.query.TaskTimeRangeQuery 接口，如 com.xxx.TaskTimeQuery
     */
    private String taskScheduleTimeRangeClassName = "";
}
