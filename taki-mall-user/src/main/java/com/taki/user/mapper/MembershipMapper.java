package com.taki.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taki.user.domain.entity.MembershipDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author long
 * @since 2021-12-02
 */
@Mapper
public interface MembershipMapper extends BaseMapper<MembershipDO> {

}
