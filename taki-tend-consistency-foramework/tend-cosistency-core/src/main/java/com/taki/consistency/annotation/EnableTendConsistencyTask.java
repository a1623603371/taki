package com.taki.consistency.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启动一致性任务框架注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
@Import(ConsistencyTaskSelector.class)
public @interface EnableTendConsistencyTask {
}
