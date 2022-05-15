package com.taki.fulfill.saga;

import com.taki.fulfill.domain.request.ReceiveFulFillRequest;

/**
 * @ClassName FulfillSagaService
 * @Description TODO
 * @Author Long
 * @Date 2022/5/15 23:11
 * @Version 1.0
 */
public interface FulfillSagaService {

    /** 
     * @description: 创建 履约单
     * @param request 创建履约单请求
     * @return  java.lang.Boolean
     * @author Long
     * @date: 2022/5/15 23:12
     */ 
    Boolean createFulfillOrder(ReceiveFulFillRequest request);


    /** 
     * @description:  补偿创建履约单
     * @param request
     * @return  java.lang.Boolean
     * @author Long
     * @date: 2022/5/15 23:13
     */ 
    Boolean createFulfillOrderCompensate(ReceiveFulFillRequest request);
}
