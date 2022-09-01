package com.taki.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.taki.config.TendConsistencyConfiguration;
import com.taki.custom.TaskTimeRangeQuery;
import com.taki.enums.ConsistencyTaskStatusEnum;
import com.taki.enums.PerformanceEnum;
import com.taki.enums.ThreadWayEnum;
import com.taki.exception.ConsistencyException;
import com.taki.manager.TaskEngineExecutor;
import com.taki.mapper.TaskStoreMapper;
import com.taki.model.ConsistencyTaskInstance;
import com.taki.service.TaskStoreService;
import com.taki.util.ReflectTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionService;

/**
 * @ClassName TaskStoreServiceImpl
 * @Description 一致性任务存储 service接口 实现
 * @Author Long
 * @Date 2022/8/31 21:47
 * @Version 1.0
 */
@Service
@Slf4j
public class TaskStoreServiceImpl implements TaskStoreService {

    /**
     * 任务存储的mapper 组件
     */
   @Autowired
   private TaskStoreMapper taskStoreMapper;

    /**
     * 任务执行池
     */
    @Autowired
   private CompletionService<ConsistencyTaskInstance> consistencyTaskPool;

    /**
     * 一致性框架配置
     */
    @Autowired
    private TendConsistencyConfiguration tendConsistencyConfiguration;

    /**
     * 任务执行器
     */
    @Autowired
    private TaskEngineExecutor taskEngineExecutor;


    @Override
    public void initTask(ConsistencyTaskInstance consistencyTaskInstance) {
        // 直接基于 mybatis的mapper，把我们任务实例数据，给持久化到数据库中
        Boolean result = taskStoreMapper.initTask(consistencyTaskInstance); // 这个是第一个数据库操作的故障点
        log.info("[一致性任务框架] 初始化任务结果 [{}]",result);
        // 如果任务实例数据落库失败，不直接报错，而是把这个任务数据写入磁盘文件中，做一个标记
        //就直接返回
        // 可以让框架开启一个线程，定时去读取这个文件，把一个一个没有落库的任务实例从磁盘文件中读取出来，再次尝试落库和执行

        //如果执行模式是不是立即执行任务
        if(!(PerformanceEnum.PERFORMANCE_RIGHT_NOW.getCode().compareTo(consistencyTaskInstance.getPerformanceWay()) == 0)){
            return ;

        }

        // 判断单前Action是否包含在事务中，如果是，等事务提交后在执行Action
        //固定写的一般逻辑，不是我们来进行配置
        //会使用Spring事务API,判断一下，当前Action执行是否包含在事务里，如果包含在一个事务里的话，不要根其他的事务混合在一起

        Boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();

        if (synchronizationActive){
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    submitTaskInstance(consistencyTaskInstance);
                }
            });
        }else {
                submitTaskInstance(consistencyTaskInstance);
        }


    }

    @Override
    public ConsistencyTaskInstance getTaskByIdAndShardKey(Long id, String shardKey) {
        return taskStoreMapper.getTaskByIdAndShardKey(id,shardKey);
    }

    @Override
    public List<ConsistencyTaskInstance> listByUnFinishTask() {

        LocalDateTime startTime,endTime;

        Long limitTaskCount;

        try {
            // 获取TaskTimeLineQuery 实现类
            if (!StringUtils.isEmpty(tendConsistencyConfiguration.getTaskScheduleTimeRangeClassName())){
                // 获取spring容器所有对应TaskTimeRangeQuery接口实现类
                Map<String, TaskTimeRangeQuery> beansOfTypeMap = SpringUtil.getBeansOfType(TaskTimeRangeQuery.class);
                TaskTimeRangeQuery taskTimeRangeQuery = getTaskTimeLineQuery(beansOfTypeMap);
                startTime = taskTimeRangeQuery.getStartTime();
                endTime = taskTimeRangeQuery.getEndTime();
                limitTaskCount = taskTimeRangeQuery.limitTaskCount();

                return taskStoreMapper.listByUnFinishTask(startTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli(),
                        endTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli(),limitTaskCount);
            }else {
                startTime = TaskTimeRangeQuery.getStartTimeByStatic();
                endTime = TaskTimeRangeQuery.getEndTimeByStatic();
                limitTaskCount = TaskTimeRangeQuery.limitTaskCountByStatic();
            }


        }catch (Exception e){
            log.error("[一致性任务框架]调用业务服务实现具体的的告警通知类时，发生异常",e);
            throw new ConsistencyException(e);
        }



        return taskStoreMapper.listByUnFinishTask(startTime.toInstant(ZoneOffset.ofHours(1)).toEpochMilli(),
                endTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli(),limitTaskCount);
    }
    
    /*** 
     * @description:  获取TaskTimeRangeQuery
     * @param beansOfTypeMap TaskTimeRangeQuery 接口实现类的map集合
     * @return  com.taki.custom.TaskTimeRangeQuery
     * @author Long
     * @date: 2022/8/31 23:53
     */ 
    private TaskTimeRangeQuery getTaskTimeLineQuery(Map<String, TaskTimeRangeQuery> beansOfTypeMap) {
        // 如果只有一个实现类
        if(beansOfTypeMap.size() == 1){
            String [] beanNamesForType = SpringUtil.getBeanNamesForType(TaskTimeRangeQuery.class);
            return  SpringUtil.getBean(beanNamesForType[0]);
        }

        Class<?> clazz = ReflectTools.getClassByName(tendConsistencyConfiguration.getTaskScheduleTimeRangeClassName());

        return (TaskTimeRangeQuery) SpringUtil.getBean(clazz);


    }

    @Transactional(rollbackFor = Exception.class , propagation = Propagation.REQUIRES_NEW)
    @Override
    public Boolean turnOnTask(ConsistencyTaskInstance consistencyTaskInstance) {
        consistencyTaskInstance.setExecuteTime(System.currentTimeMillis()); // 这里是本次任务实际运行的时间，去做一个重置
        consistencyTaskInstance.setTaskStatus(ConsistencyTaskStatusEnum.START.getCode());
        return taskStoreMapper.turnOnTask(consistencyTaskInstance);
    }

    @Override
    public Boolean markSuccess(ConsistencyTaskInstance consistencyTaskInstance) {
        return taskStoreMapper.markSuccess(consistencyTaskInstance);
    }

    @Override
    public Boolean markFail(ConsistencyTaskInstance consistencyTaskInstance) {
        return taskStoreMapper.markFail(consistencyTaskInstance);
    }

    @Override
    public Boolean markFallbackFail(ConsistencyTaskInstance consistencyTaskInstance) {
        return taskStoreMapper.markFallbackFail(consistencyTaskInstance);
    }

    @Override
    public void submitTaskInstance(ConsistencyTaskInstance consistencyTaskInstance) {
        if (ThreadWayEnum.SYNC.getCode().equals(consistencyTaskInstance.getThreadWay())){
            // 选择事务模型并执行任务
            taskEngineExecutor.executeTaskInstance(consistencyTaskInstance);
        }else if(ThreadWayEnum.ASYNC.getCode().equals(consistencyTaskInstance.getThreadWay())){

            consistencyTaskPool.submit(()->{

                taskEngineExecutor.executeTaskInstance(consistencyTaskInstance);

                    return consistencyTaskInstance;

            });
        }
    }
}
