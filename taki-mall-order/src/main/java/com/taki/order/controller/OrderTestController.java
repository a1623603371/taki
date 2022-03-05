package com.taki.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.enums.OrderStatusChangEnum;
import com.taki.common.page.PagingInfo;
import com.taki.common.utlis.ResponseData;
import com.taki.fulfill.api.FulFillApi;
import com.taki.fulfill.domain.evnet.OrderDeliveredWmsEvent;
import com.taki.fulfill.domain.evnet.OrderOutStockWmsEvent;
import com.taki.fulfill.domain.evnet.OrderSignedWmsEvent;
import com.taki.order.api.OrderApi;
import com.taki.order.api.OrderQueryApi;
import com.taki.order.domain.dto.*;
import com.taki.order.domain.query.OrderQuery;
import com.taki.order.domain.request.RemoveOrderRequest;
import com.taki.order.domain.request.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    @DubboReference(version = "1.0.0",retries = 0)
    private OrderApi orderInfoService;


    @DubboReference(version = "1.0.0",retries = 0)
    private OrderQueryApi orderQueryApi;


    @DubboReference(version = "1.0.0",retries = 0)
    private FulFillApi fulFillApi;

    /**
     * @description: 生成订单Id
     * @param genOrderIdRequest 生成订单Id请求
     * @return  响应结果数据
     * @author Long
     * @date: 2022/1/14 17:30
     */
    @ApiOperation("生成订单Id")
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
    @ApiOperation("创建订单")
    @PostMapping("/createOrder")
    public  ResponseData<CreateOrderDTO> createOrder(@RequestBody CreateOrderRequest createOrderRequest){

        return orderInfoService.createOrder(createOrderRequest);
    }


    /**
     * @description: 预支付
     * @param payOrderRequest 预支付请求
     * @return  响应结果数据
     * @author Long
     * @date: 2022/2/16 15:43
     */
    @ApiOperation("预支付")
    @PostMapping("/prepayOrder")
    public ResponseData<PrePayOrderDTO> prepayOrder(@RequestBody PrePayOrderRequest payOrderRequest) {

        return orderInfoService.prePayOrder(payOrderRequest);
    }

    /**
     * @description: 支付回调
     * @param payCallbackRequest 支付回调 请求
     * @return  支付回调结果
     * @author Long
     * @date: 2022/2/16 16:03
     */
    @ApiOperation("支付回调")
    @PostMapping("/payCallback")
    public ResponseData<Boolean> payCallback(@RequestBody PayCallbackRequest payCallbackRequest){


        return orderInfoService.payCallback(payCallbackRequest);
    }


    /**
     * @description: 移除订单
     * @param removeOrderRequest 删除订单请求参数
     * @return删除订单 信息
     * @author Long
     * @date: 2022/2/26 17:43
     */
    @ApiOperation("移除订单")
    @PostMapping("/removeOrders")
    public ResponseData<RemoveOrderDTO> removeOrder(@RequestBody RemoveOrderRequest removeOrderRequest){
        return  orderInfoService.removeOrder(removeOrderRequest);
    }

    /** 
     * @description:  调整订单地址
     * @param adjustDeliveryAddressRequest 调整订单地址请求
     * @return  调整订单地址请求结果
     * @author Long
     * @date: 2022/2/26 19:48
     */
    @ApiOperation("调整订单地址")
    @PostMapping("/adjustDeliveryAddress")
    public ResponseData<AdjustDeliveryAddressDTO>  adjustDeliveryAddress(@RequestBody AdjustDeliveryAddressRequest adjustDeliveryAddressRequest){

        return orderInfoService.adjustDeliveryAddress(adjustDeliveryAddressRequest);
    }

    /**
     * @description:  查询订单集合
     * @param orderQuery 订单查询
     * @return  订单集合
     * @author Long
     * @date: 2022/2/26 19:48
     */
    @ApiOperation("查询订单集合")
    @PostMapping("/listOrders")
    public  ResponseData<PagingInfo<OrderListDTO>> listOrders(@RequestBody OrderQuery orderQuery){

        return orderQueryApi.listOrders(orderQuery);
    }

    /**
     * @description:  查询订单详情
     * @param orderId 订单iD
     * @return  订单详情
     * @author Long
     * @date: 2022/3/3 23:21
     */
    @ApiOperation("订单详情")
    @PostMapping("/listOrders")
    public ResponseData<OrderDetailDTO> orderDetail(@RequestParam("orderId") String orderId){

        return orderQueryApi.orderDetail(orderId);
    }

    /**
     * @description: 触发订单发货出库事件
     * @param event 订单发货出库事件
     * @return  结果
     * @author Long
     * @date: 2022/3/3 23:25
     */
    @ApiOperation("触发订单发货出库事件")
    @PostMapping("/triggerOrderOutStockWmsEvent")
    public ResponseData<Boolean> triggerOrderOutStockWmsEvent(@RequestBody OrderOutStockWmsEvent event){
        log.info("orderId={},event",event.getOrderId(), JSONObject.toJSONString(event));

        return fulFillApi.triggerOrderWmsShipEvent(event.getOrderId(), OrderStatusChangEnum.ORDER_OUT_STOCKED,event);

    }

    /**
     * @description: 触发订单发货出库事件
     * @param event 订单发货出库事件
     * @return  处理结果
     * @author Long
     * @date: 2022/3/3 23:25
     */
    @ApiOperation("触发订单发货出库事件")
    @PostMapping("/triggerOrderDeliveredWmsEvent")
    public  ResponseData<Boolean>  triggerOrderDeliveredWmsEvent (@RequestBody OrderDeliveredWmsEvent event){

        return fulFillApi.triggerOrderWmsShipEvent(event.getOrderId(),OrderStatusChangEnum.ORDER_DELIVERED,event);
    }

    /**
     * @description: 触发订单发货出库事件
     * @param event 订单发货出库事件
     * @return  处理结果
     * @author Long
     * @date: 2022/3/3 23:25
     */
    @ApiOperation("触发订单收货")
    @PostMapping("/triggerOrderDeliveredWmsEvent")
    public ResponseData<Boolean> triggerOrderSignedWmsEvent(@RequestBody OrderSignedWmsEvent event){
        return fulFillApi.triggerOrderWmsShipEvent(event.getOrderId(),OrderStatusChangEnum.ORDER_SIGNED,event);
    }


}
