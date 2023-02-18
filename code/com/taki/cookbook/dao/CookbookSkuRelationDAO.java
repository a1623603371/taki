package com.taki.cookbook.dao;

import com.taki.cookbook.domain.entity.CookbookSkuRelationDO;
import com.taki.cookbook.mapper.CookbookSkuRelationMapper;
import com.taki.cookbook.service.CookbookSkuRelationService;
import com.taki.common.dao.BaseDAO;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜谱商品关联表 服务实现类
 * </p>
 *
 * @author long
 * @since 2023-02-18
 */
@Service
public class CookbookSkuRelationDAO extends BaseDAO<CookbookSkuRelationMapper, CookbookSkuRelationDO> implements CookbookSkuRelationService {

}
