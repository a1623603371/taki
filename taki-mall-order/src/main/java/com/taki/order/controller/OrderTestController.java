package com.taki.order.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONObject;
import com.taki.common.constants.RocketMQConstant;
import com.taki.common.enums.OrderStatusChangEnum;
import com.taki.common.message.PaidOrderSuccessMessage;
import com.taki.common.page.PagingInfo;
import com.taki.common.utli.ResponseData;
import com.taki.fulfill.api.FulFillApi;
import com.taki.fulfill.domain.evnet.OrderDeliveredWmsEvent;
import com.taki.fulfill.domain.evnet.OrderOutStockWmsEvent;
import com.taki.fulfill.domain.evnet.OrderSignedWmsEvent;

import com.taki.fulfill.domain.request.ReceiveFulfillRequest;
import com.taki.fulfill.domain.request.TriggerOrderWmsShipEventRequest;
import com.taki.order.dao.OrderInfoDao;
import com.taki.order.domain.dto.*;
import com.taki.order.domain.entity.OrderInfoDO;
import com.taki.order.domain.query.OrderQuery;
import com.taki.order.domain.request.RemoveOrderRequest;
import com.taki.order.domain.request.*;
import com.taki.order.mq.producer.DefaultProducer;
import com.taki.order.service.OrderInfoService;
import com.taki.order.service.OrderQueryService;
import com.taki.order.service.impl.OrderFulFillServiceImpl;
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


    @Autowired
    private OrderInfoService orderInfoService;


    @Autowired
    private OrderQueryService orderQueryService;




    @DubboReference(version = "1.0.0",retries = 0)
    private FulFillApi fulFillApi;

    @Autowired
    private DefaultProducer defaultProducer;


    @Autowired
    private OrderInfoDao orderInfoDao;

    @Autowired
    private OrderFulFillServiceImpl orderFulFillService;
    /**
     * @description: 生成订单Id
     * @param genOrderIdRequest 生成订单Id请求
     * @return  响应结果数据
     * @author Long
     * @date: 2022/1/14 17:30
     */
    @ApiOperation("生成订单Id")
    @PostMapping("/genOrderId")
    @SentinelResource("OrderTestController:genOrderId")
    public ResponseData<GenOrderIdDTO> genOrderId(@RequestBody GenOrderIdRequest genOrderIdRequest){

        return ResponseData.success(orderInfoService.getGenOrderId(genOrderIdRequest)) ;
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
    @SentinelResource("OrderTestController:createOrder")
    public  ResponseData<CreateOrderDTO> createOrder(@RequestBody CreateOrderRequest createOrderRequest)  {

        return ResponseData.success(orderInfoService.createOrder(createOrderRequest));
    }


    /**
     * @description: 预支付
     * @param payOrderRequest 预支付请求
     * @return  响应结果数据
     * @author Long
     * @date: 2022/2/16 15:43
     */
    @ApiOperation("预支付")
    @SentinelResource("OrderTestController:prePayOrder")
    @PostMapping("/prePayOrder")
    public ResponseData<PrePayOrderDTO> prepayOrder(@RequestBody PrePayOrderRequest payOrderRequest) {

        return  ResponseData.success(orderInfoService.prePayOrder(payOrderRequest));
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
    @SentinelResource("OrderTestController:payCallback")
    public ResponseData<Boolean> payCallback(@RequestBody PayCallbackRequest payCallbackRequest){
        orderInfoService.payCallback(payCallbackRequest);
        return ResponseData.success(true);
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
    public ResponseData<RemoveOrderDTO> removeOrders(@RequestBody RemoveOrderRequest removeOrderRequest){
      //  return  orderInfoService.removeOrder(removeOrderRequest);
        return null;
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
        Boolean result =   orderInfoService.adjustDeliveryAddress(adjustDeliveryAddressRequest);
        return ResponseData.success(new AdjustDeliveryAddressDTO(result));
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

        return ResponseData.success(orderQueryService.executeListOrderQuery(orderQuery));
    }

    /**
     * @description:  查询订单详情
     * @param orderId 订单iD
     * @return  订单详情
     * @author Long
     * @date: 2022/3/3 23:21
     */
    @ApiOperation("订单详情")
    @GetMapping("/orderDetail")
    public ResponseData<OrderDetailDTO> orderDetail(@RequestParam("orderId") String orderId) throws Exception {
//        if (true) {
//            throw new Exception();
//        }
        return ResponseData.success(orderQueryService.orderDetail(orderId));
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
    public ResponseData<Boolean> triggerOrderOutStockWmsEvent(
            @RequestParam("orderId")  String orderId,
            @RequestParam("fulfillId") String fulfillId,
            @RequestBody OrderOutStockWmsEvent event){
        log.info("orderId={},fulfillId = {},event={}",event.getOrderId(),fulfillId ,JSONObject.toJSONString(event));

        TriggerOrderWmsShipEventRequest request = new TriggerOrderWmsShipEventRequest(orderId,fulfillId,OrderStatusChangEnum.ORDER_OUT_STOCKED,event);
        return fulFillApi.triggerOrderWmsShipEvent(request);

    }

    /**
     * @description: 触发订单配送事件
     * @param event 订单配送事件
     * @return  处理结果
     * @author Long
     * @date: 2022/3/3 23:25
     */
    @ApiOperation("触发订单配送事件")
    @PostMapping("/triggerOrderDeliveredWmsEvent")
    public  ResponseData<Boolean>  triggerOrderDeliveredWmsEvent (
            @RequestParam("orderId")  String orderId,
            @RequestParam("fulfillId") String fulfillId,@RequestBody OrderDeliveredWmsEvent event){

        log.info("orderId={},fulfillId = {},event={}",event.getOrderId(),fulfillId ,JSONObject.toJSONString(event));

        TriggerOrderWmsShipEventRequest request = new TriggerOrderWmsShipEventRequest(orderId,fulfillId,OrderStatusChangEnum.ORDER_DELIVERED,event);

        return fulFillApi.triggerOrderWmsShipEvent(request);
    }

    /**
     * @description: 触发订单收货
     * @param event 订单收货事件
     * @return  处理结果
     * @author Long
     * @date: 2022/3/3 23:25
     */
    @ApiOperation("触发订单收货")
    @PostMapping("/triggerOrderSignedWmsEvent")
    public ResponseData<Boolean> triggerOrderSignedWmsEvent( @RequestParam("orderId")  String orderId,
                                                             @RequestParam("fulfillId") String fulfillId,@RequestBody OrderSignedWmsEvent event){
        log.info("orderId={},fulfillId = {},event={}",event.getOrderId(),fulfillId ,JSONObject.toJSONString(event));

        TriggerOrderWmsShipEventRequest request = new TriggerOrderWmsShipEventRequest(orderId,fulfillId,OrderStatusChangEnum.ORDER_SIGNED       ,event);


        return fulFillApi.triggerOrderWmsShipEvent(request);
    }


    /**
     * @description: 触发订单已支付时间
     * @param orderId 订单Id
     * @return  com.taki.common.utlis.ResponseData<java.lang.Boolean>
     * @author Long
     * @date: 2022/5/17 18:47
     */
    @PostMapping("/triggerOrderPaidEvent")
    @ApiOperation("触发订单已支付时间")
    public ResponseData<Boolean> triggerOrderPaidEvent(@RequestParam("orderId") String orderId){

        log.info("orderId = {}",orderId);

        PaidOrderSuccessMessage paidOrderSuccessMessage = new PaidOrderSuccessMessage();
        paidOrderSuccessMessage.setOrderId(orderId);

        String message = JSONObject.toJSONString(paidOrderSuccessMessage);

        defaultProducer.sendMessage(RocketMQConstant.PAID_ORDER_SUCCESS_TOPIC,message,"订单支付已完成支付","","");

        return ResponseData.success(true);
    }

    /**
     * @description: 触发订单履约
     * @param orderId 订单Id
     * @return
     * @author Long
     * @date: 2022/5/17 18:47
     */
    @PostMapping("/triggerReceiveOrderFulFill")
    @ApiOperation("触发订单履约")
    public ResponseData<Boolean> triggerReceiveOrderFulFill(@RequestParam("orderId") String orderId
    ,@RequestParam("fulfillException") String  fulfillException,@RequestParam("wmsException") String wmsException,
                                                            @RequestParam("tmsException")  String tmsException){

        log.info("orderId = {}",orderId);

        OrderInfoDO orderInfo = orderInfoDao.getByOrderId(orderId);

        ReceiveFulfillRequest request = orderFulFillService.builderReceiveFulFillRequest(orderInfo);

        request.setWmsException(wmsException);
        request.setTmsException(tmsException);
        request.setFulfillException(fulfillException);

        return  fulFillApi.receiveOrderFulFill(request);
    }

}
