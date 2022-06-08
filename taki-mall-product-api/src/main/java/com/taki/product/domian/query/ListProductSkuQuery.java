package com.taki.product.domian.query;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName ListProductSkuQuery
 * @Description
 * @Author Long
 * @Date 2022/6/8 15:29
 * @Version 1.0
 */
@Data
public class ListProductSkuQuery implements Serializable {


    /**
     * 卖家Id
     */
    private String sellerId;
    /**
     * sku编号
     */
    private List<String> skuCodes;
}
