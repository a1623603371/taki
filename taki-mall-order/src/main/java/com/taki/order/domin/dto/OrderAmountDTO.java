package com.taki.order.domin.dto;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName CalculateOrderAmountDTO
 * @Description 营销计算费用
 * @Author Long
 * @Date 2022/1/5 9:34
 * @Version 1.0
 */
@Data
public class OrderAmountDTO extends AbstractObject implements Serializable {


    private static final long serialVersionUID = 4025880044948870837L;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 收费类型
     */
    private Integer amountType;

    /**
     * 价格
     */
    private BigDecimal amount;
}