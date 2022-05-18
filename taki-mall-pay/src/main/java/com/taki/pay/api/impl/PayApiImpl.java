package com.taki.pay.api.impl;

import com.taki.common.enums.PayTypeEnum;
import com.taki.common.utlis.RandomUtil;
import com.taki.common.utlis.ResponseData;
import com.taki.pay.api.PayApi;
import com.taki.pay.domian.dto.PayOrderDTO;
import com.taki.pay.domian.rquest.PayOrderRequest;
import com.taki.pay.domian.rquest.PayRefundRequest;
import com.taki.pay.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName PayApiImpl
 * @Description TODO
 * @Author Long
 * @Date 2022/5/18 15:49
 * @Version 1.0
 */
@DubboService(version = "1.0.0",retries = 0)
@Slf4j
public class PayApiImpl implements PayApi {



    @Autowired
    private PayService payService;


    @Override
    public ResponseData<PayOrderDTO> payOrder(PayOrderRequest payOrderRequest) {
        String orderId = payOrderRequest.getOrderId();
        BigDecimal payAmount = payOrderRequest.getPayAmount();
        String outTradeNo = RandomUtil.genRandomNumber(19);

        //模拟 调用 第三方支付平台的支付接口

        // 组装数据
        PayOrderDTO payOrderDTO = new PayOrderDTO();
        payOrderDTO.setOrderId(orderId);
        payOrderDTO.setOutTradeNo(outTradeNo);
        payOrderDTO.setPayType(PayTypeEnum.ALI_TYPE.getCode());

        Map<String,Object> payData = new HashMap<>();

        payData.put("appid","alipay225422222");
        payData.put("prepayId",RandomUtil.genRandomNumber(11));
        payData.put("payAmount",payAmount);
        payData.put("webUrl","http://www.baidu.com");
        payOrderDTO.setPayData(payData);

        return ResponseData.success(payOrderDTO);
    }

    @Override
    public ResponseData<Boolean> executeRefund(PayRefundRequest payRefundRequest) {
        log.info("调用支付接口执行退款，订单号：{}，售后订单号：{}",payRefundRequest.getOrderId(),payRefundRequest.getAfterSaleId());

        return ResponseData.success(true);
    }

    @Override
    public ResponseData<Boolean> getTradeNoByRealTime(String orderId, Integer businessIdentifier) {
        return ResponseData.success(payService.getTradeNoByRealTime(orderId,businessIdentifier));
    }
}
