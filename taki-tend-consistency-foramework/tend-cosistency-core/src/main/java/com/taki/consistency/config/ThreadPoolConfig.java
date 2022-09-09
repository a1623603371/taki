package com.taki.consistency.config;

import com.taki.consistency.model.ConsistencyTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName ThreadPoolConfig
 * @Description 框架线程池相关配置
 * @Author Long
 * @Date 2022/9/4 13:59
 * @Version 1.0
 */
@Configuration
public class ThreadPoolConfig {

    /**
     * 一致性任务线程名称前缀
     */
    private static final String CONSISTENCY_TASK_THREAD_POOL_PREFIX = "CTThreadPool_";

    /**
     * 告警线程名称前缀
     */
    private static final    String ALERT_THREAD_POOL_PREFIX = "AlertThreadPool_";

    /**
     * 获取框架级的配置
     */
    @Autowired
    private TendConsistencyConfiguration tendConsistencyConfiguration;


    /*** 
     * @description: 一致性任务执行的并行任务执行线程池
     * @param 
     * @return  java.util.concurrent.CompletionService<com.taki.consistency.model.ConsistencyTaskInstance>
     * @author Long
     * @date: 2022/9/4 14:04
     */
    @Bean
    public CompletionService<ConsistencyTaskInstance> consistencyTaskPool(){
        LinkedBlockingQueue<Runnable> asyncConsistencyTaskThreadPoolQueue =
                new LinkedBlockingQueue<>(tendConsistencyConfiguration.getThreadPoolQueueSize());
        ThreadPoolExecutor asyncReleaseResourceExecutorPool = new ThreadPoolExecutor(
                tendConsistencyConfiguration.getThreadCorePoolSize(),
                tendConsistencyConfiguration.getThreadCorePoolSize(),
                tendConsistencyConfiguration.getThreadPoolKeepAliveTime(),
                TimeUnit.valueOf(tendConsistencyConfiguration.getThreadPoolKeepAliveUnit()),
                asyncConsistencyTaskThreadPoolQueue,
                createThreadFactory(CONSISTENCY_TASK_THREAD_POOL_PREFIX)
        );
        return new ExecutorCompletionService<>(asyncReleaseResourceExecutorPool);
    }

    
    /*** 
     * @description:  用于告警通知的线程池
     * @param 
     * @return  并行任务线程池
     * @author Long
     * @date: 2022/9/4 14:16
     */
    @Bean
    public ThreadPoolExecutor alertNoticePool(){
        LinkedBlockingQueue<Runnable> asyncAlertNoticeThreadPoolQueue = new LinkedBlockingQueue<>(100);

        return new ThreadPoolExecutor(
                3,
                5,
                60,
                TimeUnit.SECONDS,
                asyncAlertNoticeThreadPoolQueue,
                createThreadFactory(ALERT_THREAD_POOL_PREFIX));
    }

    /*** 
     * @description:  创建线程池工厂
     * @param threadPoolPrefix 线程池的前缀
     * @return  线程池工厂
     * @author Long
     * @date: 2022/9/4 14:08
     */ 
    private ThreadFactory createThreadFactory(String threadPoolPrefix) {


        return new ThreadFactory() {

            private AtomicInteger threadIndex = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,threadPoolPrefix +this.threadIndex.incrementAndGet()) ;
            }
        };
    }


}
