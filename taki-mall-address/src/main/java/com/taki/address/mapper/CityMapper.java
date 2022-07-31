package com.taki.address.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taki.address.domain.entity.CityDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName CityMapper
 * @Description 城市 mapper
 * @Author Long
 * @Date 2022/7/31 16:30
 * @Version 1.0
 */
@Mapper
public interface CityMapper extends BaseMapper<CityDO> {
}
