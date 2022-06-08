package com.taki.order.remote;

import com.taki.common.utlis.ResponseData;
import com.taki.order.exception.OrderBizException;
import com.taki.order.exception.OrderErrorCodeEnum;
import com.taki.pay.api.PayApi;
import com.taki.pay.domian.dto.PayOrderDTO;
import com.taki.pay.domian.rquest.PayOrderRequest;
import com.taki.pay.domian.rquest.PayRefundRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @ClassName PayRemote
 * @Description 支付服务运程API 接口
 * @Author Long
 * @Date 2022/6/8 14:09
 * @Version 1.0
 */
@Component
public class PayRemote {

    @DubboReference(version = "1.0.0 ",retries = 0)
    private PayApi payApi;


    /** 
     * @description: 调用运程支付进行预支付下单
     * @param payOrderRequest 预支付请求参数
     * @return
     * @author Long
     * @date: 2022/6/8 14:11
     */ 
    public PayOrderDTO payOrder(PayOrderRequest payOrderRequest){

        ResponseData<PayOrderDTO> responseResult = payApi.payOrder(payOrderRequest);

        if (!responseResult.getSuccess()){
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_PRE_PAY_ERROR);
        }

        return responseResult.getData();
    }


    /** 
     * @description: 调用支付系统执行退款
     * @param payRefundRequest 支付退款请求参数
     * @return  void
     * @author Long
     * @date: 2022/6/8 14:12
     */ 
    public void executeRefund(PayRefundRequest payRefundRequest){
        ResponseData<Boolean> responseResult = payApi.executeRefund(payRefundRequest);
        if (!responseResult.getSuccess()){
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_REFUND_AMOUNT_FAILED);
        }

    }
}
