package com.taki.demo.controller;

import com.taki.demo.order.OrderInfoDTO;
import com.taki.demo.order.SendMessageComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TestController
 * @Description TODO
 * @Author Long
 * @Date 2022/9/5 20:25
 * @Version 1.0
 */
@RestController
@RequestMapping("/test")
public class TestController {

    private static  final Logger log = LoggerFactory.getLogger(TestController.class);


    @Autowired
    private SendMessageComponent sendMessageComponent;

    @GetMapping("/send")
    public String send(){

        OrderInfoDTO orderInfoDTO = OrderInfoDTO.builder().id("111").build();

        sendMessageComponent.send(orderInfoDTO);

        return "";
    }

    @GetMapping("/sendRightNowAsyncMessage")
    public String sendRightNowAsyncMessage(){
        OrderInfoDTO orderInfoDTO = OrderInfoDTO.builder().id("222").build();

        //发送异步任务
        sendMessageComponent.sendRightNowAsyncMessage(orderInfoDTO);

        return"ok";
    }
}
