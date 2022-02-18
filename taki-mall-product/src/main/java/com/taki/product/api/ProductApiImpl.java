package com.taki.product.api;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.utlis.ParamCheckUtil;
import com.taki.common.utlis.ResponseData;
import com.taki.product.domian.dto.ProductSkuDTO;
import com.taki.product.domian.query.ProductSkuQuery;
import com.taki.product.exception.ProductBizException;
import com.taki.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

/**
 * @ClassName ProductApi
 * @Description 商品 API
 * @Author Long
 * @Date 2022/2/17 17:42
 * @Version 1.0
 */
@DubboService(version = "1.0.0",interfaceClass = ProductApi.class)
@Slf4j
public class ProductApiImpl implements ProductApi {


    @Autowired
    private ProductService productService;


    @Override
    public ResponseData<ProductSkuDTO> getProductSku(ProductSkuQuery productSkuQuery) {
        try {
            ParamCheckUtil.checkObjectNonNull(productSkuQuery);
            String skuCode = productSkuQuery.getSkuCode();
            ProductSkuDTO productSkuDTO = productService.getProductSkuByCode(skuCode);
            log.error("productStockDTO={},productQuery={}", JSONObject.toJSONString(productSkuDTO),JSONObject.toJSONString(productSkuQuery));
            return ResponseData.success(productSkuDTO);
        }catch (ProductBizException e){
            log.error("biz error",e);
            return ResponseData.error(e
            .getErrorCode(),e
            .getErrorMessage());


        }catch (Exception e){
            log.error("System error",e);
            return ResponseData.error(e.getMessage());
        }

    }
}
