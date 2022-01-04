package com.taki.product.domian.query;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ProductSkuQuery
 * @Description 商品 SKU 查询条件
 * @Author Long
 * @Date 2022/1/4 11:52
 * @Version 1.0
 */
@Data
public class ProductSkuQuery  extends AbstractObject implements Serializable {


    private static final long serialVersionUID = -6668854304324674885L;

    /**
     * 卖家Id
     */
    private String sellerId;
    /**
     * sku编号
     */
    private String skuCode;
}
