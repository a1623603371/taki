package com.taki.persona.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taki.persona.domain.dto.PersonaFilterConditionDTO;
import com.taki.persona.domain.entity.MembershipFilterDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @ClassName MembershipFilterMapper
 * @Description 会员中心的会员轨迹 mapper 组件
 * @Author Long
 * @Date 2022/10/13 20:53
 * @Version 1.0
 */
@Mapper
public interface MembershipFilterMapper extends BaseMapper<MembershipFilterDO> {




    @Select("<script>" +
            "SELECT  MAX(account_id)" +
            "" +
            "" +
            "" +
            "" +
            "" +
            "" +
            "" +
            "" +
            "</script>")
    Long  queryMaxUserId(PersonaFilterConditionDTO personaFilterConditionDTO);
}
