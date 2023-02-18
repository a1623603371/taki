package com.taki.careerplan.cookbook.service.impl;

import com.taki.careerplan.cookbook.dao.CookbookDAO;
import com.taki.careerplan.cookbook.service.CookbookService;
import com.taki.careerplan.domain.dto.SaveOrUpdateCookbookDTO;
import com.taki.careerplan.domain.request.SaveOrUpdateCookbookRequest;
import com.taki.common.constants.RedisLockKeyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜谱表 服务实现类
 * </p>
 *
 * @author long
 * @since 2023-02-18
 */
@Service
public class CookbookServiceImpl  implements CookbookService {


    @Autowired
    private CookbookDAO cookbookDAO;


    @Override
    public SaveOrUpdateCookbookDTO saveOrUpdateCookbook(SaveOrUpdateCookbookRequest request) {

        String cookbookUpdateLockKey = RedisLockKeyConstants.COOK_UPDATE_LOCK_PREFIX + request.getOperator();


        return null;
    }
}
