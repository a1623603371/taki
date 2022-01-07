package com.taki.product.domian.dto;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName CalculateOrderAmountDTO
 * @Description 营销计算费用详情
 * @Author Long
 * @Date 2022/1/5 9:34
 * @Version 1.0
 */
@Data
public class OrderAmountDetailDTO extends AbstractObject implements Serializable {


    private static final long serialVersionUID = 343559812774360119L;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 商品类型
     */
    private Integer productType;

    /**
     * sku编码
     */
    private String skuCode;


    /**
     * 销售 数量
     */
    private Integer saleQuantity;

    /**
     * 销售 价格
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