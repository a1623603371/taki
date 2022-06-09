package com.taki.order.service;

import com.taki.order.domain.dto.OrderInfoDTO;
import com.taki.order.domain.entity.OrderInfoDO;
import com.taki.order.domain.request.CancelOrderAssembleRequest;

/**
 * @ClassName AfterSaleManager
 * @Description 售后管理组件
 * @Author Long
 * @Date 2022/5/17 23:00
 * @Version 1.0
 */
public interface AfterSaleManager {

    /** 
     * @description: 执行履约取消 更新订单状态，新增订单日志操作
     * @param cancelOrderAssembleRequest 取消订单组装请求
     * @return  void
     * @author Long
     * @date: 2022/5/17 23:02
     */ 
    void cancelOrderFulfillmentAndUpdateOrderStatus(CancelOrderAssembleRequest  cancelOrderAssembleRequest);

    /** 
     * @description: 取消组装订单操作 记录售后信息
     * @param cancelOrderAssembleRequest 取消订单组转请求
     * @param afterSaleStatus  售后状态

     * @return  void
     * @author Long
     * @date: 2022/5/17 23:03
     */ 
    void insertCancelOrderAfterSale(CancelOrderAssembleRequest cancelOrderAssembleRequest, Integer afterSaleStatus );
}


