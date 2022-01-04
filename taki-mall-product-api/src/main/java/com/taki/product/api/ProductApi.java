package com.taki.product.api;

import com.taki.common.utlis.ResponseData;
import com.taki.product.domian.dto.ProductSkuDTO;
import com.taki.product.domian.query.ProductSkuQuery;

/**
 * @ClassName ProductApi
 * @Description 商品服务 RPC API
 * @Author Long
 * @Date 2022/1/4 11:41
 * @Version 1.0
 */
public interface ProductApi {

    ResponseData<ProductSkuDTO> getProductSku(ProductSkuQuery productSkuQuery);
}
