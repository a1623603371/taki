package com.taki.user.mapper;

import com.taki.user.domain.UserDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author long
 * @since 2021-11-26
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

}
