package com.taki.product.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.dao.BaseDAO;
import com.taki.product.domain.entity.ProductSkuDO;
import com.taki.product.mapper.ProductSkuMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName ProductSkuDao
 * @Description 商品Sku dao 组件
 * @Author Long
 * @Date 2022/2/17 17:26
 * @Version 1.0
 */
@Repository
public class ProductSkuDao extends BaseDAO<ProductSkuMapper, ProductSkuDO> {

    /**
     * @description: 根据 商品SKU 编码 查询 商品SKU
     * @param skuCode 商品sku编码
     * @return  商品SKU
     * @author Long
     * @date: 2022/2/17 17:32
     */
    public  ProductSkuDO getProductSkuByCode(String skuCode){

        return this.getOne(new QueryWrapper<ProductSkuDO>().eq(ProductSkuDO.SKU_CODE,skuCode));

    }

    /**
     * @description: 批量查询商品
     * @param skuCodes 商品编码 集合
     * @return
     * @author Long
     * @date: 2022/6/8 15:37
     */
    public List<ProductSkuDO> listProductSkuByCode(List<String> skuCodes) {

        return this.list(new QueryWrapper<ProductSkuDO>().in(ProductSkuDO.SKU_CODE,skuCodes));
    }
}
