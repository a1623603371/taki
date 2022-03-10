package com.taki.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sun.corba.se.spi.ior.IdentifiableFactory;
import com.taki.common.constants.RedisLockKeyConstants;
import com.taki.common.constants.RocketMQConstant;
import com.taki.common.enums.OrderOperateTypeEnum;
import com.taki.common.enums.OrderStatusEnum;
import com.taki.common.redis.RedisLock;
import com.taki.common.utlis.ObjectUtil;
import com.taki.common.utlis.ParamCheckUtil;
import com.taki.common.utlis.ResponseData;
import com.taki.fulfill.api.FulFillApi;
import com.taki.fulfill.domain.request.CancelFulfillRequest;
import com.taki.order.dao.OrderInfoDao;
import com.taki.order.dao.OrderItemDao;
import com.taki.order.dao.OrderOperateLogDao;
import com.taki.order.domain.dto.OrderInfoDTO;
import com.taki.order.domain.dto.OrderItemDTO;
import com.taki.order.domain.entity.OrderInfoDO;
import com.taki.order.domain.entity.OrderItemDO;
import com.taki.order.domain.entity.OrderOperateLogDO;
import com.taki.order.domain.request.CancelOrderAssembleRequest;
import com.taki.order.domain.request.CancelOrderRequest;
import com.taki.order.enums.OrderCancelTypeEnum;
import com.taki.order.exception.OrderBizException;
import com.taki.order.exception.OrderErrorCodeEnum;
import com.taki.order.mq.producer.DefaultProducer;
import com.taki.order.service.OrderAfterSaleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName OrderAfterSaleServiceImpl
 * @Description 订单售后 service
 * @Author Long
 * @Date 2022/3/8 17:05
 * @Version 1.0
 */
@Service
@Slf4j
public class OrderAfterSaleServiceImpl implements OrderAfterSaleService {



    @Autowired
    private RedisLock redisLock;


    @Autowired
    private OrderInfoDao orderInfoDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private OrderOperateLogDao orderOperateLogDao;


    @DubboReference
    private FulFillApi fulFillApi;

    @Autowired
    private DefaultProducer defaultProducer;

    @Override
    public Boolean cancelOrder(CancelOrderRequest cancelOrderRequest) {

        // 入参检查
        checkCancelOrderRequestPram(cancelOrderRequest);
        //分布式锁
        String orderId = cancelOrderRequest.getOrderId();

        String key = RedisLockKeyConstants.CANCEL_KEY + orderId;

        try {
            Boolean lock = redisLock.lock(key);

            if (!lock){
                throw new OrderBizException(OrderErrorCodeEnum.CANCEL_ORDER_REPEAT);
            }

            // 执行取消订单
            executeCancelOrder(cancelOrderRequest,orderId);

            return true;
        }catch (Exception e){
            throw new OrderBizException(e.getMessage());
        }
        finally {
            redisLock.unLock(key);
        }
    }

    /** 
     * @description: z执行取消订单
     * @param cancelOrderRequest 取消订单请求
     * @param orderId 订单Id
     * @return  void
     * @author Long
     * @date: 2022/3/9 10:44
     */ 
    private void executeCancelOrder(CancelOrderRequest cancelOrderRequest, String orderId) {
        //1.组装数据
        CancelOrderAssembleRequest request = buildAssembleRequest(orderId,cancelOrderRequest);

        // 2 幂等效验 防止多线程同时操作取消同一订单
        if (OrderStatusEnum.CREATED.getCode().equals(request.getOrderInfo().getOrderStatus())){
            return;
        }
        // 3.更新订单状态 和记录 订单操作
        updateOrderStatusAndSaveOperationLog(request);

        //4.超时未支付的订单不用继续在执行取消
        if (OrderStatusEnum.PAID.getCode() > request.getOrderInfo().getOrderStatus()){
          return;
        }

        // 4. 履约取消
        cancelFulfill(request);

        // 5. 释放资产
        defaultProducer.sendMessage(RocketMQConstant.RELEASE_ASSETS_TOPIC, JSONObject.toJSONString(cancelOrderRequest),"释放资产");
    }

    /** 
     * @description: 履约取消
     * @param request 取消组装数据请求
     * @return  void
     * @author Long
     * @date: 2022/3/9 14:15
     */ 
    private void cancelFulfill(CancelOrderAssembleRequest request) {
        OrderInfoDTO orderInfoDTO = request.getOrderInfo();
        CancelFulfillRequest cancelFulfillRequest = orderInfoDTO.clone(CancelFulfillRequest.class);

        ResponseData<Boolean> response = fulFillApi.cancelFulfill(cancelFulfillRequest);

        if (!response.getSuccess()){
            throw new OrderBizException(OrderErrorCodeEnum.CANCEL_ORDER_FULFILL_ERROR);
        }
    }

    /**
     * @description: 修改订单状态并记录订单操作
     * @param request 取消订单请求
     * @return  void
     * @author Long
     * @date: 2022/3/9 11:42
     */
    private void updateOrderStatusAndSaveOperationLog(CancelOrderAssembleRequest request) {
        // 更新表
        OrderInfoDO orderInfoDO =request.getOrderInfo().clone(OrderInfoDO.class);
        orderInfoDO.setCancelType(request.getCancelType());
        orderInfoDO.setOrderStatus(OrderStatusEnum.CANCELED.getCode());
        orderInfoDO.setCancelTime(LocalDateTime.now());
        orderInfoDao.updateById(orderInfoDO);
        log.info("更新订单信息OrderInfo状态：orderId:{},status:{}",orderInfoDO.getOrderId(),orderInfoDO.getOrderStatus() );

        // 新增订单操作日志表
        Integer cancelTType = Integer.valueOf(orderInfoDO.getCancelType());
        String orderId = orderInfoDO.getOrderId();
        OrderOperateLogDO orderOperateLog = OrderOperateLogDO
                .builder()
                .orderId(orderId)
                .preStatus(request.getOrderInfo().getOrderStatus())
                .currentStatus(OrderStatusEnum.CREATED.getCode())
                .operateType(OrderOperateTypeEnum.AUTO_CANCEL_ORDER.getCode()).build();

        if (OrderCancelTypeEnum.USE_CANCELED.getCode().equals(cancelTType)){
            orderOperateLog.setOperateType(OrderOperateTypeEnum.MANUAL_CANCEL_ORDER.getCode());
            orderOperateLog.setRemark(OrderOperateTypeEnum.MANUAL_CANCEL_ORDER.getSmg() + orderOperateLog.getPreStatus() + "-" + orderOperateLog.getCurrentStatus());

        }

        if (OrderCancelTypeEnum.TIMEOUT_CANCELED.getCode().equals(cancelTType)){
            orderOperateLog.setOperateType(OrderOperateTypeEnum.AUTO_CANCEL_ORDER.getCode());
            orderOperateLog.setRemark(OrderOperateTypeEnum.AUTO_CANCEL_ORDER.getSmg() + orderOperateLog.getPreStatus() + "-" + orderOperateLog.getCurrentStatus());

        }
        orderOperateLogDao.save(orderOperateLog);

        log.info("新增订单操作日志 OrderOperateLog状态，orderId:{},PreStatus:{},CurrentStatus:{}",orderId,orderOperateLog.getPreStatus(),orderOperateLog.getCurrentStatus());
    }

    /** 
     * @description: 构建 取消订单 数据
     * @param orderId 订单Id
     * @param cancelOrderRequest 取消订单请求
     * @return
     * @author Long
     * @date: 2022/3/9 10:58
     */ 
    private CancelOrderAssembleRequest buildAssembleRequest(String orderId, CancelOrderRequest cancelOrderRequest) {

        Integer cancelType = cancelOrderRequest.getCancelType();

        OrderInfoDTO orderInfo =  findOrderInfo(orderId,cancelType);
        List<OrderItemDTO> orderItems = findOrderItems(orderId);
        CancelOrderAssembleRequest request = cancelOrderRequest.clone(CancelOrderAssembleRequest.class);
        request.setOrderId(orderId);
        request.setCancelType(cancelType);
        request.setOrderInfo(orderInfo);
        request.setOrderItems(orderItems);
        return request;

    }

    /**
     * @description: 查询 订单 条目
     * @param orderId 订单Id
     * @return  订单条目DTO
     * @author Long
     * @date: 2022/3/9 11:22
     */
    private List<OrderItemDTO> findOrderItems(String orderId) {
    List<OrderItemDO> orderItems = orderItemDao.listByOrderId(orderId);

    if (CollectionUtils.isEmpty(orderItems)){
        throw new OrderBizException(OrderErrorCodeEnum.ORDER_ITEM_IS_NULL);
    }

    List<OrderItemDTO> orderItemDTOs = ObjectUtil.convertList(orderItems,OrderItemDTO.class);

        return orderItemDTOs;
    }

    /**
     * @description: 查询 订单 信息
     * @param orderId 订单Id
     * @param cancelType 取消类型
     * @return  订单信息
     * @author Long
     * @date: 2022/3/9 11:09
     */
    private OrderInfoDTO findOrderInfo(String orderId, Integer cancelType) {

        OrderInfoDO orderInfo = orderInfoDao.getByOrderId(orderId);

        if (ObjectUtils.isEmpty(orderId)){
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_INFO_IS_NULL);
        }

        OrderInfoDTO orderInfoDTO = orderInfo.clone(OrderInfoDTO.class);
        orderInfoDTO.setCancelType(String.valueOf(cancelType));

        return orderInfoDTO;
    }

    /**
     * @description: 入参检查
     * @param cancelOrderRequest 取消订单请求
     * @return  void
     * @author Long
     * @date: 2022/3/8 17:17
     */
    private void checkCancelOrderRequestPram(CancelOrderRequest cancelOrderRequest) {
        ParamCheckUtil.checkObjectNonNull(cancelOrderRequest);

        // 订单状态
        Integer orderStatus = cancelOrderRequest.getOrderStatus();
        ParamCheckUtil.checkObjectNonNull(orderStatus, OrderErrorCodeEnum.ORDER_STATUS_IS_NULL);

        if (orderStatus >= OrderStatusEnum.OUT_STOCK.getCode()){
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_STATUS_CHANGED);
        }

        if (orderStatus.equals(OrderStatusEnum.CANCELED.getCode())){
                throw new OrderBizException(OrderErrorCodeEnum.ORDER_STATUS_CANCELED);
        }

        // 订单 ID
        String orderId = cancelOrderRequest.getOrderId();
        ParamCheckUtil.checkStringNonEmpty(orderId,OrderErrorCodeEnum.ORDER_ID_IS_NULL);

        // 业务线标识
        Integer businessIdentifier = cancelOrderRequest.getBusinessIdentifier();
        ParamCheckUtil.checkObjectNonNull(businessIdentifier,OrderErrorCodeEnum.BUSINESS_IDENTIFIER_IS_NULL);

        // 订单取消类型
        Integer cancelType = cancelOrderRequest.getCancelType();
        ParamCheckUtil.checkObjectNonNull(cancelType,OrderErrorCodeEnum.ORDER_TYPE_IS_NULL);

        // 用户Id
        String userId = cancelOrderRequest.getUserId();
        ParamCheckUtil.checkStringNonEmpty(userId,OrderErrorCodeEnum.USER_ID_IS_NULL);

        // 订单类型
        Integer orderType = cancelOrderRequest.getOrderType();
        ParamCheckUtil.checkObjectNonNull(orderType,OrderErrorCodeEnum.ORDER_TYPE_IS_NULL);


    }
}
