package com.taki.cookbook.mapper;

import com.taki.cookbook.domain.entity.CookbookDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 菜谱表 Mapper 接口
 * </p>
 *
 * @author long
 * @since 2023-02-18
 */
@Mapper
public interface CookbookMapper extends BaseMapper<CookbookDO> {

}
