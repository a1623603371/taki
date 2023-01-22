package com.taki.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taki.user.domain.entity.MembershipDO;
import com.taki.user.domain.entity.MembershipFilterDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName MembershipFilterMapper
 * @Description 会员
 * @Author Long
 * @Date 2022/10/3 21:23
 * @Version 1.0
 */
@Mapper
public interface MembershipFilterMapper extends BaseMapper<MembershipFilterDO> {



}
