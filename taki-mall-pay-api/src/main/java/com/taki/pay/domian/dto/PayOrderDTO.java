package com.taki.pay.domian.dto;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName PayOrderDTO
 * @Description 支付订单
 * @Author Long
 * @Date 2022/1/16 15:24
 * @Version 1.0
 */
@Data
public class PayOrderDTO extends AbstractObject implements Serializable {


    private static final long serialVersionUID = -5678424294868382430L;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 支付系统 交易单号
     */
    private String outTradeNo;

    /**
     * 支付类型
     */
    private   Integer payType;

    /**
     * 第三方支付平台支付数据
     */
    private Map<String, Object> payData;
}
