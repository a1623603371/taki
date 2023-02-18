package com.taki.careerplan.cookbook.mapper;

import com.taki.careerplan.cookbook.domain.entity.CookbookSkuRelationDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 菜谱商品关联表 Mapper 接口
 * </p>
 *
 * @author long
 * @since 2023-02-18
 */
@Mapper
public interface CookbookSkuRelationMapper extends BaseMapper<CookbookSkuRelationDO> {

}
