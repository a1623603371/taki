package com.taki.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taki.user.domain.dto.MembershipDTO;
import com.taki.user.domain.entity.MembershipDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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


    @Select("SELECT  MAX(id) FROM membership")
    Long queryMaxUserId();


    @Select("SELECT  id FROM  membership WHERE   id >= #{startUserId} AND  id <= #{endUserId}")
    List<MembershipDO> queryMembershipByIdRange(@Param("startUserId") Long startUserId, @Param("endUserId") Long endUserId);
}
