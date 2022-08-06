package com.taki.product.api;

import com.taki.common.utli.ResponseData;
import com.taki.product.domian.dto.ProductSkuDTO;
import com.taki.product.domian.query.ListProductSkuQuery;
import com.taki.product.domian.query.ProductSkuQuery;

import java.util.List;

/**
 * @ClassName ProductApi
 * @Description 商品服务 RPC API
 * @Author Long
 * @Date 2022/1/4 11:41
 * @Version 1.0
 */
public interface ProductApi {

    /**
     * @description: 查询商品
     * @param productSkuQuery 查询 商品参数
     * @return
     * @author Long
     * @date: 2022/6/8 15:31
     */
    ResponseData<ProductSkuDTO> getProductSku(ProductSkuQuery productSkuQuery);



    /** 
     * @description:  批量 查询商品sku 信息
     * @param listProductSkuQuery
     * @return
     * @author Long
     * @date: 2022/6/8 15:31
     */ 
    ResponseData<List<ProductSkuDTO>> listProductSku(ListProductSkuQuery listProductSkuQuery);
}
