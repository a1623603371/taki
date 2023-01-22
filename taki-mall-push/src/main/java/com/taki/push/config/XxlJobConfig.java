package com.taki.push.config;

import com.xxl.job.core.executor.impl.XxlJobSimpleExecutor;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName XxlJobConfig
 * @Description
 * @Author Long
 * @Date 2022/10/5 20:49
 * @Version 1.0
 */
@Slf4j
@Configuration
public class XxlJobConfig {


    @Value("xxl.job.admin.address")
    private String adminAddress;


    @Value("xxl.job.executor.appname")
    private String appname;

    @Value("xxl.job.executor.port")
    private int port;



    @Bean("xxlJobSpringExecutor")
    public XxlJobSpringExecutor xxlJobSpringExecutor(){
        log.info(">>>>>>>>>> xxl-job config init.");

        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddress);
        xxlJobSpringExecutor.setAppname(appname);
        xxlJobSpringExecutor.setPort(port);
        return xxlJobSpringExecutor;

    }
}
