package com.taki.tms.api;

import com.taki.common.utli.ResponseData;
import com.taki.tms.domain.dto.SendOutDTO;
import com.taki.tms.domain.request.SendOutRequest;

/**
 * @ClassName Tms
 * @Description TMS 服务
 * @Author Long
 * @Date 2022/5/17 14:37
 * @Version 1.0
 */
public interface TmsApi {
    
    /** 
     * @description: 发货
     * @param request 发货请求
     * @return
     * @author Long
     * @date: 2022/5/17 14:55
     */ 
    ResponseData<SendOutDTO> sendOut(SendOutRequest request);


    /**
     * @description: 取消 发货单
     * @param orderId 订单Id
     * @return  处理结果
     * @author Long
     * @date: 2022/5/17 14:56
     */
    ResponseData<Boolean> cancelSendOut(String orderId);
}
