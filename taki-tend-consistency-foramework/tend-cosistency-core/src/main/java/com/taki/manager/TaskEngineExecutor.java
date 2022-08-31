package com.taki.manager;

import com.taki.model.ConsistencyTaskInstance;

/**
 * @ClassName TaskEngineExecutor
 * @Description 任务执行引擎接口
 * @Author Long
 * @Date 2022/8/31 22:38
 * @Version 1.0
 */
public interface TaskEngineExecutor {



    /*** 
     * @description:  执行指定任务实例
     * @param consistencyTaskInstance 一致性任务信息实例
     * @return  void
     * @author Long
     * @date: 2022/8/31 22:39
     */ 
    void executeTaskInstance(ConsistencyTaskInstance consistencyTaskInstance);

    /** 
     * @description: 执行 任务失败的时候，执行逻辑
     * @param consistencyTaskInstance
     * @return  void
     * @author Long
     * @date: 2022/8/31 22:41
     */ 
    void fallbackExecuteTask(ConsistencyTaskInstance consistencyTaskInstance);
}
