package com.taki.customer.service;

import com.taki.customer.domain.request.CustomReviewReturnGoodsRequest;
import com.taki.customer.domain.request.CustomerReceiveAfterSaleRequest;

/**
 * @ClassName CustomerService
 * @Description 客服 service 组件
 * @Author Long
 * @Date 2022/7/30 20:27
 * @Version 1.0
 */
public interface CustomerService {

    /** 
     * @description: 接受 售后单
     * @param customerReceivesAfterSaleRequest
     * @return  java.lang.Boolean
     * @author Long
     * @date: 2022/7/30 20:29
     */ 
    Boolean receiveAfterSale(CustomerReceiveAfterSaleRequest customerReceivesAfterSaleRequest);


    /*** 
     * @description: 客服审核
     * @param customReviewReturnGoodsRequest
     * @return  java.lang.Boolean
     * @author Long
     * @date: 2022/7/30 20:30
     */ 
    Boolean customerAudit(CustomReviewReturnGoodsRequest customReviewReturnGoodsRequest);
}
