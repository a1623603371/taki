package com.taki.pay.service.impl;

import com.taki.pay.api.impl.PayApiImpl;
import com.taki.pay.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName PayServiceImpl
 * @Description 支付 service 实现类
 * @Author Long
 * @Date 2022/5/18 18:13
 * @Version 1.0
 */
@Service
@Slf4j
public class PayServiceImpl implements PayService {

    @Override
    public Boolean getTradeNoByRealTime(String orderId, Integer businessIdentifier) {
        log.info("调用支付接口，查询到改订单的支付信息");
        return null;
    }
}
