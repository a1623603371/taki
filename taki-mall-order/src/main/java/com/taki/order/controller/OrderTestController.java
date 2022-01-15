package com.taki.order.controller;

import com.taki.common.utlis.ResponseData;
import com.taki.order.api.OrderApi;
import com.taki.order.domian.dto.CreateOrderDTO;
import com.taki.order.domian.dto.GenOrderIdDTO;
import com.taki.order.domian.request.CreateOrderRequest;
import com.taki.order.domian.request.GenOrderIdRequest;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName OrderTestController
 * @Description 订单测试接口 控制类
 * @Author Long
 * @Date 2022/1/14 11:54
 * @Version 1.0
 */
@RestController
@Api(tags = "订单服务测试类接口")
@Slf4j
@RequestMapping("/order/test")
public class OrderTestController {


    @Autowired
    private OrderApi orderInfoService;

    /**
     * @description: 生成订单Id
     * @param genOrderIdRequest 生成订单Id请求
     * @return  结果
     * @author Long
     * @date: 2022/1/14 17:30
     */
    @PostMapping("/genOrderId")
    public ResponseData<GenOrderIdDTO> genOrderId(@RequestBody GenOrderIdRequest genOrderIdRequest){

        return  orderInfoService.genOrderId(genOrderIdRequest);
    }

    /**
     * @description:  创建订单
     * @param createOrderRequest 创建订单请求
     * @return  响应结果数据
     * @author Long
     * @date: 2022/1/15 14:30
     */
    @PostMapping("/createOrder")
    public  ResponseData<CreateOrderDTO> createOrder(@RequestBody CreateOrderRequest createOrderRequest){

        return orderInfoService.createOrder(createOrderRequest);
    }

}
