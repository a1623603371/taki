package com.taki.core.mybatis;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.incrementer.H2KeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @ClassName MybatisPlusConfig
 * @Description mybatis_plus 配置
 * @Author Long
 * @Date 2021/12/3 22:14
 * @Version 1.0
 */
@Slf4j
@Component
public class MybatisPlusConfig {



    /** 
     * @description: 设置Sequence主键生成
     * 这里主键生成一般 使用在 主键生成策略为 INPUT 下
     *内置支持：
     * DB2KeyGenerator
     * H2KeyGenerator
     * KingbaseKeyGenerator
     * OracleKeyGenerator
     * PostgreKeyGenerator
     * 如果内置支持不满足你的需求，可实现IKeyGenerator接口来进行扩展
     *
     * 这里我们使用的是数据库自增不开启
     * @param: 
     * @return:
     * @author Long
     * @date: 2021/12/3 22:17
     *
    @Bean
    public IKeyGenerator keyGenerator() {
        return new H2KeyGenerator();
    } */

    @Bean
    public IdentifierGenerator idGenerator() {
        return new CustomIdGenerator();
    }
}
