package com.taki.consistency.mapper;

import com.taki.consistency.model.ConsistencyTaskInstance;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @ClassName TaskStoreMapper
 * @Description 一致性任务数据mapper 接口
 * @Author Long
 * @Date 2022/8/22 21:47
 * @Version 1.0
 */
@Mapper
public interface TaskStoreMapper {



    /** 
     * @description: 保存 最终一致性 任务实例
     * @param consistencyTaskInstance 存储最终一致性任务实例信息
     * @return  java.lang.Long
     * @author Long
     * @date: 2022/8/22 22:15
     */
    @Insert("INSERT INTO taki_tend_consistency_task ("
            + "task_id,"
            + "task_status,"
            + "execute_times,"
            + "execute_time,"
            + "parameter_types,"
            + "method_name,"
            + "method_sign_name,"
            + "exeucete_interval_sec,"
            + "delay_time,"
            + "task_parameter,"
            + "performance_way,"
            + "thread_way,"
            + "error_msg,"
            + "alert_expression,"
            + "alert_action_bean_name,"
            + "fallback_class_name,"
            + "fallback_err_msg,"
            + "shard_key,"
            + "create_time,"
            + "update_time)"
            + "VALUES("
            + "#{taskId},"
            + "#{taskStatus},"
            + "#{executeTimes},"
            + "#{executeTime},"
            + "#{parameterTypes},"
            + "#{methodName},"
            + "#{methodSignName},"
            + "#{exeuceteIntervalSec},"
            + "#{delayTime},"
            + "#{taskParameter},"
            + "#{performanceWay},"
            + "#{threadWay},"
            + "#{errorMsg},"
            + "#{alertExoression},"
            + "#{alertActionBeanName},"
            + "#{fallbackClassName},"
            + "#{fallbackErrorMsg},"
            + "shardKey,"
            + "createTime," +
            "updateTime)")
    @Options(keyColumn = "id",keyProperty = "id",useGeneratedKeys = true)
    Boolean initTask(ConsistencyTaskInstance consistencyTaskInstance);


    /***
     * @description: 根据 id 和 分片key 查询 分布式任务实例
     * @param id 任务id
     * @param shardKey 分片键 key
     * @return
     * @author Long
     * @date: 2022/8/30 21:36
     */
    @Select("SELECT " +
            "id,task_id,task_status,execute_times,execute_time,parameter_types,method_name,method_sign_name," +
            "execute_interval_sec,delay_time,task_parameter,performance_way," +
            "thread_way,err_msg,alert_expression," +
            "alert_action_bean_name,fallback_class_name,fallack_error_msg,shard_key,create_time,update_time " +
            "FROM taki_tend_consistency_task " +
            "WHERE id = #{id} AND shard_key = #{shardKey}")
    @Results({
            @Result(column = "id",property = "id",id = true),
            @Result(column = "task_id",property = "taskId"),
            @Result(column = "task_status",property = "taskStatus"),
            @Result(column = "execute_status",property = "executeStatus"),
            @Result(column = "execute_times",property = "executeTimes"),
            @Result(column = "execute_time",property = "executeTime"),
            @Result(column = "parameter_types",property = "parameterTypes"),
            @Result(column = "method_name",property = "methodName"),
            @Result(column = "method_sign_name",property = "methodSignName"),
            @Result(column = "execute_interval_sec",property = "executeIntervalSec"),
            @Result(column = "delay_time",property = "delayTime"),
            @Result(column = "bean_class_name",property = "beanClassName"),
            @Result(column = "task_parameter",property = "taskParameter"),
            @Result(column = "performance_way",property = "performanceWay"),
            @Result(column = "thread_way",property = "threadWay"),
            @Result(column = "error_msg",property = "errorMsg"),
            @Result(column = "alert_expression",property = "alertExpression"),
            @Result(column = "alert_action_bean_name",property = "alertActionBeanName"),
            @Result(column = "fallback_class_name",property = "fallbackClassName"),
            @Result(column = "fallback_error_msg",property = "fallbackErrorMsg"),
            @Result(column = "shard_key",property = "shardKey"),
            @Result(column = "create_time",property = "createTime"),
            @Result(column = "update_time",property = "updateTime")
    })
    ConsistencyTaskInstance getTaskByIdAndShardKey(@Param("id") Long id, @Param("shardKey") Long shardKey);




    /***
     * @description:  查询 来完成的任务
     * @param startTime 开始时间
     * @param  endTime 结束时间
     * @param  limitTaskCount 每次查询限制次数
     * @return
     * @author Long
     * @date: 2022/8/30 22:18
     */



    @Select("SELECT " +
            "id,task_id,task_status,execute_times,execute_time,parameter_types,method_name,method_sign_name," +
            "execute_interval_sec,delay_time,task_parameter,performance_way," +
            "thread_way,err_msg,alert_expression," +
            "alert_action_bean_name,fallback_class_name,fallack_error_msg,shard_key,create_time,update_time " +
            "FROM taki_tend_consistency_task " +
            "WHERE task_status <= 2 AND execute_time >= #{startTime} AND execute_time <= #{endTime}" +
            "ORDER  BY  execute_time DESC " +
            "LIMIT #{limitTaskCount}")
    @Results({
            @Result(column = "id",property = "id",id = true),
            @Result(column = "task_id",property = "taskId"),
            @Result(column = "task_status",property = "taskStatus"),
            @Result(column = "execute_status",property = "executeStatus"),
            @Result(column = "execute_times",property = "executeTimes"),
            @Result(column = "execute_time",property = "executeTime"),
            @Result(column = "parameter_types",property = "parameterTypes"),
            @Result(column = "method_name",property = "methodName"),
            @Result(column = "method_sign_name",property = "methodSignName"),
            @Result(column = "execute_interval_sec",property = "executeIntervalSec"),
            @Result(column = "delay_time",property = "delayTime"),
            @Result(column = "bean_class_name",property = "beanClassName"),
            @Result(column = "task_parameter",property = "taskParameter"),
            @Result(column = "performance_way",property = "performanceWay"),
            @Result(column = "thread_way",property = "threadWay"),
            @Result(column = "error_msg",property = "errorMsg"),
            @Result(column = "alert_expression",property = "alertExpression"),
            @Result(column = "alert_action_bean_name",property = "alertActionBeanName"),
            @Result(column = "fallback_class_name",property = "fallbackClassName"),
            @Result(column = "fallback_error_msg",property = "fallbackErrorMsg"),
            @Result(column = "shard_key",property = "shardKey"),
            @Result(column = "create_time",property = "createTime"),
            @Result(column = "update_time",property = "updateTime")
    })
    List<ConsistencyTaskInstance> listByUnFinishTask(@Param("startTime") Long startTime,@Param("endTime") Long endTime,@Param("limitTaskCount")Long limitTaskCount);


    /*** 
     * @description: 启动任务
     * @param consistencyTaskInstance 任务实例信息
     * @return  java.lang.Boolean
     * @author Long
     * @date: 2022/8/31 21:05
     */ 
    @Update("UPDATE " +
            "taki_tend_consistency_task " +
            "SET " +
            "task_status = #{taskStatus},execute_times=#{executeTimes},execute_time=#{executeTime} " +
            "WHERE id=#{id} AND task_status != 1 and shard_key =#{shardKey}")
    Boolean turnOnTask(ConsistencyTaskInstance consistencyTaskInstance);


    /*** 
     * @description: 标记任务成功
     * @param consistencyTaskInstance 任务实例信息
     * @return  java.lang.Boolean
     * @author Long
     * @date: 2022/8/31 21:07
     */ 
    @Delete("DELETE FROM taki_tend_consistency_task WHERE id = #{id} AND  shard_key = #{ShardKey}")
    Boolean markSuccess(ConsistencyTaskInstance consistencyTaskInstance);


    /***
     * @description: 标记 任务失败
     * @param consistencyTaskInstance 任务实例信息
     * @return  java.lang.Boolean
     * @author Long
     * @date: 2022/8/31 21:20
     */
    @Update("UPDATE task_tend_consistency_task SET task_status = 2,error_msg = #{errorMsg},execute_time = #{executeTime}")
    Boolean markFail(ConsistencyTaskInstance consistencyTaskInstance);


    /*** 
     * @description:  标记降级失败
     * @param consistencyTaskInstance 任务实例信息
     * @return  java.lang.Boolean
     * @author Long
     * @date: 2022/8/31 21:21
     */ 
    @Update("UPDATE task_tend_consistency_task SET fallback_error_msg = #{fallbackErrorMsg} WHERE id = #{id} AND shard_key = #{ShardKey}  " )
    Boolean markFallbackFail(ConsistencyTaskInstance consistencyTaskInstance);




}
