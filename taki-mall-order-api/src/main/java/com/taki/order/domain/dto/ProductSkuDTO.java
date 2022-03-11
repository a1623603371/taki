package com.taki.order.domain.dto;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ProductSkuDTO
 * @Description 商品 SKU DTO
 * @Author Long
 * @Date 2022/3/10 17:33
 * @Version 1.0
 */
@Data
public class ProductSkuDTO extends AbstractObject implements Serializable {


    private static final long serialVersionUID = -6922677347730215262L;

    /**
     * 商品Id
     */
    private String productId;

    /**
     * 商品类型
     */
    private Integer productType;

    /**
     * sku code
     */
    private String skuCode;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品图片
     */
    private String productImg;

    /**
     * 商品单位
     */
    private  String productUnit;

    /**
     *商品销售价格
     */
    private Integer salePrice;

    /**
     *商品采购价格
     */
    private Integer purchasePrice;

}
