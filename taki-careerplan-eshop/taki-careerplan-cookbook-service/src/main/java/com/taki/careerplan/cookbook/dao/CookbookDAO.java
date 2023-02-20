package com.taki.careerplan.cookbook.dao;

import com.taki.careerplan.cookbook.domain.entity.CookbookDO;
import com.taki.careerplan.cookbook.mapper.CookbookMapper;
import com.taki.careerplan.domain.dto.CookbookDTO;
import com.taki.common.dao.BaseDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 菜谱表 服务实现类
 * </p>
 *
 * @author long
 * @since 2023-02-18
 */
@Repository
public class CookbookDAO extends BaseDAO<CookbookMapper, CookbookDO>  {


    @Autowired
    private CookbookMapper cookbookMapper;

    public CookbookDTO getCookbookInfoById(Long cookbookId) {

        return cookbookMapper.getCookbookInfoById(cookbookId);
    }
}
