package com.taki.cookbook.service.impl;

import com.taki.cookbook.domain.entity.CookbookDO;
import com.taki.cookbook.mapper.CookbookMapper;
import com.taki.cookbook.service.CookbookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class CookbookServiceImpl extends ServiceImpl<CookbookMapper, CookbookDO> implements CookbookService {

}
