package com.taki.inventory.domain.request;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ModifyProductStockRequest
 * @Description 调整商品SKU 库存请求
 * @Author Long
 * @Date 2022/5/13 18:35
 * @Version 1.0
 */
@Data
public class ModifyProductStockRequest extends AbstractObject implements Serializable {

    /**
     * 商品SKU 编码
     */
    private String skuCode;


    /**
     * 销售库存增量 （可正  可负）
     */
    private Long stockIncremental;
}
