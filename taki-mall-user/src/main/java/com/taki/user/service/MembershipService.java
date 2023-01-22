package com.taki.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.taki.common.utli.ResponseData;
import com.taki.user.domain.dto.MemberFilterDTO;
import com.taki.user.domain.dto.MembershipDTO;
import com.taki.user.domain.entity.MembershipDO;
import com.taki.user.domain.request.ChangePasswordRequest;
import com.taki.user.domain.request.RegisterRequest;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author long
 * @since 2021-12-02
 */
public interface MembershipService  {

    /**
     * @description: 注册会员
     * @param request 注册请求实体
     * @return  Boolean
     * @author Long
     * @date: 2021/12/17 17:13
     */
    Boolean register(RegisterRequest request);

    /**
     * @description: 获取用户信息
     * @param uid 用户id
     * @return  用户信息
     * @author Long
     * @date: 2021/12/21 17:00
     */
    MembershipDTO getUserInfo(Long uid);

    /**
     * @description: 修改密码
     * @param request 修改密码请求数据
     * @return  void
     * @author Long
     * @date: 2021/12/23 20:26
     */
    void changePassword(ChangePasswordRequest request);
    
    /*** 
     * @description: 查询所有用户信息
     * @param 
     * @return
     * @author Long
     * @date: 2022/10/3 20:44
     */ 
    List<MembershipDTO> listMembership();

    /***
     * @description: 查询最大用户Id
     * @param
     * @return
     * @author Long
     * @date: 2022/10/3 20:52
     */
    Long queryMaxUserId();
    
    /*** 
     * @description: 根据id范围查询用户
     * @param startUserId
     * @param endUserId
     * @return  java.util.List<com.taki.user.domain.dto.MembershipDTO>
     * @author Long
     * @date: 2022/10/3 20:59
     */ 
    List<MembershipDTO> queryMembershipByIdRange(Long startUserId, Long endUserId);

    /*** 
     * @description: 根据条件查询用户
     * @param memberFilterDTO
     * @return
     * @author Long
     * @date: 2022/10/3 21:04
     */ 
    List<MembershipDTO> listMembershipByCondition(MemberFilterDTO memberFilterDTO);
}
