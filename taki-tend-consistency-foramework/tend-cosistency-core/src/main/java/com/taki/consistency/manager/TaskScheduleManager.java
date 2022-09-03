package com.taki.consistency.manager;

import cn.hutool.extra.spring.SpringUtil;
import com.taki.consistency.model.ConsistencyTaskInstance;
import com.taki.consistency.util.ReflectTools;
import com.taki.consistency.exception.ConsistencyException;
import com.taki.consistency.service.TaskStoreService;
import com.taki.consistency.util.ThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * @ClassName TaskScheduleManager
 * @Description 任务调度器
 * @Author Long
 * @Date 2022/9/1 22:31
 * @Version 1.0
 */
@Component
@Slf4j
public class TaskScheduleManager {

    /**
     * 任务存储mapper 组件
     */
    @Autowired
    private TaskStoreService taskStoreService;

    /**
     * 并行线程池
     */
    @Autowired
    private CompletionService<ConsistencyTaskInstance> consistencyTaskPool;


    @Autowired
    private TaskEngineExecutor taskEngineExecutor;

    /*** 
     * @description: 该方法在业务服务中的定时任务中进行调度查询并执行未完成的一致任务
     * @param 
     * @return  void
     * @author Long
     * @date: 2022/9/1 22:36
     */ 
    public void performanceTask() throws InterruptedException {
        //第一步，先查询未完成的任务实例list列表
        //查询待执行任务实例集合的时候，数据库故障了，这是第2个故障点
        List<ConsistencyTaskInstance> consistencyTaskInstances = taskStoreService.listByUnFinishTask();
        if (CollectionUtils.isEmpty(consistencyTaskInstances)){
            return;
        }

        // 过滤出需要被执行任务
        // 每个任务都可以来运行，不是的，根据你execute time来的， execute time 是根据delay time 来的

        consistencyTaskInstances =  consistencyTaskInstances.stream()

                //针对我们查询出来的任务实例执行一次过滤，根据execute time 过滤
                //每个任务实例的execute time 是根据你的运行模式和delay time 运算出来，执行运行模式， execute time就是当时实例化任务的now
                //如果调度任务运行模式，是now + delay time = execute time ,延迟过的时间
                // schedule运行模式， delay time = 10s, now1 =22:56:30, execute time =22:56:40, now 2 = 22:56:45 减法 = 正数， 是不能运行的
                // execute time - now2 = 0,刚好达到了我预期最快运行的时间，此时任务就可以运行了
                // execute time - now2 = -5 负数，必须赶紧运行了，此时超过了我预期运行的时间了，就不对了，就赶紧执行
                .filter(e->e.getExecuteTime() - System.currentTimeMillis() <= 0)
                .collect(Collectors.toList()); // 拿到任务实例，都是execute time >= now


        if (CollectionUtils.isEmpty(consistencyTaskInstances)){
            return;
        }

        //多线程并发执行
        //如果说查出来了比如说很多个实例，1000个，往你的线程池里提交，5个线程+100 size queue
        // 如果说出现了这个问题的话，会导致线程的reject 提交任务
        CountDownLatch latch = new CountDownLatch(consistencyTaskInstances.size());

        consistencyTaskInstances.forEach(instance->{
            consistencyTaskPool.submit(()->{
                try {
                // 执行任务
                taskEngineExecutor.executeTaskInstance(instance);
                return  instance;
                }finally {
                    latch.countDown();
                }
            });
        });

        latch.await();

        log.info("[一致性任务框架] 执行完成");

    }


    /*** 
     * @description:  执行指定任务
     * @param taskInstance 一致性任务实例信息
     * @return  void
     * @author Long
     * @date: 2022/9/1 23:03
     */ 
    public void performanceTask(ConsistencyTaskInstance taskInstance){

        // 任务实例数据中封装了目标方法（AOP切入点,JoinPoint） + 注解 （@ConsistencyTask）
        // 都完成一个封装，此时我们就可以用到所有的数据

        //获取方法签名，格式：类路径#方法名（参数1的类型，参数2的类型，....参数N的类型）
        String methodSignName = taskInstance.getMethodSignName();
        // 获取方法所在的类，包含了类全限定名，就通过字符串操作，可以拿到你的方法的所属类
        Class<?> clazz = getTaskMethodClass(methodSignName.split("#")[0]);
        if (ObjectUtils.isEmpty(clazz)){
            return;
        }
        //通过你的类class类型，从spring容器里获取这个class类型bean
        Object bean = SpringUtil.getBean(clazz);
        if(ObjectUtils.isEmpty(bean)){
            return;
        }

        //后面把methodName独立出一个字段
        String methodName = taskInstance.getMethodName();

        //获取参数类型的字符串 多个逗号分割
        String[] parameterTypes = taskInstance.getParameterTypes().split(",");

        //构造参数类数组
        Class<?>[] parameterClasses = ReflectTools.buildTypeClassArray(parameterTypes);

        //获取目标方法
        Method targetMethod = getTargetMethod(methodName,parameterClasses,clazz);

        if (ObjectUtils.isEmpty(targetMethod)){
            return;
        }

        // 已知获取到了目标类 -》 spring容器里的bean实例对象，有了这个bean实例对象之后，就可以进行反射调用
        //目标类里指定的方法名称+ 入参类型 ->Method方法
    //如果 说 要通过反射，去调用bean实例对象的method方法，传入参数对象
        // 要把入参参数对象搞出来

        //构造入参
        //task parameter 就是入参对象数组转为json字符串
        Object[] args = ReflectTools.buildArgs(taskInstance.getTaskParameter(),parameterClasses);

        try {
            // 执行目标方法调用
            ThreadLocalUtils.setFlag(true); // 基于 thread local 设置一个 flag ，true

            //基于 反射 进行方法调用，调用谁，bean 调用的是bean的那个方法，给方法调用转入的是哪个参数

            //bean=spring容器获取的bean，方法就是类的方法，args入参对象就是本次方法调用传入的入参对象
            // 结论：我们在进行方法调用的时候，其实也会进行AOP增强逻辑的，完成了AOP增强逻辑之后，才会推进到目标方法的执行
            targetMethod.invoke(bean,args); // 基于反射，method 对象完成针对bean实例的传入参对象数组的调用
            ThreadLocalUtils.setFlag(false); // 跑完设置为 false

        }catch (InvocationTargetException e){
            log.error("调用目标方法时，发送异常",e);
            Throwable target = e.getTargetException();
            throw new ConsistencyException((Exception) target);
        }
        catch (Exception e){
            throw new ConsistencyException(e);
        }


        
    }

    /***
     * @description: 获取目标方法
     * @param methodName 方法名称
     * @param   parameterClasses 入参类数组
     * @param  clazz 方法所在类Class对象
     * @return  java.lang.reflect.Method
     * @author Long
     * @date: 2022/9/1 23:15
     */
    private Method getTargetMethod(String methodName, Class<?>[] parameterClasses, Class<?> clazz) {

        try {

            //基于反射，从指定的目标类里拿到了类的方法对象的Method
            return clazz.getMethod(methodName,parameterClasses);

        }catch (Exception e){
            log.error("获取目标方法失败",e);
            return null;
        }

    }

    /*** 
     * @description:  构造任务方法所在的类对象
     * @param className 类名称
     * @return 类对象
     * @author Long
     * @date: 2022/9/1 23:08
     */ 
    private Class<?> getTaskMethodClass(String className) {

        Class<?> clazz;
        try {
            clazz = Class.forName(className);
            return clazz;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
