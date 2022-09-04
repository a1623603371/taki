package com.taki.consistency.annotation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName ComponentScanConfig
 * @Description 组件扫描配置
 * @Author Long
 * @Date 2022/9/4 19:45
 * @Version 1.0
 */
@Configuration
@ComponentScan(value = {"com.taki.consistency"}) //作用是让spring去扫描框架各个包下的bean
@MapperScan(basePackages = {"com.taki.consistency.mapper"})
public class ComponentScanConfig {
}
