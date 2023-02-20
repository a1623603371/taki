package com.taki.careerplan.cookbook.service;

import java.util.List;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author long
 * @since 2023-02-18
 */
public interface SkuInfoService {
    
    /*** 
     * @description:  根据 标签
     * @param tags
     * @return  java.util.List<java.lang.Long>
     * @author Long
     * @date: 2023/2/20 16:12
     */ 
    List<Long> getSkuIdsByTags(List<String> tags);
}
