package com.taki.user.dao;

import com.taki.common.BaseDAO;
import com.taki.user.domain.dto.MemberFilterDTO;
import com.taki.user.domain.entity.MembershipDO;
import com.taki.user.domain.entity.MembershipFilterDO;
import com.taki.user.mapper.MembershipFilterMapper;
import com.taki.user.mapper.MembershipMapper;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

/**
 * @ClassName MembershipFilterDAO
 * @Description TODO
 * @Author Long
 * @Date 2022/10/3 21:22
 * @Version 1.0
 */
public class MembershipFilterDAO extends BaseDAO<MembershipFilterMapper, MembershipFilterDO> {



    /***
     * @description: 根据条件查询用户
     * @param memberFilterDTO 条件筛选条件
     * @return  java.util.List<com.taki.user.domain.dto.MembershipDTO>
     * @author Long
     * @date: 2022/10/3 21:06
     */
    public List<MembershipFilterDO> listMembershipByCondition(MemberFilterDTO memberFilterDTO) {

        return this.lambdaQuery()
                .eq(ObjectUtils.isNotEmpty(memberFilterDTO.getAccountType()),MembershipFilterDO::getAccountType,memberFilterDTO.getAccountType())
                .ge(ObjectUtils.isNotEmpty(memberFilterDTO.getMembershipLevel()),MembershipFilterDO::getMembershipLevel,memberFilterDTO.getMembershipLevel())
                .ge(ObjectUtils.isNotEmpty(memberFilterDTO.getActiveCount()),MembershipFilterDO::getActiveCount,memberFilterDTO.getActiveCount())
                .ge(ObjectUtils.isNotEmpty(memberFilterDTO.getMembershipLevel()),MembershipFilterDO::getMembershipLevel,memberFilterDTO.getMembershipLevel())
                .ge(ObjectUtils.isNotEmpty(memberFilterDTO.getTotalAmount()),MembershipFilterDO::getTotalAmount,memberFilterDTO.getTotalAmount())
                .ge(ObjectUtils.isNotEmpty(memberFilterDTO.getTotalActiveCount()),MembershipFilterDO::getTotalActiveCount,memberFilterDTO.getTotalActiveCount()).list();
    }
}
