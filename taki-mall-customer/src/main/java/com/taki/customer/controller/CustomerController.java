package com.taki.customer.controller;

import com.taki.common.constants.RedisLockKeyConstants;
import com.taki.common.redis.RedisLock;
import com.taki.common.utli.ResponseData;
import com.taki.customer.domain.request.CustomReviewReturnGoodsRequest;
import com.taki.customer.domain.request.CustomerReceiveAfterSaleRequest;
import com.taki.customer.exception.CustomerBizException;
import com.taki.customer.exception.CustomerErrorCodeEnum;
import com.taki.customer.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName CustomerController
 * @Description 客服
 * @Author Long
 * @Date 2022/7/30 20:26
 * @Version 1.0
 */
@RestController
@RequestMapping("/customer")
@Slf4j
public class CustomerController {


    @Autowired
    private CustomerService customerService;

    @Autowired
    private RedisLock redisLock;

    /*** 
     * @description: 客服售后审核
     * @param customReviewReturnGoodsRequest 服售后审核请求
     * @return
     * @author Long
     * @date: 2022/9/17 20:29
     */ 
    @PostMapping("/audit")
    public ResponseData<Boolean> audit(CustomReviewReturnGoodsRequest customReviewReturnGoodsRequest){
        Long afterSaleId = customReviewReturnGoodsRequest.getAfterSaleId();

        //分布式锁
        String key = RedisLockKeyConstants.REFUND_KEY + afterSaleId;
        Boolean lock = redisLock.tryLock(key);
        if (!lock){
            throw new CustomerBizException(CustomerErrorCodeEnum.CUSTOMER_AUDIT_CANNOT_REPEAT);
        }

        try {
            return ResponseData.success(customerService.customerAudit(customReviewReturnGoodsRequest)) ;
        }catch (Exception e){
            log.error("system error",e);
            return  ResponseData.error(e.getMessage());
        }finally {
            redisLock.unLock(key);
        }

    }
}
