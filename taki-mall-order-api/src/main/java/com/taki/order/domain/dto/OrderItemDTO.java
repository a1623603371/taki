package com.taki.order.domain.dto;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName OrderItemDTO
 * @Description 订单条目
 * @Author Long
 * @Date 2022/3/2 22:29
 * @Version 1.0
 */
@Data
public class OrderItemDTO  implements Serializable {
    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 订单条目ID
     *
     */
    private String orderItemId;

    /**
     * 商品类型
     */
    private Integer productType;

    /**
     * 商品Id
     */
    private String productId;

    /**
     * 商品图片
     */
    private String productImg;

    /**
     * 商品名称
     */
    private String productName;
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
     * 单前商品支付原总结
     */
    private BigDecimal originAmount;

    /**
     * 支付价格
     */
    private BigDecimal payAmount;

    /**
     * 商品单位
     */
    private String  productUnit;

    /**
     * 采购价格成本价
     */
    private Integer purchasePrice;

    /**
     * 卖家编号
     */
    private String sellerId;


}
