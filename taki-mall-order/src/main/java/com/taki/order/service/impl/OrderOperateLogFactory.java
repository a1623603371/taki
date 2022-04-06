package com.taki.order.service.impl;

import com.taki.common.enums.OrderOperateTypeEnum;
import com.taki.common.enums.OrderStatusChangEnum;
import com.taki.order.dao.OrderOperateLogDao;
import com.taki.order.domain.entity.OrderInfoDO;
import com.taki.order.domain.entity.OrderOperateLogDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName OrderOperateLogFactory
 * @Description 订单操作日志工厂
 * @Author Long
 * @Date 2022/4/6 15:24
 * @Version 1.0
 */
@Component
public class OrderOperateLogFactory {


    @Autowired
    private OrderOperateLogDao orderOperateLogDao;

    
    /** 
     * @description: 获取订单操作日志
     * @param orderInfo 订单信息
     * @param statusChangEnum 订单状态变更
     * @return 订单操作日志
     * @author Long
     * @date: 2022/4/6 15:26
     */ 
    public  OrderOperateLogDO get(OrderInfoDO orderInfo, OrderStatusChangEnum statusChangEnum){
        OrderOperateTypeEnum orderOperateType = statusChangEnum.getOperateType();

        Integer preStatus = statusChangEnum.getPreStatus().getCode();
        Integer currentStatus = statusChangEnum.getCurrentStatus().getCode();

        return cretate(orderInfo,orderOperateType,preStatus,currentStatus,orderOperateType.getSmg());


    }

    /**
     * @description: 创建 订单操作日志
     * @param orderInfo 订单
     * @param orderOperateType 订单操作 类型
     * @param preStatus 变更前的状态
     * @param currentStatus 变更后的状态
     * @return 订单操作日志
     * @author Long
     * @date: 2022/4/6 15:28
     */
    private  OrderOperateLogDO cretate(OrderInfoDO orderInfo, OrderOperateTypeEnum orderOperateType, Integer preStatus, Integer currentStatus,String msg) {

        OrderOperateLogDO orderOperateLogDO = new OrderOperateLogDO();

        orderOperateLogDO.setOrderId(orderInfo.getOrderId());
        orderOperateLogDO.setOperateType(orderOperateType.getCode());
        orderOperateLogDO.setPreStatus(preStatus);
        orderOperateLogDO.setCurrentStatus(currentStatus);
        orderOperateLogDO.setRemark(msg);

        return orderOperateLogDO;


    }
}
