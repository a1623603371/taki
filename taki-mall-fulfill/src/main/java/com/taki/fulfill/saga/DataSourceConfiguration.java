package com.taki.fulfill.saga;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * @ClassName DataSourceConfiguration
 * @Description TODO
 * @Author Long
 * @Date 2022/5/17 16:11
 * @Version 1.0
 */
public class DataSourceConfiguration {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DruidDataSource druidDataSource(){
        return new DruidDataSource();
    }

    @Primary
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager (@Qualifier("druidDataSource") DruidDataSource druidDataSource){

        return new DataSourceTransactionManager(druidDataSource);

    }
}
