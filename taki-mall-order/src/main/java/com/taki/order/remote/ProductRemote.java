package com.taki.order.remote;

import com.taki.common.utli.ResponseData;
import com.taki.order.exception.OrderBizException;
import com.taki.product.api.ProductApi;
import com.taki.product.domian.dto.ProductSkuDTO;
import com.taki.product.domian.query.ListProductSkuQuery;
import com.taki.product.domian.query.ProductSkuQuery;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName ProductRemote
 * @Description 商品远程 api
 * @Author Long
 * @Date 2022/6/8 15:23
 * @Version 1.0
 */
@Component
public class ProductRemote {

    @DubboReference(version = "1.0.0",retries = 0)
    private ProductApi productApi;



    /** 
     * @description: 更加 商品编码 与  商家id 查询 商品
     * @param skuCode 商品 编码
     * @param sellerId 商家id
     * @return
     * @author Long
     * @date: 2022/6/8 15:25
     */ 
    public ProductSkuDTO getProductSku(String  skuCode,String sellerId){

        ProductSkuQuery query = new ProductSkuQuery();
        query.setSkuCode(skuCode);
        query.setSellerId(sellerId);


        ResponseData<ProductSkuDTO> result = productApi.getProductSku(query);

        if (!result.getSuccess()){
            throw new OrderBizException(result.getCode(),result.getMessage());
        }
        return result.getData();
    }


    /** 
     * @description:  批量查询商品信息
     * @param 
     * @return
     * @author Long
     * @date: 2022/6/8 15:28
     */ 
    public List<ProductSkuDTO> listProductSku(List<String>  skuCodeList,String sellerId){

        ListProductSkuQuery listProductSkuQuery = new ListProductSkuQuery();
        listProductSkuQuery.setSellerId(sellerId);
        listProductSkuQuery.setSkuCodes(skuCodeList);

        ResponseData<List<ProductSkuDTO>> result = productApi.listProductSku(listProductSkuQuery);

        if (!result.getSuccess()){
            throw new OrderBizException(result.getCode(),result.getMessage());
        }
        return result.getData();
    }

}
