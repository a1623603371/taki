package com.taki.common.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taki.common.config.ObjectMapperImpl;
import com.taki.common.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/**
 * web相关bean组件配置
 *

 * @version 1.0
 */
@Configuration
@Import(value = {GlobalExceptionHandler.class , GlobalResponseBodyAdvice.class})
public class WebConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapperImpl();
    }

}