package com.taki.careerplan.cookbook.service.impl;

import com.taki.careerplan.cookbook.dao.CookbookSkuRelationDAO;
import com.taki.careerplan.cookbook.domain.entity.CookbookSkuRelationDO;
import com.taki.careerplan.cookbook.service.CookbookSkuRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 菜谱商品关联表 服务实现类
 * </p>
 *
 * @author long
 * @since 2023-02-18
 */
@Service
public class CookbookSkuRelationServiceImpl  implements CookbookSkuRelationService {

    @Autowired
    private CookbookSkuRelationDAO cookbookSkuRelationDAO;


    @Override
    public Boolean saveBatch(List<CookbookSkuRelationDO> cookbookSkuRelationList) {
        return cookbookSkuRelationDAO.saveBatch(cookbookSkuRelationList);
    }
}
