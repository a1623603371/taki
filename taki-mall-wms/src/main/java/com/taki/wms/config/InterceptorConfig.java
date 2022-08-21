package com.taki.wms.config;

import com.taki.wms.Interceptor.MyInterceptor;
import com.taki.wms.Interceptor.MyLoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName InterceptorConfig
 * @Description TODO
 * @Author Long
 * @Date 2022/8/21 10:59
 * @Version 1.0
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyLoginInterceptor()).order(0)
                .addPathPatterns("/**");
    }
}
