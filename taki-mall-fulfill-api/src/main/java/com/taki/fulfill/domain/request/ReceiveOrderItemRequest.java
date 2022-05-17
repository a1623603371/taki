package com.taki.fulfill.domain.request;

import com.taki.common.core.AbstractObject;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName ReceiveOrderItemRequest
 * @Description 履约订单商品明细请求
 * @Author Long
 * @Date 2022/3/4 10:55
 * @Version 1.0
 */
@Data
@Builder
public class ReceiveOrderItemRequest extends AbstractObject implements Serializable {


    private static final long serialVersionUID = -3155004658227291612L;
    /**
     * 商品id
     */
    private String skuCode;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 销售单价
     */
    private BigDecimal salePrice;

    /**
     * 销售数量
     */
    private Integer saleQuantity;

    /**
     * 商品单位
     */
    private String productUnit;

    /**
     * 付款金额
     */
    private BigDecimal payAmount;

    /**
     * 当前商品支付原总价
     */
    private BigDecimal originAmount;

    @Tolerate
    public ReceiveOrderItemRequest() {

    }
}
