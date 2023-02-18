package com.taki.cookbook.dao;

import com.taki.cookbook.domain.entity.CookbookDO;
import com.taki.cookbook.mapper.CookbookMapper;
import com.taki.cookbook.service.CookbookService;
import com.taki.common.dao.BaseDAO;
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
public class CookbookDAO extends BaseDAO<CookbookMapper, CookbookDO> implements CookbookService {

}
