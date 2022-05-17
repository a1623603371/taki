package com.taki.tms.service;

import com.taki.tms.domain.dto.SendOutDTO;
import com.taki.tms.domain.request.SendOutRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName LogisticOrderService
 * @Description 物流单 service 组件
 * @Author Long
 * @Date 2022/5/17 15:10
 * @Version 1.0
 */
public interface LogisticOrderService {
    
    /** 
     * @description: 发货
     * @param request 发货请求
     * @return
     * @author Long
     * @date: 2022/5/17 15:20
     */ 
    SendOutDTO sendOut(SendOutRequest request);


    /**
     * @description: 取消 发货
     * @param orderId 订单Id
     * @return
     * @author Long
     * @date: 2022/5/17 15:34
     */
    Boolean cancelSendOut(String orderId);
}
