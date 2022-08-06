package com.taki.product.api;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.utli.ParamCheckUtil;
import com.taki.common.utli.ResponseData;
import com.taki.product.domian.dto.ProductSkuDTO;
import com.taki.product.domian.query.ListProductSkuQuery;
import com.taki.product.domian.query.ProductSkuQuery;
import com.taki.product.exception.ProductBizException;
import com.taki.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
            log.info("ProductSkuDTO={},productSkuQuery={}", JSONObject.toJSONString(productSkuDTO),JSONObject.toJSONString(productSkuQuery));
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

    @Override
    public ResponseData<List<ProductSkuDTO>> listProductSku(ListProductSkuQuery listProductSkuQuery) {
        try {
            ParamCheckUtil.checkObjectNonNull(listProductSkuQuery);
           List<String> skuCodes = listProductSkuQuery.getSkuCodes();
           List<ProductSkuDTO>  productSkus = productService.listProductSkuByCode(skuCodes);
            log.info("productSkuDTO={},listProductSkuQuery={}", JSONObject.toJSONString(productSkus),JSONObject.toJSONString(listProductSkuQuery));

            return ResponseData.success(productSkus);

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
