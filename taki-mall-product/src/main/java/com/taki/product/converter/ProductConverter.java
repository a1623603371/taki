package com.taki.product.converter;

import com.taki.product.domain.entity.ProductSkuDO;
import com.taki.product.domain.entity.vo.ProductSkuVO;
import com.taki.product.domian.dto.ProductSkuDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @ClassName ProductConverter
 * @Description TODO
 * @Author Long
 * @Date 2022/9/14 20:51
 * @Version 1.0
 */
@Mapper(componentModel = "spring")
public interface ProductConverter {

    /***
     * @description: 对象转换
     * @param productSkus
     * @return
     * @author Long
     * @date: 2022/9/14 20:53
     */
    List<ProductSkuDTO> converter(List<ProductSkuDO> productSkus);


    /***
     * @description: 对象转换
     * @param productSku 商品SKU
     * @return  com.taki.product.domian.dto.ProductSkuDTO
     * @author Long
     * @date: 2022/9/14 20:54
     */
    ProductSkuDTO converter(ProductSkuDO productSku);


    /***
     * @description: 对象转换
     * @param productSkuDTO
     * @return
     * @author Long
     * @date: 2022/9/14 20:55
     */
    ProductSkuVO converter(ProductSkuDTO productSkuDTO);
}
