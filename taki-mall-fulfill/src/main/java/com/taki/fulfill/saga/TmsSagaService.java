package com.taki.fulfill.saga;

import com.taki.fulfill.domain.request.ReceiveFulFillRequest;

/**
 * @ClassName TmsSageService
 * @Description tms saga service

 * @Author Long
 * @Date 2022/5/17 15:55
 * @Version 1.0
 */
public interface TmsSagaService {

    /** 
     * @description:  发货
     * @param request 履约请求参数
     * @return 处理结果
     * @author Long
     * @date: 2022/5/17 15:56
     */ 
    Boolean senOut(ReceiveFulFillRequest request);

    /**
     * @description: 取消 发货
     * @param request 履约请求参数
     * @return  处理结果
     * @author Long
     * @date: 2022/5/17 15:56
     */
    Boolean canOutCompensate(ReceiveFulFillRequest request);
}
