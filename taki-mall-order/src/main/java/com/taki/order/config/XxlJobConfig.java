package com.taki.order.config;

import com.xxl.job.core.executor.impl.XxlJobSimpleExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName XxlJobConfig
 * @Description XXL-JOB 配置
 * @Author Long
 * @Date 2022/6/21 17:25
 * @Version 1.0
 */
@Configuration
@Slf4j
public class XxlJobConfig {

    @Value("${xxl.job.admin.addresses}")
    private String adminAddress;

    @Value("${xxl.job.executor.appname}")
    private String appName;

    @Bean
    public XxlJobSimpleExecutor xxlJobSimpleExecutor() {
        log.info(">>>>> xxl-job config init ");

        XxlJobSimpleExecutor xxlJobSimpleExecutor = new XxlJobSimpleExecutor();
        xxlJobSimpleExecutor.setAdminAddresses(adminAddress);
        xxlJobSimpleExecutor.setAppname(appName);
        return xxlJobSimpleExecutor;


    }



}
