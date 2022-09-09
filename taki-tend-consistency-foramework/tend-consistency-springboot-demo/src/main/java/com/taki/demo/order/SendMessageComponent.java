package com.taki.demo.order;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.taki.consistency.annotation.ConsistencyTask;
import com.taki.consistency.enums.PerformanceEnum;
import com.taki.consistency.enums.ThreadWayEnum;
import com.taki.demo.fail.SendMessageFallbackHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName SendMessageComponent
 * @Description TODO
 * @Author Long
 * @Date 2022/9/5 20:30
 * @Version 1.0
 */
@Component
@Slf4j
public class SendMessageComponent {

    /*** 
     * @description:  正常运行失败，降级运行成功，异步调用任务测试
     * @param orderInfoDTO
     * @return  void
     * @author Long
     * @date: 2022/9/5 20:52
     */ 
    @ConsistencyTask(
            // 任务执行秒数
            executeIntervalSec = 20,
            //这个方法被调用开始，到后续被异步调用执行，至少距离调用时间已经过去10s
            delayTime = 10,
            performanceWay = PerformanceEnum.PERFORMANCE_SCHEDULE,
            threadWay = ThreadWayEnum.ASYNC,
            fallbackClass = SendMessageFallbackHandler.class,
            alertActionBeanName = "normalAlerter"
    )
    // 这个任务如果这么配置的话，我们对他的运行效果期望
    //调用的时候先进AOP切面，肯定会基于方法和注解，封装任务实例，数据会持久化落库
    //是否直接运行目标方法
    public void send(OrderInfoDTO orderInfoDTO){

        System.out.println(1 / 0);

        log.info("[异步调用任务测试] 执行send（OrderInfoDTO）方法{}", JSONUtil.toJsonStr(orderInfoDTO));
    }

    /*** 
     * @description: 正常运行失败，降级也失败，触发警告通知， 立即执行 异步任务测试 立即执行异步任务的情况下 executeIntervalSec 和 delayTime 属性无用
     * 开一个新的线程去执行任务
     * 《p》
     * 验证情况：
     *1.发送消息时，执行失败，有异常发生情况，会标记任务状态为失败，同时记录失败的原因
     * 2.当满足 降级条件（executeTimes 执行次数）> TendConsistencyConfig.fallbackThreadshold(默认为0) 可以触发降级逻辑，调用相关用户实现的自定义降级类的指定方法
     *3.当满足 默认的 alertExpression(executeTimes > 1 & executeTimes < 5) 告警通知时，会触发消息的推送，并可以调用相关实现类
     * @param orderInfoDTO
     * @return  void
     * @author Long
     * @date: 2022/9/8 21:37
     */
    @ConsistencyTask(
            executeIntervalSec = 2,
            // delayTime = 5,
            performanceWay = PerformanceEnum.PERFORMANCE_RIGHT_NOW,
            threadWay = ThreadWayEnum.ASYNC,
            fallbackClass = SendMessageFallbackHandler.class,
            alertActionBeanName = "normalAlerter" // normalAlerter就是 com.ruyuan.eshop.alertm.NormalAlerter类在spring容器中的beanName
    )
    public void sendRightNowAsyncMessage(OrderInfoDTO orderInfoDTO){

    log.info("[异步调用任务测试] 执行sendRightNowAsyncMessage(orderInfoDTO)方法 {}",JSONUtil.toJsonStr(orderInfoDTO));

        System.out.println(1/0); // 模拟失败
        
    }
}
