package com.taki.order.domian.dto;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName OrderAmountDTO
 * @Description 订单费用
 * @Author Long
 * @Date 2022/1/7 11:19
 * @Version 1.0
 */
@Data
public class OrderAmountDTO extends AbstractObject implements Serializable {


    private static final long serialVersionUID = -8344623524209815890L;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 费用类型
     */
    private Integer amountType;

    /**
     * 费用
     */
    private BigDecimal amount;

}
