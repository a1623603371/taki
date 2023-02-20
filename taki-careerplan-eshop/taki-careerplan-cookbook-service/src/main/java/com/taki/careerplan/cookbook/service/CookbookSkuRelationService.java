package com.taki.careerplan.cookbook.service;

import com.taki.careerplan.cookbook.domain.entity.CookbookSkuRelationDO;

import java.util.List;

/**
 * <p>
 * 菜谱商品关联表 服务类
 * </p>
 *
 * @author long
 * @since 2023-02-18
 */
public interface CookbookSkuRelationService {

    Boolean saveBatch(List<CookbookSkuRelationDO> cookbookSkuRelationList);
}
