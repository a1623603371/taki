package com.taki.address.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taki.address.domain.entity.StreetDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName StreetMapper
 * @Description 街道 mapper 组件
 * @Author Long
 * @Date 2022/7/31 16:32
 * @Version 1.0
 */
@Mapper
public interface StreetMapper extends BaseMapper<StreetDO> {
}
