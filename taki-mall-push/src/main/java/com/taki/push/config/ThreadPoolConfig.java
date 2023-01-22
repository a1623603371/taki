package com.taki.push.config;

import com.taki.push.common.concurrent.SafeThreadPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName ThreadPoolConfig
 * @Description TODO
 * @Author Long
 * @Date 2022/10/5 17:22
 * @Version 1.0
 */
@Configuration
public class ThreadPoolConfig {


    /*** 
     * @description:  发送消息共用的线程池
     *
     * @param
     * @return  com.taki.push.common.concurrent.SafeThreadPool
     * @author Long
     * @date: 2022/10/5 19:41
     */
    @Bean("sharedSendMsgThreadPool")
    public SafeThreadPool sharedSendMsgThreadPool(){

        return new SafeThreadPool("sharedSendMsgThreadPool",30);


    }
}
