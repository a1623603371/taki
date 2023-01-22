package com.taki.consistency.manager;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONUtil;
import com.taki.consistency.config.TendConsistencyConfiguration;
import com.taki.consistency.exception.ConsistencyException;
import com.taki.consistency.model.ConsistencyTaskInstance;
import com.taki.consistency.service.TaskStoreService;
import com.taki.consistency.util.ReflectTools;
import com.taki.consistency.custom.alerter.ConsistencyFrameworkAlerter;
import com.taki.consistency.util.ExpressionUtils;
import com.taki.consistency.util.SpringUtils;
import com.taki.consistency.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName TaskEngineExecutorImpl
 * @Description 任务执行引擎实现类
 * @Author Long
 * @Date 2022/9/2 21:22
 * @Version 1.0
 */
@Slf4j
@Component
public class TaskEngineExecutorImpl implements TaskEngineExecutor{

    /**
     * 一致性任务存储的service组件
     */
    @Autowired
    private TaskStoreService taskStoreService;

    /**
     * 任务执行引擎
     */
    @Autowired
    private TaskScheduleManager taskScheduleManager;


    /**
     * 告警通知线程池
     */
    @Autowired
    private ThreadPoolExecutor alertNoticePool;

    /**
     * 获取框架级配置
     */
    @Autowired
    private TendConsistencyConfiguration tendConsistencyConfiguration;



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executeTaskInstance(ConsistencyTaskInstance consistencyTaskInstance) {

        try {
        //启动任务
        Boolean result = taskStoreService.turnOnTask(consistencyTaskInstance);

        if (!result){
            log.warn("[一致性框架] 任务执行已经为启动状态，退出执行流程 task:{}",consistencyTaskInstance);
            return;
        }

        //获取启动好的一致性任务实例
        //重新查询一次数据是最新的，比如刚才SQL里更新了 execute times
        //Task 任务实例对象来，并没有设置这个execute time = 0
        // 但是在这里重新查询一次，让数据库里的数据刷新到内存对象里来，execute time = 1，就出来
        consistencyTaskInstance = taskStoreService.getTaskByIdAndShardKey(consistencyTaskInstance.getId(), consistencyTaskInstance.getShardKey());

        // 执行任务
         taskScheduleManager.performanceTask(consistencyTaskInstance);

         // 标记为执行成功，这里会移除改任务
        Boolean successResult = taskStoreService.markSuccess(consistencyTaskInstance);

        //针对mark success数据库去执行一个try cache，对于任务执行成功，但是mark success 失败
        //完全基于磁盘文件可以去一个打一个执行成功的执行
        // 框架自带，带有一个线程，不断去读取磁盘文件里的执行成功的标记，自动给他去在数据库里mark success去补充执行
        // delete操作，把他删除
        // 可以让框架基于redis 开启一个可选的幂等的选项，可以给一个参数，配置一个redis地址，开启一个幂等性的机制
         //在redis里可以写入一个指定任务id已经成功的幂等标记
        //对于定时调度运行的程序，对每个数据库里查询出来的任务id，都去redis 里检查，自动做一个幂等的检查
        //确保已经成功的任务不要重新执行
        log.info("[一致性任务框架] 标记执行成功的结果为【{}】",successResult);
        }catch (Exception e){
            log.error("【一致性任务框架】{} 执行一致性任务时，发生异常，taskInstance 的实例信息为：{}", JSONUtil.toJsonStr(consistencyTaskInstance),e);
            consistencyTaskInstance.setErrorMsg(getErrorMsg(e));
            //对于最终一致性的框架，他执行Action，如果失败了，是要无限次重试
            //如果说出现一个失败，下一次必然会是进行重试，下一次是什么时候可以进行重试
            //在这里需要给他去计算一个一次进行重试的execute time
            consistencyTaskInstance.setExecuteTime(getNextExecuteTime(consistencyTaskInstance)); // 这个下一次执行时间，非常关键

            Boolean failResult = taskStoreService.markFail(consistencyTaskInstance);

            if (failResult){
                System.out.println(failResult);
            }

            log.info("[一致性任务框架] 标记执行失败的结果为【{}】下次调度时间为【{} - {}】",failResult,consistencyTaskInstance.getExecuteTime(),getFormatTime(consistencyTaskInstance.getExecuteTime()));

            //执行降级逻辑
            fallbackExecuteTask(consistencyTaskInstance);

        }


    }
    
    /*** 
     * @description:
     * @param executeTime
     * @return  java.lang.Object
     * @author Long
     * @date: 2022/9/2 23:13
     */ 
    private Object getFormatTime(Long executeTime) {
        //设置格式
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(executeTime);
    }

    /*** 
     * @description:  获取任务下一次的执行时间
     * @param ： consistencyTaskInstance一致性任务实例
     * @return  java.lang.Long
     * @author Long
     * @date: 2022/9/2 22:35
     */ 
    private Long getNextExecuteTime(ConsistencyTaskInstance consistencyTaskInstance) {

        return consistencyTaskInstance.getExecuteTime() + ((consistencyTaskInstance.getExecuteTimes() + 1) * TimeUtils.seToMill(consistencyTaskInstance.getExecuteIntervalSec().longValue()));

    }

    /*** 
     * @description:  获取异常信息
     * @param e 异常对象
     * @return
     * @author Long
     * @date: 2022/9/2 21:57
     */ 
    private String getErrorMsg(Exception e) {
        if("".equals(e.getMessage())){
            return "";
        }
        String errorMsg = e.getMessage();

        if (StringUtils.isEmpty(errorMsg)){
            if (e instanceof  IllegalAccessException){
                IllegalAccessException illegalAccessException = (IllegalAccessException) e;
                errorMsg = illegalAccessException.getMessage();
            }

            if (e instanceof  IllegalArgumentException){
                IllegalArgumentException illegalArgumentException = (IllegalArgumentException) e;
                errorMsg = illegalArgumentException.getMessage();
            }

            if (e instanceof InvocationTargetException){
                InvocationTargetException invocationTargetException = (InvocationTargetException) e;
                errorMsg = invocationTargetException.getMessage();
            }

        }

        return errorMsg.substring(0,Math.min(errorMsg.length(),200));

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void fallbackExecuteTask(ConsistencyTaskInstance consistencyTaskInstance) {

        //如果注解（任务实例信息）中没有提供降级类，则退出，不执行降级
        if (StringUtils.isEmpty(consistencyTaskInstance.getFallbackClassName())){
            //解析并对表达式结果进行效验，并执行相关的告警通知逻辑
            // 如果没有配置降级类且符合告警通知的表达式，则直接进行告警
            parseExpressionAndDoAlert(consistencyTaskInstance);
            return;
        }

        //获取全局配置 默认是否开启降级策略 的 如果 失败会进行降级
        if (consistencyTaskInstance.getExecuteTimes() <= tendConsistencyConfiguration.getFailCountThreshold()){
            return;
        }
        log.info("[一致性任务框架] 执行任务id：{}的降级逻辑...",consistencyTaskInstance.getId());

        Class<?> fallbackClass = ReflectTools.getClassByName(consistencyTaskInstance.getFallbackClassName());

        if (ObjectUtils.isEmpty(fallbackClass)){
            return;
        }

        //获取参数值列表的json数组字符串
        String taskParameterText = consistencyTaskInstance.getTaskParameter();
        //参数类型字符串 多个逗号进行分割
        String parameterTypes = consistencyTaskInstance.getParameterTypes();
        // 构造参数类数组
        Class<?>[] paramTypes = getParamTypes(parameterTypes);

        // 参数具体的值
        Object[] paramValues = ReflectTools.buildArgs(taskParameterText,paramTypes);

        // 从spring容器中获取相关降级的bean
        Object fallbackClassBean = getBeanBySpringApplicationContext(fallbackClass,paramValues);

        Method fallbackMethod = ReflectUtil.getMethod(fallbackClass,consistencyTaskInstance.getMethodName(),paramTypes);

        try {
            // 执行降级逻辑的方法
            fallbackMethod.invoke(fallbackClassBean,paramValues);

            // 标记 执行成功 这里移除任务
            Boolean successResult = taskStoreService.markSuccess(consistencyTaskInstance);

            // 可以实现一套跟我们之前讲的执行成功后的mark success 故障的降级机制，一模一样
            // 写磁盘 对这个任务进行mark success 后台线程不停删除任务
            //开启redis 只要降级成功了，mark success 失败了 也必须要去redis 里写标记
            log.info("[一致性任务框架] 降级逻辑执行成功 标记为执行成功的结果为【{}】",successResult);
        }catch (Exception e){
            log.error("[一致性任务框架] 降级逻辑执行失败异常：{}",e);
        // 解析并对表达式结果进行效验，并执行相关的告警通知逻辑
         //在执行完降级逻辑后,再发送消息，因为如果降级成功了，也就不用发送告警通过了，如果降级失败，在发送告警通知
            parseExpressionAndDoAlert(consistencyTaskInstance);
            consistencyTaskInstance.setFallbackErrorMsg(getErrorMsg(e));
            Boolean failResult = taskStoreService.markFallbackFail(consistencyTaskInstance);

            log.error("[一致性任务框架] 降级逻辑也失败了，标记执行失败结果为[{}] 下次调用时间为 【{} - {}】",failResult,consistencyTaskInstance.getExecuteTime(),getFormatTime(consistencyTaskInstance.getExecuteTime()),e);

        }

    }
    
    /*** 
     * @description:  从spring 容器中 获取相关降级的bean
     * @param fallbackClass 降级的Class类
     * @param paramValues 参数值
     * @return  java.lang.Object
     * @author Long
     * @date: 2022/9/3 15:58
     */ 
    private Object getBeanBySpringApplicationContext(Class<?> fallbackClass, Object[] paramValues) {


        return SpringUtils.getBean(fallbackClass,paramValues);


    }

    /*** 
     * @description:  获取参数类型数组
     * @param taskParameterText 参数类型数组
     * @return  java.lang.Class<?>[]
     * @author Long
     * @date: 2022/9/3 15:54
     */ 
    private Class<?>[] getParamTypes(String taskParameterText) {

        return  ReflectTools.buildTypeClassArray(taskParameterText.split(","));
    }

    /*** 
     * @description:  解析并对表达式结果进行效验，并执行相关的告警通知逻辑
     * @param consistencyTaskInstance 一致性任务实例信息
     * @return  void
     * @author Long
     * @date: 2022/9/2 23:41
     */ 
    private void parseExpressionAndDoAlert(ConsistencyTaskInstance consistencyTaskInstance) {

        try {
            if (StringUtils.isEmpty(consistencyTaskInstance.getAlertExpression())){
                return;
            }
            //使用线程 对不正常业务调用造成时间的占用 一般推送消息使用的发送短信，钉钉企业微信 邮件等
            //操作会有一点的耗时（不过这个也要具体看实现类这么实现，如果实现中使用的是异步推送告警，其实这里不用放到线程池）
            alertNoticePool.submit(()->{
                //对表达式进行重写
                String expr = ExpressionUtils.rewriteExpr(consistencyTaskInstance.getAlertExpression());
                // 获取表达式解析后的结果
                String exprResult = ExpressionUtils.readExpr(expr,ExpressionUtils.buildDataMap(consistencyTaskInstance));

                // 执行alert告警
                doAlert(exprResult,consistencyTaskInstance);
            });



        }catch (Exception e){
            log.error("发送告警通知时，发送异常",e);
        }
    }
    
    /*** 
     * @description:  执行告警
     * @param exprResult
     * @param consistencyTaskInstance
     * @return  void
     * @author Long
     * @date: 2022/9/3 14:58
     */ 
    private void doAlert(String exprResult, ConsistencyTaskInstance consistencyTaskInstance) {

        if (StringUtils.isEmpty(exprResult)){
            return;
        }

        if (!ExpressionUtils.RESULT_FLAG.equals(exprResult)) {
            return;
        }

        log.warn("[一致性任务框架] 告警通知 实例id为{}的任务{}触发告警规则，请进行排查。",consistencyTaskInstance.getId(),JSONUtil.toJsonStr(consistencyTaskInstance));
        if (StringUtils.isEmpty(consistencyTaskInstance.getAlertActionBeanName())){
            return;
        }

        // 发送告警
        sendAlertNotice(consistencyTaskInstance);
        
        }
    
    /*** 
     * @description:  发送告警通知
     * @param consistencyTaskInstance 一致性任务实例信息
     * @return  void
     * @author Long
     * @date: 2022/9/3 15:28
     */       
    private void sendAlertNotice(ConsistencyTaskInstance consistencyTaskInstance) {
        // 获取Spring容器中所有对于ConsistencyFrameworkAlerter 接口的实现类
        Map<String, ConsistencyFrameworkAlerter> beanOfTypeMap = SpringUtils.getBeansOfType(ConsistencyFrameworkAlerter.class);

        if (CollectionUtils.isEmpty(beanOfTypeMap)){
            log.warn("[一致性任务框架] 未获取到 ConsistencyFrameworkAlerter 相关实现类，无法进行告警通知...");
            return;
        }

        try {
            //获取ConsistencyFrameworkAlerter 的实现类并发送告警通知
            getConsistencyFrameworkAlerterImpler(beanOfTypeMap,consistencyTaskInstance).sendAlertNotice(consistencyTaskInstance);
        }catch (Exception  e){
            log.error("[一致性任务框架] 调用业务服务实现具体的告警通知类时，发生异常",e);
            throw  new ConsistencyException(e);
        }


    }

    /*** 
     * @description:  获取ConsistencyFrameworkAlerter 的实现类
     * @param beanOfTypeMap ConsistencyFrameworkAlerter 的接口实现类的map集合
     * @param consistencyTaskInstance 一致性任务实例数据
     * @return
     * @author Long
     * @date: 2022/9/3 15:40
     */ 
    private ConsistencyFrameworkAlerter getConsistencyFrameworkAlerterImpler(Map<String, ConsistencyFrameworkAlerter> beanOfTypeMap, ConsistencyTaskInstance consistencyTaskInstance) {

        // 如果只有一个实现类
        if (beanOfTypeMap.size() == 1){
            String [] beanNamesForType = SpringUtils.getBeanNameForType(ConsistencyFrameworkAlerter.class);

            return SpringUtils.getBean(beanNamesForType[0]);
        }
        return beanOfTypeMap.get(consistencyTaskInstance.getAlertActionBeanName());
    }
}



