package com.taki.fulfill.saga;

import com.alibaba.druid.pool.DruidDataSource;
import io.seata.saga.engine.config.DbStateMachineConfig;
import io.seata.saga.engine.impl.ProcessCtrlStateMachineEngine;
import io.seata.saga.rm.StateMachineEngineHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;

import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName StateMachineConfiguration
 * @Description TODO
 * @Author Long
 * @Date 2022/5/17 16:15
 * @Version 1.0
 */
@Configuration
public class StateMachineConfiguration {


    @Bean
    public ThreadPoolExecutorFactoryBean threadPoolExecutor(){
        ThreadPoolExecutorFactoryBean threadExecutor = new ThreadPoolExecutorFactoryBean();
        threadExecutor.setThreadGroupName("SAGA_ASYNC_EXE_");
        threadExecutor.setCorePoolSize(2);
        threadExecutor.setMaxPoolSize(20);

        return threadExecutor;
    }


    @Bean
    public DbStateMachineConfig dbStateMachineConfig(ThreadPoolExecutorFactoryBean threadPoolExecutor, DruidDataSource  druidDataSource) throws IOException {

    DbStateMachineConfig dbStateMachineConfig = new DbStateMachineConfig();

    dbStateMachineConfig.setDataSource(druidDataSource);

    dbStateMachineConfig.setThreadPoolExecutor((ThreadPoolExecutor) threadPoolExecutor.getObject());

    dbStateMachineConfig.setResources(new PathMatchingResourcePatternResolver().getResources("classpath:statelang/*.json"));
    dbStateMachineConfig.setEnableAsync(true);

    dbStateMachineConfig.setTxServiceGroup("taki-eshop-fulfill-group");

    return dbStateMachineConfig;


    }

    @Bean
    public ProcessCtrlStateMachineEngine stateMachineEngineHolder(DbStateMachineConfig dbStateMachineConfig){

        ProcessCtrlStateMachineEngine processCtrlStateMachineEngine = new ProcessCtrlStateMachineEngine();

        processCtrlStateMachineEngine.setStateMachineConfig(dbStateMachineConfig);

        return processCtrlStateMachineEngine;
    }

    @Bean
    public  StateMachineEngineHolder    stateMachineEngineHolder (ProcessCtrlStateMachineEngine processCtrlStateMachineEngine){

        StateMachineEngineHolder stateMachineEngineHolder = new StateMachineEngineHolder() ;

        stateMachineEngineHolder.setStateMachineEngine(processCtrlStateMachineEngine);

        return stateMachineEngineHolder;


    }
}
