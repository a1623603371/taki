package com.taki.product.service.impl;

import com.taki.common.utlis.ParamCheckUtil;
import com.taki.product.dao.ProductSkuDao;
import com.taki.product.domain.entity.ProductSkuDO;
import com.taki.product.domian.dto.ProductSkuDTO;
import com.taki.product.exception.ProductErrorCodeEnum;
import com.taki.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * @ClassName ProductServiceImpl
 * @Description 商品 service组件
 * @Author Long
 * @Date 2022/2/17 17:36
 * @Version 1.0
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductSkuDao productSkuDao;


    @Override
    public ProductSkuDTO getProductSkuByCode(String skuCode) {

        ParamCheckUtil.checkStringNonEmpty(skuCode, ProductErrorCodeEnum.SKU_CODE_IS_NULL);

        ProductSkuDO productSkuDO = productSkuDao.getProductSkuByCode(skuCode);

        if (ObjectUtils.isEmpty(productSkuDO)) {
            return null;
        }
        return productSkuDO.clone(ProductSkuDTO.class);
    }
}
