package com.taki.demo.schedule;

import com.taki.consistency.manager.TaskScheduleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @ClassName Scheduler
 * @Description TODO
 * @Author Long
 * @Date 2022/9/9 16:50
 * @Version 1.0
 */
@Slf4j
@Component
public class Scheduler {

    /**
     * 一致性任务调度器
     */
    @Autowired
    private TaskScheduleManager taskScheduleManager;


    /*** 
     * @description:  对一致性任务进行调度
     * 1. 如果使用的是分布式任务调度框架（如：xxl-job）则配置相关策略保证多实例情况下，同一时刻只有一个实例可以调用performanceTask方法
     * 2. 如果像我们这里，使用的是spring自带的 定时任务，则记得加锁，防止多实例重复提交，保证同一时刻只有一个实例运行
     * @param
     * @return  void
     * @author Long
     * @date: 2022/9/9 16:51
     */ 
    @Scheduled(fixedRate = 5 * 1000L)
    public void execute(){
        try {
            taskScheduleManager.performanceTask();
        }catch (Exception e){
            log.error("一致性任务调度时，发送异常",e);
        }
    }
}
