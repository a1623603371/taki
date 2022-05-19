package com.taki.order.service;

import com.taki.common.message.ActualRefundMessage;
import com.taki.common.utlis.ResponseData;
import com.taki.order.domain.dto.LackDTO;
import com.taki.order.domain.request.*;

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
     * @description: z执行取消订单
     * @param cancelOrderRequest 取消订单请求
     * @param orderId 订单Id
     * @return  void
     * @author Long
     * @date: 2022/3/9 10:44
     */
    Boolean executeCancelOrder(CancelOrderRequest cancelOrderRequest,String orderId);

    /** 
     * @description: 处理售后 申请
     * @param request 退货申请请求 参数
     * @return
     * @author Long
     * @date: 2022/4/3 15:32
     */ 
    Boolean processApplyAfterSale(ReturnGoodsOrderRequest request);

    /**
     * @description: 退款
     * @param actualRefundMessage 实际退款 消息
     * @return  处理结果
     * @author Long
     * @date: 2022/4/5 14:56
     */
   ResponseData<Boolean>  refundMoney(ActualRefundMessage actualRefundMessage);

    /** 
     * @description:  取消订单/超时未支付订单 执行退款 计算 金额，记录 售后信息 等准备工作
     * @param cancelOrderAssembleRequest 取消订单 数据集合 请求
     * @return 处理结果
     * @author Long
     * @date: 2022/4/5 18:48
     */
    ResponseData<Boolean> processCancelOrder(CancelOrderAssembleRequest cancelOrderAssembleRequest);
    
    /** 
     * @description: 取消订单支付退款 回调入口
     * @param refundCallbackRequest 支付退款 回调请求 参数
     * @return  java.lang.Object
     * @author Long
     * @date: 2022/4/6 17:42
     */ 
    Boolean receivePaymentRefundCallback(RefundCallbackRequest refundCallbackRequest);
    
    /** 
     * @description:  接受客服审核结果通过 入口
     * @param customerAuditAssembleRequest  客户审核组装请求
     * @return  java.lang.Object
     * @author Long
     * @date: 2022/4/6 18:24
     */ 
    Boolean receiveCustomerAuditAccept(CustomerAuditAssembleRequest customerAuditAssembleRequest);


    /**
     * @description: 接受客服审核结果 拒绝 入口
     * @param customerAuditAssembleRequest 客户审核组装请求
     * @return  java.lang.Boolean
     * @author Long
     * @date: 2022/5/19 18:14
     */
    Boolean receiveCustomerAuditReject(CustomerAuditAssembleRequest customerAuditAssembleRequest);

    /**
     * @description:  查询 客服 审核状态
     * @param afterSaleId 售后Id
     * @return  java.lang.Integer
     * @author Long
     * @date: 2022/5/19 21:28
     */
    Integer findCustomerAuditSaleStatus(Long afterSaleId);
    
    /** 
     * @description:  撤销售后申请
     * @param revokeAfterSaleRequest 撤销售后申请 请求
     * @return  void
     * @author Long
     * @date: 2022/5/19 21:43
     */ 
    Boolean revokeAfterSale(RevokeAfterSaleRequest revokeAfterSaleRequest);
}
