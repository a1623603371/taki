package com.taki.user.controller;



import com.taki.common.utlis.ResponseData;
import com.taki.user.domain.dto.MembershipDTO;
import com.taki.user.domain.request.RegisterRequest;
import com.taki.user.domain.vo.MembershipVO;
import com.taki.user.service.MembershipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  会员前端控制器
 * </p>
 *
 * @author long
 * @since 2021-12-02
 */
@RestController
@RequestMapping("/membership")
@Api(tags = "会员相关")
public class MembershipController {



    @Autowired
    private MembershipService membershipService;




    /**
     * @description: 用户注册
     * @param: membership
     * @return:
     * @author Long
     * @date: 2021/12/4 12:51
     */
    @PostMapping("/register")
    @ApiOperation("用户注册")
    public ResponseData<Boolean> register(@RequestBody RegisterRequest request){

        return ResponseData.success(membershipService.register(request));
    }


    /**
     * @description: 获取用户信息
     * @param uid
     * @return 用户信息
     * @author Long
     * @date: 2021/12/20 13:56
     */
    public ResponseData<MembershipVO> getUserInfo(Long uid){

        return ResponseData.success(membershipService.getUserInfo(uid).clone(MembershipVO.class));
    }


}
