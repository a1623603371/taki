package com.taki.inventory.domain.dto;

import com.taki.common.core.AbstractObject;
import com.taki.inventory.domain.entity.ProductStockDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName DeductStockDTO
 * @Description 扣减商品库存DTO
 * @Author Long
 * @Date 2022/5/11 21:23
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeductStockDTO extends AbstractObject {


    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 商品 skucode
     */
    private String skuCode;

    /**
     * 销售数量
     */
    private Long saleQuantity;

    /**
     * 库存数据
     */
    private ProductStockDO productStockDO;
}
