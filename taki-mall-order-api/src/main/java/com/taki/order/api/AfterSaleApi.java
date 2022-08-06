package com.taki.order.api;

import com.taki.common.utli.ResponseData;
import com.taki.customer.domain.request.CustomReviewReturnGoodsRequest;
import com.taki.customer.domain.request.CustomerReceiveAfterSaleRequest;
import com.taki.order.domain.dto.LackDTO;
import com.taki.order.domain.request.*;

/**
 * @ClassName AfterSaleApi
 * @Description 订单中心 -逆向售后业务接口
 * @Author Long
 * @Date 2022/3/9 14:37
 * @Version 1.0
 */
public interface AfterSaleApi {

    /** 
     * @description: 取消订单/超时取消订单
     * @param cancelOrderRequest 取消订单请求
     * @return  处理结果
     * @author Long
     * @date: 2022/3/9 14:39
     */ 
    ResponseData<Boolean> cancelOrder(CancelOrderRequest cancelOrderRequest);


    /** 
     * @description: 缺品
     * @param lackRequest 缺品 请求
      * @return  缺品
     * @author Long
     * @date: 2022/3/9 14:41
     */ 
    ResponseData<LackDTO> lockItem(LackRequest lackRequest);


    /** 
     * @description: 取消订单支付退款回调
     * @param refundCallbackRequest 支付退款回调
     * @return  处理结果
     * @author Long
     * @date: 2022/3/9 14:49
     */ 
    ResponseData<Boolean> refundCallback(RefundCallbackRequest refundCallbackRequest);


    /*** 
     * @description: 接受客户审核结果
     * @param customReviewReturnGoodsRequest 接受客户审核结果 请求
     * @return 处理结果
     * @author Long
     * @date: 2022/3/9 14:55
     */ 
    ResponseData<Boolean> receiveCustomerAuditResult(CustomReviewReturnGoodsRequest customReviewReturnGoodsRequest);


    /** 
     * @description:  用户撤销售后申请
     * @param revokeAfterSaleRequest 撤销售后申请请求
     * @return  处理结果
     * @author Long
     * @date: 2022/3/9 15:03
     */ 
    ResponseData<Boolean> revokeAfterSale(RevokeAfterSaleRequest revokeAfterSaleRequest);


    /** 
     * @description:  提供客服系统查询售后支付单信息
     * @param 
     * @return
     * @author Long
     * @date: 2022/6/9 18:57
     */ 
    ResponseData<Long> customerFindAfterSaleRefundInfo(CustomerReceiveAfterSaleRequest customerReceiveAfterSaleRequest);
}
