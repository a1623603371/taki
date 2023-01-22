package com.taki.push.common.concurrent;

import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName SafeThreadPool
 * @Description TODO
 * @Author Long
 * @Date 2022/10/5 19:32
 * @Version 1.0
 */
public class SafeThreadPool {

    private final Semaphore semaphore;


    private ThreadPoolExecutor threadPoolExecutor;


    public SafeThreadPool(String  name,int permits) {
        // 如果超过了100个任务同时运行，会通过semaphore信号量阻塞
        this.semaphore = new Semaphore(permits);

        //
        threadPoolExecutor = new ThreadPoolExecutor(0
                ,permits * 2,
                60,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),NamedDaemonThreadFactory.getInstance(name));
    }


    public void execute(Runnable  task){

        semaphore.acquireUninterruptibly();

        threadPoolExecutor.submit(()->{
            try {
                task.run();
            }finally {
            semaphore.release();
            }

        });
    }
}
