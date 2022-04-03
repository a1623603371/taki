package com.taki.order.service;

import com.taki.common.utlis.ResponseData;
import com.taki.order.domain.request.CancelOrderRequest;
import com.taki.order.domain.request.ReturnGoodsOrderRequest;

/**
 * @ClassName OrderAfterSaleService
 * @Description 订单售后流程
 * @Author Long
 * @Date 2022/3/8 17:00
 * @Version 1.0
 */
public interface OrderAfterSaleService {

    /**
     * @description: 取消订单/超时未支付取消 人口
     * <p>
     * 有3个 调用方法：
     * 1.用户手动取消，订单出库状态之前都可以取消
     * 2.消费者正向生单的MQ取消，要先判断支付状态。 未支付可以取消
     * 3.定时取消 定时任务扫描，超过 30 分钟 ，未支付才取消
     * @param cancelOrderRequest 取消订单请求
     * @return 处理结果
     * @author Long
     * @date: 2022/3/8 17:04
     */
    Boolean cancelOrder(CancelOrderRequest cancelOrderRequest);
    
    /** 
     * @description: 处理售后 申请
     * @param request 退货申请请求 参数
     * @return
     * @author Long
     * @date: 2022/4/3 15:32
     */ 
    Boolean processApplyAfterSale(ReturnGoodsOrderRequest request);
}
