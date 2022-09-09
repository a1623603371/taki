package com.taki.demo.fail;

import cn.hutool.json.JSONUtil;
import com.taki.demo.order.OrderInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName SendMessageFallbackHandler
 * @Description 自定义降级类
 * @Author Long
 * @Date 2022/9/5 20:39
 * @Version 1.0
 */
@Component
@Slf4j
public class SendMessageFallbackHandler {

    /*** 
     * @description:
     * @param orderInfo
     * @return  void
     * @author Long
     * @date: 2022/9/8 21:25
     */ 
 public void send(OrderInfoDTO orderInfo){

     log.info("触发send方法的降级逻辑");
 }


public void sendRightNowAsyncMessage(OrderInfoDTO orderInfo){

     log.info("[立即执行异步任务测试] 降级逻辑 执行 sendRightNowAsyncMessage(OrderInfoDTO)方法 {}", JSONUtil.toJsonStr(orderInfo));
   // System.out.println(1/0); //模拟降级失败
}

}
