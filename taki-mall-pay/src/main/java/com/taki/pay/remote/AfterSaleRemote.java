package com.taki.pay.remote;

import com.taki.common.utlis.ResponseData;
import com.taki.order.api.AfterSaleApi;
import com.taki.order.domain.request.RefundCallbackRequest;
import com.taki.pay.exception.PayBizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName AfterSaleRemote
 * @Description 订单售后运程接口
 * @Author Long
 * @Date 2022/6/21 10:12
 * @Version 1.0
 */
@Component
@Slf4j
public class AfterSaleRemote {

    @DubboReference(version = "1.0.0",retries = 0)
    private AfterSaleApi afterSaleApi;


    /** 
     * @description:  取消订单支付回调
     * @param request
     * @return
     * @author Long
     * @date: 2022/6/21 10:34
     */ 
    public ResponseData<Boolean> refundCallBack(RefundCallbackRequest request){

        ResponseData<Boolean> result = afterSaleApi.refundCallback(request);

        if (!result.getSuccess()){
            throw new PayBizException(result.getCode(),result.getMessage());
        }

        return  result;

    }


}
