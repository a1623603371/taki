package com.taki.tms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taki.tms.domain.entity.LogisticOrderDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName LogisticOrderMapper
 * @Description TODO
 * @Author Long
 * @Date 2022/5/17 15:05
 * @Version 1.0
 */
@Mapper
public interface LogisticOrderMapper extends BaseMapper<LogisticOrderDO> {
}
