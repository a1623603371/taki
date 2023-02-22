package com.taki.careerplan.cookbook.mapper;

import com.taki.careerplan.cookbook.dao.CookbookDAO;
import com.taki.careerplan.cookbook.domain.entity.CookbookDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taki.careerplan.domain.dto.CookbookDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

    CookbookDTO getCookbookInfoById(Long cookbookId);

    List<CookbookDTO> pageByUserId(Long userId, Long page, Long pageSize);
}
