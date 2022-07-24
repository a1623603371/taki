package com.taki.common.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName DefaultWebMvcConfigurer
 * @Description 默认 的 MVC 相关配置
 * @Author Long
 * @Date 2022/6/9 14:24
 * @Version 1.0
 */
@Configuration
public class DefaultWebMvcConfigurer implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new GlobalWebMvcInterceptor()).addPathPatterns("/**");
    }
}
