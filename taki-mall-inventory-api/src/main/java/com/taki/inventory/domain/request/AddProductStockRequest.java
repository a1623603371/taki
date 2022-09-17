package com.taki.inventory.domain.request;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName AddProductStockRequest
 * @Description 添加 商品库存请求
 * @Author Long
 * @Date 2022/5/11 15:48
 * @Version 1.0
 */
@Data
public class AddProductStockRequest implements Serializable {


    private static final long serialVersionUID = 857649650530332648L;


    /**
     * 商品 SKU
     */
    private String skuCode;

    /**
     *销售库存
     */
    private Long saleStockQuantity;
}
