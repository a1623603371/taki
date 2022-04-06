package com.taki.order.api.impl;

import com.taki.common.utlis.ResponseData;
import com.taki.customer.domain.request.CustomReviewReturnGoodsRequest;
import com.taki.order.api.AfterSaleApi;
import com.taki.order.domain.dto.CheckLackDTO;
import com.taki.order.domain.dto.LackDTO;
import com.taki.order.domain.request.*;
import com.taki.order.exception.OrderBizException;
import com.taki.order.service.OrderAfterSaleService;
import com.taki.order.service.OrderLackService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.SimpleTriggerContext;

/**
 * @ClassName AfterSaleApiImpl
 * @Description 售后Api
 * @Author Long
 * @Date 2022/3/9 15:07
 * @Version 1.0
 */
@Slf4j
@DubboService(version = "1.0.0",retries = 1)
public class AfterSaleApiImpl implements AfterSaleApi {


    @Autowired
    private OrderAfterSaleService orderAfterSaleService;


    @Autowired
    private OrderLackService orderLackService;

    @Override
    public ResponseData<Boolean> cancelOrder(CancelOrderRequest cancelOrderRequest) {

        try {
            Boolean success = orderAfterSaleService.cancelOrder(cancelOrderRequest);
            return ResponseData.success(success);
        }catch (OrderBizException e){
            log.error("biz error",e);
            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());
        }catch (Exception e){
            log.error("system error",e);
            return ResponseData.error(e.getMessage());
        }

    }

    @Override
    public ResponseData<LackDTO> lockItem(LackRequest lackRequest) {
        try {
            // 1.參數效驗
            CheckLackDTO checkResult = orderLackService.checkRequest(lackRequest);
            // 2.缺品處理
            return ResponseData.success(orderLackService.executeLackRequest(lackRequest,checkResult));
        }catch (OrderBizException e){
            log.error("biz error",e);
            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());
        }catch (Exception e){
            log.error("system error",e);
            return ResponseData.error(e.getMessage());
        }
    }

    @Override
    public ResponseData<Boolean> refundCallback(RefundCallbackRequest refundCallbackRequest) {
        String orderId = refundCallbackRequest.getOrderId();

        log.info("接受到取消订单支付退款回调，orderId：{}",orderId);

        return ResponseData.success(orderAfterSaleService.receivePaymentRefundCallback(refundCallbackRequest));
    }

    @Override
    public ResponseData<Boolean> receiveCustomerAuditResult(CustomerAuditAssembleRequest customerAuditAssembleRequest) {
        return ResponseData.success(orderAfterSaleService.receiveCustomerAuditResult(customerAuditAssembleRequest));
    }

    @Override
    public ResponseData<Boolean> revokeAfterSale(RevokeAfterSaleRequest revokeAfterSaleRequest) {
        return null;
    }
}
