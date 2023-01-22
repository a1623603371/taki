package com.taki.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taki.common.BaseDAO;
import com.taki.common.utli.ResponseData;
import com.taki.user.domain.dto.MemberFilterDTO;
import com.taki.user.domain.dto.MembershipDTO;
import com.taki.user.domain.entity.MembershipDO;
import com.taki.user.mapper.MembershipMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName MembershipDAO
 * @Description 用户管理模块DAO
 * @Author Long
 * @Date 2021/12/17 0:40
 * @Version 1.0
 */
@Repository
public class MembershipDAO extends BaseDAO<MembershipMapper, MembershipDO> {


    @Autowired
    private MembershipMapper membershipMapper;


    /**
     * @description: 修改 会员信息
     * @param membership 会员信息
     * @return  处理结果
     * @author Long
     * @date: 2021/12/23 20:32
     */
    Boolean updateMembership(MembershipDO membership){

       return this.update().set(StringUtils.isNotEmpty(membership.getAccount()),MembershipDO.ACCOUNT,membership.getAccount())
                     .set(StringUtils.isNotEmpty(membership.getAvatar()),MembershipDO.AVATAR,membership.getAvatar())
                     .set(StringUtils.isNotEmpty(membership.getEmail()),MembershipDO.EMAIL,membership.getEmail())
                    .set(StringUtils.isNotEmpty(membership.getUsername()),MembershipDO.USERNAME,membership.getUsername())
                    .set(StringUtils.isNotEmpty(membership.getPhone()),MembershipDO.PHONE,membership.getPhone())
                    .set(StringUtils.isNotEmpty(membership.getPassword()),MembershipDO.PASSWORD,membership.getPassword())
                    .set(ObjectUtils.isNotEmpty(membership.getStatus()),MembershipDO.STATUS,membership.getStatus()).update();



    };

    /***
     * @description:  查询用户信息
     * @param
     * @return  com.taki.user.domain.entity.MembershipDO
     * @author Long
     * @date: 2022/10/3 20:49
     */
    public List<MembershipDO> listMembership() {

        return this.list();
    }

    /***
     * @description: 查询最大用户Id
     * @param
     * @return
     * @author Long
     * @date: 2022/10/3 20:52
     */
    public Long queryMaxUserId() {

        return  membershipMapper.queryMaxUserId();
    }

    /***
     * @description: 根据id范围查询用户
     * @param startUserId
     * @param endUserId
     * @return  java.util.List<com.taki.user.domain.dto.MembershipDTO>
     * @author Long
     * @date: 2022/10/3 20:59
     */
    public List<MembershipDO> queryMembershipByIdRange(Long startUserId, Long endUserId) {

        return  membershipMapper.queryMembershipByIdRange(startUserId,endUserId);
    }


}
