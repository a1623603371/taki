package com.taki.order.domain.dto;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName PrePayOrderDTO
 * @Description 预付订单
 * @Author Long
 * @Date 2022/1/15 14:33
 * @Version 1.0
 */
@Data
public class PrePayOrderDTO  implements Serializable {


    private static final long serialVersionUID = 5036439332554276163L;

    /**
     *
     * 订单Id
     */
    private String orderId;

    /**
     * 支付系统交易号
     */
    private String outTradeNo;

    /**
     * 支付类型
     */
    private Integer payType;

    /**
     * 支付系统返回信息
     *不同字符方式，返回的内容不同
     */
    private Map<String,Object> payData;
}
