package com.taki.user.api.impl;

import com.taki.common.utli.ResponseData;
import com.taki.user.api.MembershipApi;
import com.taki.user.domain.dto.MemberFilterDTO;
import com.taki.user.domain.dto.MembershipDTO;
import com.taki.user.domain.dto.SaveOrUpdateMembershipDTO;
import com.taki.user.domain.request.MembershipRequest;
import com.taki.user.exception.UserBizException;
import com.taki.user.service.MembershipService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName MembershipApiImpl
 * @Description TODO
 * @Author Long
 * @Date 2022/10/3 20:35
 * @Version 1.0
 */
@DubboService(interfaceClass = MembershipApi.class,retries = 0)
@Slf4j
public class MembershipApiImpl implements MembershipApi {

    @Autowired
    private MembershipService membershipService;


    @Override
    public ResponseData<SaveOrUpdateMembershipDTO> saveOrUpdateMembership(SaveOrUpdateMembershipDTO saveOrUpdateMembershipDTO) {
        return null;
    }

    @Override
    public ResponseData<MembershipDTO> getMembership(MembershipRequest membershipRequest) {
        return null;
    }

    @Override
    public ResponseData<List<MembershipDTO>> listMembership() {

        try {
            List<MembershipDTO> memberships =  membershipService.listMembership();

            return ResponseData.success(memberships);


        }catch (UserBizException e){
            log.error("biz error:can not get all accounts",e);
            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());

        }
        catch (Exception e){
            log.error("system error",e);
            return  ResponseData.error(e.getMessage());

        }

    }

    @Override
    public ResponseData<Long> queryMaxUserId() {

        try {
            return  ResponseData.success(membershipService.queryMaxUserId());
        }catch (UserBizException e){
            log.error("biz error:can not get all accounts",e);
            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());
        }catch (Exception e){
            log.error("system error",e);
            return  ResponseData.error(e.getMessage());

        }


    }

    @Override
    public ResponseData<List<MembershipDTO>> queryMembershipByIdRange(Long startUserId, Long endUserId) {


        try {
            return  ResponseData.success(membershipService.queryMembershipByIdRange(startUserId,endUserId));
        }catch (UserBizException e){
            log.error("biz error:can not get all accounts",e);
            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());
        }catch (Exception e){
            log.error("system error",e);
            return  ResponseData.error(e.getMessage());

        }

    }

    @Override
    public ResponseData<List<MembershipDTO>> listMembershipByCondition(MemberFilterDTO memberFilterDTO) {

        try {
            return  ResponseData.success(membershipService.listMembershipByCondition(memberFilterDTO));
        }catch (UserBizException e){
            log.error("biz error:can not get all accounts",e);
            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());
        }catch (Exception e){
            log.error("system error",e);
            return  ResponseData.error(e.getMessage());

        }
    }
}
