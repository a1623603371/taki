package com.taki.core.config;

;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * @ClassName JacksonCustomizerConfig
 * @Description 时间日期转换
 * @Author Long
 * @Date 2021/11/27 14:04
 * @Version 1.0
 */
@Configuration
public class JacksonCustomizerConfig {

    /** 
     * @description:  适配自定义序列化和反序列化策略 返回前端指定数据类型
     * @param: 
     * @return: 序列化组件
     * @author Long
     * @date: 2021/11/27 14:06
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer(){

        return builder -> {
            builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer());
            builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer());

        };
    }

    /**
     * @description:  接受前端时间戳转为 LocalDateTime
     * @param:
     * @return: 序列化组件
     * @author Long
     * @date: 2021/11/27 14:06
     */
    @Bean
    public Converter<String,LocalDateTime> localDateTimeConverter(){

        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String value) {
                return LocalDateTimeUtil.of(Long.valueOf(value), ZoneId.of("+8"));
            }

        };
    }

    /** 
     * @description:  date 转换 接受前端时间戳转为 date
     * @param: 
     * @return: 转换器
     * @author Long
     * @date: 2021/11/27 14:23
     */
    @Bean
    public  Converter<String, Date> dateConverter(){

        return new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                Long timeStamp = Long.valueOf(source);

                if (timeStamp > 0){
                    return new Date(timeStamp);
                }
                return null;
            }
        };
    }


    /** 
     * @description: 序列化
     *    序列化为毫秒级别
     * @author Long
     * @date: 2021/11/27 14:12
     */ 
    public static  class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value !=null){
                Long timestamp = LocalDateTimeUtil.toEpochMilli(value);
                gen.writeNumber(timestamp);
            }
        }
    }

    /**
     * @description: 反序列化
     * @author Long
     * @date: 2021/11/27 14:14
     */
    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

            long timestamp = p.getValueAsLong();

            if (timestamp > 0){
                return LocalDateTimeUtil.of(timestamp, ZoneOffset.of("+8"));
            }

            return null;
        }
    }
}
