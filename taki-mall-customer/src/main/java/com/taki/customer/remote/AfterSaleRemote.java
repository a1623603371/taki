package com.taki.customer.remote;

import com.taki.common.utli.ResponseData;
import com.taki.customer.domain.request.CustomReviewReturnGoodsRequest;
import com.taki.customer.domain.request.CustomerReceiveAfterSaleRequest;
import com.taki.customer.exception.CustomerBizException;
import com.taki.order.api.AfterSaleApi;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @ClassName AfterSaleRemote
 * @Description TODO
 * @Author Long
 * @Date 2022/7/30 20:45
 * @Version 1.0
 */
@Component
public class AfterSaleRemote {

    @DubboReference(version = "1.0.0",retries = 0)
    private AfterSaleApi afterSaleApi;

    /** 
     * @description:  接受客服审核通过
     * @param 
     * @return  java.lang.Long
     * @author Long
     * @date: 2022/7/30 20:47
     */ 
    public Boolean receiveCustomerAuditResult(CustomReviewReturnGoodsRequest customReviewReturnGoodsRequest){

        ResponseData<Boolean> result = afterSaleApi.receiveCustomerAuditResult(customReviewReturnGoodsRequest);

        if (!result.getSuccess()){
            throw new CustomerBizException(result.getCode(),result.getMessage());
        }
        return result.getData();

        
    }

    /** 
     * @description: 客服系统查询售后支付信息
     * @param 
     * @return  java.lang.Boolean
     * @author Long
     * @date: 2022/7/30 20:52
     */ 
    public Long customerFindAfterSaleRefundInfo(CustomerReceiveAfterSaleRequest customerReceiveAfterSaleRequest){

        ResponseData<Long> result = afterSaleApi.customerFindAfterSaleRefundInfo(customerReceiveAfterSaleRequest);

        if (!result.getSuccess()){
            throw new CustomerBizException(result.getCode(),result.getMessage());
        }

        return result.getData();
        
    }
}
