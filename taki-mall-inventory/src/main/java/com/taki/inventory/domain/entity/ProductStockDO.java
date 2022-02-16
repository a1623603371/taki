package com.taki.inventory.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ProductStockDO
 * @Description 商品库存
 * @Author Long
 * @Date 2022/2/16 16:39
 * @Version 1.0
 */
@Data
@TableName("")
public class ProductStockDO extends BaseEntity  implements Serializable {


    private static final long serialVersionUID = -9127215191348356692L;


    /**
     * 商品SKU 编码
     */
    public Long skuCode;

    /**
     * 销售库存
     */
    public Long saleStockQuantity;


    /**
     * 锁定库存
     */
    public Long lockedStockQuantity;
}
