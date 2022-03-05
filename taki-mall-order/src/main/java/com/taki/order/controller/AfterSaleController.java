package com.taki.order.controller;

import com.taki.common.utlis.ResponseData;
import com.taki.order.domain.request.CancelOrderRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName AfterSaleController
 * @Description 订单售后流程
 * @Author Long
 * @Date 2022/3/5 15:01
 * @Version 1.0
 */
@Api(value = "AfterSaleController",tags = "订单售后流程")
@RestController
@RequestMapping("/afterSale")
public class AfterSaleController {

    @ApiOperation("取消订单")
    @PostMapping("/cancelOrder")
    public ResponseData<Boolean> cancelOrder(@RequestBody CancelOrderRequest cancelOrderRequest){

        return null;

    }
}
