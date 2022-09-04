package com.taki.consistency.annotation;

import com.taki.consistency.enums.PerformanceEnum;
import com.taki.consistency.enums.ThreadWayEnum;

import java.lang.annotation.*;

/**
 * 最终一致性执行器注解
 * @author long
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.METHOD)
public @interface ConsistencyTask {

    /**
     * 任务名称
     * @return 任务名称
     */
    String id() default "";

    /**
     * 执行间隔默认 60s
     * @return
     */
    int executeIntervalSec() default 60;

    /**
     * 初始化延迟时间 单位秒
     * @return 执行任务的延迟时间
     */
    long delayTime() default 60L;

    /**
     * 告警表达式，如果满足告警表达式会执行相关操作
     * @return 告警表达式
     */
    String alertExpression() default "executeTimes > 1 && executeTimes < 5";

    /**
     * 告警的动作执行实现类的beanName 需要实现 ConsistencyFramworkAlerter方法 并注入spring 容器
     * @return 告警的动作执行实现类的beanname
     */
    String alertActionBeanName() default "";

    /**
     * 降级方法的class类，注：需要自定义的降级类中，实现与被注解的方法一样实现
     * @return 降级方法的Class类
     */
    Class<?> fallbackClass() default void.class;

    /**
     * 执行模式
     * @return
     */
    PerformanceEnum performanceWay() default PerformanceEnum.PERFORMANCE_RIGHT_NOW;

    /**
     *  线程模型
     * @return 异步执行
     */
    ThreadWayEnum threadWay() default ThreadWayEnum.ASYNC;
}
