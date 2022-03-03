package com.taki.order.domain.dto;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName OrderAmountDetailDTO
 * @Description 订单费用详情
 * @Author Long
 * @Date 2022/3/2 22:38
 * @Version 1.0
 */
@Data
public class OrderAmountDetailDTO extends AbstractObject implements Serializable {


    private static final long serialVersionUID = 401682567873322852L;


    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 商品类型
     */
    private Integer productType;

    /**
     * sku code
     */
    private String skuCode;

    /**
     * 销量数量
     */
    private Integer saleQuantity;

    /**
     * 销量单价
     */
    private BigDecimal salePrice;

    /**
     * 收费类型
     */
    private Integer amountType;

    /**
     * 收费金额
     */
    private BigDecimal amount;

}
