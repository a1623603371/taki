package com.taki.order.controller;

import com.taki.common.utlis.ResponseData;
import com.taki.order.domain.request.CancelOrderRequest;
import com.taki.order.domain.request.ReturnGoodsOrderRequest;
import com.taki.order.service.OrderAfterSaleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private OrderAfterSaleService orderAfterSaleService;

    /**
     * @description: 取消订单
     * @param cancelOrderRequest 取消订单请求参数
     * @return  处理结果
     * @author Long
     * @date: 2022/4/3 15:22
     */
    @ApiOperation("取消订单")
    @PostMapping("/cancelOrder")
    public ResponseData<Boolean> cancelOrder(@RequestBody CancelOrderRequest cancelOrderRequest){
        return ResponseData.success(orderAfterSaleService.cancelOrder(cancelOrderRequest));
    }

    /**
     * @description: 用户退货售后
     * @param
     * @return  com.taki.common.utlis.ResponseData<java.lang.Boolean>
     * @author Long
     * @date: 2022/4/3 15:25
     */
    @ApiOperation("用户退货售后")
    @PostMapping("/applyAfterSale")
    public ResponseData<Boolean> applyAfterSale( @RequestBody ReturnGoodsOrderRequest request) {

        return ResponseData.success(orderAfterSaleService.processApplyAfterSale(request));
    }
}
