package com.taki.common.config;

;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.taki.common.constants.DateFormatConstants;
import org.apache.commons.lang3.StringUtils;
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
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

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
            //设置 LocalDateTime 序列化
            builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer());
            // 设置LocalDateTime 反序列化
            builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer());
            // 设置序列化时字段为null也展示
            builder.serializationInclusion(JsonInclude.Include.ALWAYS);
            //POJO 对象的属性值为“” 时 序列化 不进行展示
            builder.serializationInclusion(JsonInclude.Include.NON_EMPTY);
            //DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES相当于配置，JSON串含有未知字段时，反序列化依旧可以成功
            builder.failOnUnknownProperties(false);
            //针对于Date类型，文本格式化
            builder.simpleDateFormat("yyyy-MM-dd HH:mm:ss");

            builder.propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);


            //默认开启，若map的value为null，则不对map条目进行序列化。。
            //(已废弃)
           // builder.featuresToDisable(SerializationFeature.WRITE_NULL_MAP_VALUES);
           builder.timeZone(TimeZone.getTimeZone(DateFormatConstants.DEFAULT_TIME_ZONE));
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

            String text = p.getText();
            // 时间戳转换
            if (StringUtils.isNumeric(text) && timestamp > 0){
                return LocalDateTimeUtil.of(timestamp, ZoneOffset.of("+8"));
            }
            // yyyy-MM-dd HH:mm:ss 日期格式转换
            return LocalDateTimeUtil.parse(p.getText(),DateTimeFormatter.ofPattern(DateFormatConstants.DATE_TIME_FORMAT_PATTERN));
        }
    }
}
