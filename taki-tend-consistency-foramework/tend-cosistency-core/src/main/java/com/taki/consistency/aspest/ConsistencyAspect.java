package com.taki.consistency.aspest;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONUtil;
import com.taki.consistency.annotation.ConsistencyTask;
import com.taki.consistency.config.TendConsistencyConfiguration;
import com.taki.consistency.custom.shard.SnowflakeShardingKeyGenerator;
import com.taki.consistency.enums.ConsistencyTaskStatusEnum;
import com.taki.consistency.enums.PerformanceEnum;
import com.taki.consistency.model.ConsistencyTaskInstance;
import com.taki.consistency.service.TaskStoreService;
import com.taki.consistency.util.ReflectTools;
import com.taki.consistency.util.ThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @ClassName ConsistencyAspect
 * @Description 一致性框架切面
 * @Author Long
 * @Date 2022/9/4 19:50
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
public class ConsistencyAspect {

    /**
     * 缓存生成任务分片key的对象实例
     */
    private static Object cacheGenerateShardKeyClassInstance = null;

    /**
     * 缓存生成任务分片key的方法
     */
    private static Method cacheGenerateShardKeyMethod = null;

    /**
     * 一致性任务的service
     */
    @Autowired
    private TaskStoreService taskStoreService;

    /**
     * 框架配置
     */
    @Autowired
    private TendConsistencyConfiguration tendConsistencyConfiguration;


    /*** 
     * @description:标记了 ConsistencyTask的注解的方法执行前要做的工作
     * @param point 切面信息
     * @param  consistencyTask
     * @return  java.lang.Object
     * @author Long
     * @date: 2022/9/4 19:55
     */ 
    public Object markConsistencyTask(ProceedingJoinPoint point, ConsistencyTask consistencyTask) throws Throwable {

        log.info("access method:{}is called on {} args {}",point.getSignature().getName(),point.getThis(),point.getArgs());

        // 是否是调度器在执行任务，如果是则直接执行任务即可，因为之前已经进行了任务持久化

        if(ThreadLocalUtils.getFlag()){
            return point.proceed();
        }

        // 根据注解构造最终一致性任务的实例
        // @ConsistencyTask 是我们给这个方法定义一个注解，通过这个注解拿到我们自定义的各种框架参数
        //JoinPoint ，切面点，方法信息，包括方法入参，方法名称，方法签名
        // 我们就可以封装出来对应的一致性任务实例
        //本次针对我们的目标方法的调用，@ConsistencyTask注解，他就一个任务，Action
        // 一旦把目标任务抽象和封装起来后，包括在数据库里做持久化，
        ConsistencyTaskInstance consistencyTaskInstance = createTaskInstance(consistencyTask,point);

        //初始化任务数据到数据库
        taskStoreService.initTask(consistencyTaskInstance);
        //无论是调度执行还是立即执行的任务，任务初始完成后不对目标方法进行访问，因此返回null
        return null;
        
    }

    /***
     * @description: 根据注解构造最终一致性任务的实例
     * @param task 一致性任务注解信息 相当于任务的模板
     * @param  point 方法的切入点
     * @return  一致性任务实例
     * @author Long
     * @date: 2022/9/4 20:03
     */
    private ConsistencyTaskInstance createTaskInstance(ConsistencyTask task, ProceedingJoinPoint point) {

        // 通过@ConsistencyTask 注解提取一系列的参数
        //通过 JoinPoint切入点，可以提取一系列的方法相关的数据

        //根据入参数组获取对应的class对象的数组
        Class<?> [] argsClazz = ReflectTools.getArgsClass(point.getArgs());
        // 获取被拦截方法的全限定名 格式：类路径#方法名（参数1的类型，参数2的类型，.....参数N的类型）
        String fullyQualifiedName = ReflectTools.getTargetMethodFullyQualifiedName(point,argsClazz);
        // 获取入参的类名称数组
        String parameterTypes = ReflectTools.getArgsClassNames(point.getSignature());

        LocalDateTime now = LocalDateTime.now();

        // 一致性任务执行 =一次方法调用
        ConsistencyTaskInstance consistencyTaskInstance = ConsistencyTaskInstance.builder()
                // taskId 默认用的就是方法全限定名称，针对一个方法n多次调用，taskId 是一样的
                //taskId 并不是唯一的id标识
                .taskId(StringUtils.isEmpty(task.id()) ? fullyQualifiedName : task.id())
                .methodName(point.getSignature().getName()) // 调用方法名
                .parameterTypes(parameterTypes) //调用方法入参的类型名称
                .methodSignName(fullyQualifiedName) //方法签名
                .taskParameter(JSONUtil.toJsonStr(point.getArgs())) // 调用方法入参的对象数组，json串的转化
                .performanceWay(task.threadWay().getCode()) //注解里配置的直接执行，同步还是异步，sync还是async，async 会用我们自己初始化的线程池
                .executeIntervalSec(task.executeIntervalSec()) //每次任务执行间隔时间
                .delayTime(task.delayTime())// 任务执行延迟 时间
                .executeTimes(0) // 任务执行次数
                .taskStatus(ConsistencyTaskStatusEnum.INIT.getCode()) //任务当前所的一个状态
                .errorMsg("")
                .alertExpression(StringUtils.isEmpty(task.alertExpression()) ? "": task.alertExpression()) // 限定了你的报警要在任务执行失败多少次的范围内去报警
                .alertActionBeanName(StringUtils.isEmpty(task.alertActionBeanName()) ? "" : task.alertActionBeanName()) //如果告警的话，他的告警逻辑的调用bean是谁
                .fallbackClassName(ReflectTools.getFullyQualifiedClassName(task.fallbackClass())) // 如果执行失败了，他降级类是谁
                .fallbackErrorMsg("") // 如果降级也失败了，降级失败的异常信息
                .createTime(now)
                .updateTime(now).build();

        //设置预期执行的时间
        consistencyTaskInstance.setExecuteTime(getExecuteTime(consistencyTaskInstance));

        // 设置分片key
        consistencyTaskInstance.setShardKey(tendConsistencyConfiguration.getTaskSharded() ? generateShardKey() : 0L);

        return consistencyTaskInstance;

    }
    
    /*** 
     * @description: 获取分片键
     * @param 
     * @return 生成分片键
     * @author Long
     * @date: 2022/9/4 20:55
     */ 
    private Long generateShardKey() {

        //如果配置文件中，没有配置自定义任务分片键生成类，则使用框架自带的
        if (StringUtils.isEmpty(tendConsistencyConfiguration.getShardingKeyGeneratorClassName())){
            return SnowflakeShardingKeyGenerator.getInstance().generateShardKey();
        }
        //如果生成任务 CACHE_GENERATE_SHARD_KEY_METHOD的方法存在，直接调用方法
        if (!ObjectUtils.isEmpty(cacheGenerateShardKeyMethod) && !ObjectUtils.isEmpty(cacheGenerateShardKeyClassInstance)){
            try {
            return (Long) cacheGenerateShardKeyMethod.invoke(cacheGenerateShardKeyClassInstance);
            }catch (IllegalAccessException | InvocationTargetException e){
                log.error("使用自定义类生成任务分片键时，发送异常",e);

            }
        }
        // 获取用户自定义的任务分片键的class
        Class<?> shardingKeyGeneratorClass = getUserCustomShardingKeyGenerator();
        if (!ObjectUtils.isEmpty(shardingKeyGeneratorClass)){
            String methodName = "generateShardKey";
            Method generateShardKeyMethod = ReflectUtil.getMethod(shardingKeyGeneratorClass,methodName);
            try {
                Constructor<?> constructor = ReflectUtil.getConstructor(shardingKeyGeneratorClass);
                cacheGenerateShardKeyClassInstance = constructor.newInstance();
                cacheGenerateShardKeyMethod = generateShardKeyMethod;
                return (Long) cacheGenerateShardKeyMethod.invoke(cacheGenerateShardKeyClassInstance);
            }catch (IllegalAccessException | InvocationTargetException | InstantiationException e){
                log.error("使用自定义类生成任务分片键时，发生异常",e);
                //如果指定自定义分片键生成报错，使用框架自带的
                return SnowflakeShardingKeyGenerator.getInstance().generateShardKey();
            }
        }

        return SnowflakeShardingKeyGenerator.getInstance().generateShardKey();
    }
    
    /*** 
     * @description: 获取ShardingKeyGenerator的实现类
     * @param 
     * @return
     * @author Long
     * @date: 2022/9/4 21:01
     */ 
    private Class<?> getUserCustomShardingKeyGenerator() {
        return  ReflectTools.getClassByName(tendConsistencyConfiguration.getShardingKeyGeneratorClassName());
    }

    /*** 
     * @description:  获取任务执行时间
     * @param consistencyTaskInstance 一致性任务实例
     * @return  java.lang.Long
     * @author Long
     * @date: 2022/9/4 20:26
     */ 
    private Long getExecuteTime(ConsistencyTaskInstance consistencyTaskInstance) {
        if (PerformanceEnum.PERFORMANCE_SCHEDULE.getCode().equals(consistencyTaskInstance.getPerformanceWay())){
                Long delayTimeMillSecond = consistencyTaskInstance.getDelayTime() * 1000L;

                return System.currentTimeMillis() +delayTimeMillSecond; //如果你是调度模式，一般是跟delay time 配合使用的，你要延迟多少时间去执行
        }else {
            //如果你要是设置了right now 模式来执行的话，delayTime你设置了无效的
            return  System.currentTimeMillis();//执行时间就是当前时间
        }
    }
}
