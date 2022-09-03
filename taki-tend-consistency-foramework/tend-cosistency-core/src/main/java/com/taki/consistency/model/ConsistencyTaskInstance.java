package com.taki.consistency.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @ClassName ConsistencyTaskInstance
 * @Description 一致性任务的实例信息
 * @Author Long
 * @Date 2022/8/21 21:16
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsistencyTaskInstance {

    private Long id;

    /**
     * 任务Id
     */
    private String taskId;

    /**
     * 方法签名名称：格式 ：类路径#方法名(参数1的类型，参数2的类型，...参数N的类型)
     */
    private String methodSignName;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 参数的类路径类型
     */
    private String parameterTypes;

    /**
     *参数 JSON值
     */
    private String taskParameter;

    /**
     * 任务状态
     */
    private Integer taskStatus;

    /**
     *执行间隔时间默认 60s
     */
    private Integer executeIntervalSec;

    /**
     * 初始化延迟时间
     */
    private Long delayTime;

    /**
     * 执行次数
     */
    private int executeTimes;

    /**
     * 执行时间
     */
    private Long executeTime;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 执行模式
     */
    private Integer performanceWay;

    /**
     * 线程模型
     */
    private Integer threadWay;

    /**
     * 告警表达式
     */
    private String alertExpression;

    /**
     *告警动作执行实现类的beanName，需要实现ConsistencyFrameworkAlerter 方法 并注入spring容器
     */
    private String alertActionBeanName;

    /**
     * 降级类的class
     */
    private String fallbackClassName;

    /**
     * 降级失败时的错误信息
     */
    private String fallbackErrorMsg;

    /**
     * 分片键
     */
    private Long shardKey;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
