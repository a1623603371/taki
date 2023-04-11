package com.taki.process.engine.annoations;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName ComponenetScanConfig
 * @Description TODO
 * @Author Long
 * @Date 2023/4/11 21:26
 * @Version 1.0
 */
@Configuration
@ComponentScan(value = {"com.taki.process.engine.instance"})
public class ComponentScanConfig {
}
