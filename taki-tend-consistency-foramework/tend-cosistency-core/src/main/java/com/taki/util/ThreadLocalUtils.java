package com.taki.util;

/**
 * @ClassName ThreadLocalUtils
 * @Description 当前线程的标记位
 * @Author Long
 * @Date 2022/9/1 23:24
 * @Version 1.0
 */
public class ThreadLocalUtils {

    /**
     * 任务表示Action 被AOP 拦截的时候是不是应该立即执行，不在创建任务
     */
    private static final    ThreadLocal<Boolean> FLAG = ThreadLocal.withInitial(()->false);


    /**
     * 设置为true
     * @param flag
     */

    public static void setFlag(Boolean  flag) {FLAG.set(flag);}


    /**
     * 获取是否为调用器在执行任务的标识
     * @return 是否调度器在执行任务
     */
    public static Boolean getFlag(){return FLAG.get();}


}
