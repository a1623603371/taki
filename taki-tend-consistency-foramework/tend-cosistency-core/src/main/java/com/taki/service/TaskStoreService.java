package com.taki.service;

import com.taki.model.ConsistencyTaskInstance;

import java.util.List;

/**
 * @ClassName TaskStoreService
 * @Description 一致性任务存储 service接口
 * @Author Long
 * @Date 2022/8/31 21:38
 * @Version 1.0
 */
public interface TaskStoreService {


    /***
     * @description: 保存 一致性任务
     * @param consistencyTaskInstance 一致性任务实例
     * @return  java.lang.Boolean
     * @author Long
     * @date: 2022/8/31 21:40
     */
    void initTask(ConsistencyTaskInstance consistencyTaskInstance);



    /*** 
     * @description: 根据id获取任务实例信息
     * @param: id 任务id
     * * @param: shardKey 分片 key
     * @return  
     * @author Long
     * @date: 2022/8/31 21:41
     */

    ConsistencyTaskInstance getTaskByIdAndShardKey(Long id,String shardKey);


    /*** 
     * @description:  获取未完成的任务
     * @param 
     * @return  未完成任务集合
     * @author Long
     * @date: 2022/8/31 21:43
     */ 
    List<ConsistencyTaskInstance> listByUnFinishTask();


    /*** 
     * @description:  启动任务
     * @param consistencyTaskInstance 一致性任务实例信息
     * @return  java.lang.Boolean
     * @author Long
     * @date: 2022/8/31 21:44
     */ 
    Boolean turnOnTask(ConsistencyTaskInstance consistencyTaskInstance);


    /***
     * @description:  标记完成任务
     * @param consistencyTaskInstance 一致性任务实例信息
     * @return  java.lang.Boolean
     * @author Long
     * @date: 2022/8/31 21:45
     */
    Boolean markSuccess(ConsistencyTaskInstance consistencyTaskInstance);



    /***
     * @description: 标记任务失败
     * @param consistencyTaskInstance 一致性任务实例信息
     * @return  java.lang.Boolean
     * @author Long
     * @date: 2022/8/31 21:46
     */
    Boolean markFail(ConsistencyTaskInstance consistencyTaskInstance);

    /***
     * @description: 标记任务降级
     * @param consistencyTaskInstance 一致性任务实例信息
     * @return  java.lang.Boolean
     * @author Long
     * @date: 2022/8/31 21:45
     */
    Boolean markFallbackFail(ConsistencyTaskInstance consistencyTaskInstance);


    /*** 
     * @description:  提交任务 实例信息
     * @param consistencyTaskInstance 一致性任务实例信息
     * @return  java.lang.Boolean
     * @author Long
     * @date: 2022/8/31 21:47
     */ 
    Boolean submitTaskInstance(ConsistencyTaskInstance consistencyTaskInstance);


}
