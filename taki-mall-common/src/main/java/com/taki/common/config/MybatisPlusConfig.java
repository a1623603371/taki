package com.taki.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.type.LocalDateTimeTypeHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Configuration
@MapperScan("com.taki.*.mapper")
public class MybatisPlusConfig {

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /***
     * @description: 处理 java8 时间处理类
     * @param
     * @return
     * @author Long
     * @date: 2022/8/6 19:59
     */
    @Bean
    public LocalDateTimeTypeHandler localDateTimeTypeHandler() {

        return new LocalDateTimeTypeHandler() {
            @Override
            public LocalDateTime getResult(ResultSet rs, String columnName) throws SQLException {
                Object object = rs.getObject(columnName);

                if (object instanceof java.sql.Timestamp) {//在这里强行转换，将sql的时间转换为LocalDateTime
                    return LocalDateTime//可以根据自己的需要进行转化
                            .ofInstant(((Timestamp) object).toInstant(), ZoneOffset.ofHours(0));
                }
                return super.getResult(rs, columnName);
            }

        };

    }


//    @Bean
//    public ConfigurationCustomizer configurationCustomizer() {
//        return configuration -> configuration  //configuration.setUseDeprecatedExecutor(false) ;
//    }
}