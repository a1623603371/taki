package com.taki.address.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taki.address.domain.entity.AreaDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName AreaMapper
 * @Description 区域 mapper 组件
 * @Author Long
 * @Date 2022/7/31 16:29
 * @Version 1.0
 */
@Mapper
public interface AreaMapper extends BaseMapper<AreaDO> {
}
