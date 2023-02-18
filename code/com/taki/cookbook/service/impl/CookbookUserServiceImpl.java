package com.taki.cookbook.service.impl;

import com.taki.cookbook.domain.entity.CookbookUserDO;
import com.taki.cookbook.mapper.CookbookUserMapper;
import com.taki.cookbook.service.CookbookUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜谱作者表 服务实现类
 * </p>
 *
 * @author long
 * @since 2023-02-18
 */
@Service
public class CookbookUserServiceImpl extends ServiceImpl<CookbookUserMapper, CookbookUserDO> implements CookbookUserService {

}
