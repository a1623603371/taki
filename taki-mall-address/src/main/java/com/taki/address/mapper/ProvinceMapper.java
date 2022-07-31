package com.taki.address.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taki.address.domain.entity.ProvinceDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName ProvinceMapper
 * @Description 省 mapper 组件
 * @Author Long
 * @Date 2022/7/31 16:30
 * @Version 1.0
 */
@Mapper
public interface ProvinceMapper extends BaseMapper<ProvinceDO> {
}
