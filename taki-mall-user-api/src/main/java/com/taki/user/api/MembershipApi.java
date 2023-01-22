package com.taki.user.api;

import com.taki.common.utli.ResponseData;
import com.taki.user.domain.dto.MemberFilterDTO;
import com.taki.user.domain.dto.MembershipDTO;
import com.taki.user.domain.dto.SaveOrUpdateMembershipDTO;
import com.taki.user.domain.request.MembershipRequest;

import java.util.List;

/**
 * @ClassName MemberShipService
 * @Description 用户会员接口api接口
 * @Author Long
 * @Date 2022/10/3 13:25
 * @Version 1.0
 */
public interface MembershipApi {


    /*** 
     * @description:  新增/修改会员接口
     * @param saveOrUpdateMembershipDTO
     * @return  com.taki.common.utli.ResponseData<com.taki.user.domain.dto.SaveOrUpdateMembershipDTO>
     * @author Long
     * @date: 2022/10/3 13:27
     */ 
    ResponseData<SaveOrUpdateMembershipDTO> saveOrUpdateMembership(SaveOrUpdateMembershipDTO saveOrUpdateMembershipDTO);



    /***
     * @description:  查询用户会员
     * @param membershipRequest 会员请求参数
     * @return
     * @author Long
     * @date: 2022/10/3 20:23
     */
    ResponseData<MembershipDTO> getMembership(MembershipRequest membershipRequest);


    /*** 
     * @description:  查询全部账号接口
     * @param 
     * @return
     * @author Long
     * @date: 2022/10/3 20:24
     */ 
    ResponseData<List<MembershipDTO>> listMembership();

    /*** 
     * @description: 查询最大的用户Id
     * @param 
     * @return
     * @author Long
     * @date: 2022/10/3 20:25
     */ 
    ResponseData<Long> queryMaxUserId();


    /***
     * @description: 根据用户id范围查询用户
     * @param startUserId 开始用户idd
     * @param  endUserId  结束用户id
     * @return
     * @author Long
     * @date: 2022/10/3 20:28
     */
    ResponseData<List<MembershipDTO>> queryMembershipByIdRange(Long startUserId,Long  endUserId);


    /*** 
     * @description: 查询符合条件的账号接口
     * @param memberFilterDTO
     * @return
     * @author Long
     * @date: 2022/10/3 20:30
     */ 
    ResponseData<List<MembershipDTO>> listMembershipByCondition(MemberFilterDTO memberFilterDTO);
 }
