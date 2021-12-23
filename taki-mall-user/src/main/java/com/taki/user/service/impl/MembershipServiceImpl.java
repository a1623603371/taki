package com.taki.user.service.impl;


import com.taki.common.constants.RedisCacheKey;
import com.taki.common.exception.ErrorCodeEnum;
import com.taki.common.exception.ServiceException;
import com.taki.common.utlis.IdGenerator;
import com.taki.user.dao.MembershipDAO;
import com.taki.user.domain.dto.MembershipDTO;
import com.taki.user.domain.entity.MembershipDO;
import com.taki.user.domain.request.ChangePasswordRequest;
import com.taki.user.domain.request.RegisterRequest;
import com.taki.user.enums.RegisterEnum;
import com.taki.user.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author long
 * @since 2021-12-02
 */
@Service
public class MembershipServiceImpl  implements MembershipService {


    private static final String CHECK = "(?![0-9]+\\$)(?![a-zA-Z]+\\$)[0-9A-Za-z]{6,18}";

    @Autowired
    private MembershipDAO membershipDAO;


    @Autowired
    private RedisCache redisCache;



    @Override
    public Boolean register(RegisterRequest request) {


        // 验证密码复杂度
        if (Pattern.matches(CHECK,request.getPassword())){
            throw new ServiceException(ErrorCodeEnum.PASS_NOT_SAFE,null,ErrorCodeEnum.PASS_NOT_SAFE.getErrorMsg());
        }
        // 验证 注册码
        checkCode(request.getAccount(),request.getCode());

        MembershipDTO.MembershipDTOBuilder membershipBuilder = MembershipDTO.builder();
        if(RegisterEnum.PHONE.getValue().equals(request.getMode())){
            membershipBuilder.phone(request.getAccount());
        }else if (RegisterEnum.EMAIL.getValue().equals(request.getMode())){
            membershipBuilder.email(request.getAccount());
        }
        membershipBuilder.account(IdGenerator.INSTANCE.nextId());
        membershipBuilder.password(request.getPassword());
        membershipBuilder.username(request.getUsername());
       MembershipDTO membership =   membershipBuilder.build();
       return membershipDAO.save(membership.clone(MembershipDO.class));


    }

    @Override
    public MembershipDTO getUserInfo(Long uid) {

        MembershipDTO membership = membershipDAO.getById(uid).clone(MembershipDTO.class);
        membership.setPassword("");

        membership.setPhone("");


        return membership;
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {




    }

    /** 
     * @description: 验证 注册验证码是否正确
     * @param account 注册账号
     * @param code 验证码
     * @return  void
     * @author Long
     * @date: 2021/12/20 10:04
     */ 
    private void checkCode(String account, String code) {

       String  cacheCode  =  redisCache.get(RedisCacheKey.REGISTER_CODE_KEY + account);

       if (StringUtils.isEmpty(cacheCode) || !cacheCode.equals(code)){
           throw new ServiceException(ErrorCodeEnum.BUSINESS_ERROR,null,"验证码不正确");
       }
    }


}
