package com.taki.push.common.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName NamedDaemonThreadFactory
 * @Description TODO
 * @Author Long
 * @Date 2022/10/5 18:01
 * @Version 1.0
 */
public class NamedDaemonThreadFactory implements ThreadFactory {


    private final String name;

    private final AtomicInteger counter = new AtomicInteger(0);


    public  NamedDaemonThreadFactory(String name) {
        this.name = name;
    }

    public static   NamedDaemonThreadFactory getInstance(String name){

        Objects.requireNonNull(name,"必须要传一个线程名称的前缀");

        return new NamedDaemonThreadFactory(name);
    }

    @Override
    public Thread newThread(@NotNull Runnable r) {

        Thread thread = new Thread(name + "-" + counter.incrementAndGet());

        thread.setDaemon(true);

        return thread;
    }
}
