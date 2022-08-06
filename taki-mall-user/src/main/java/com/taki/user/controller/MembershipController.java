package com.taki.user.controller;



import com.taki.common.utli.ResponseData;

import com.taki.user.domain.request.ChangePasswordRequest;
import com.taki.user.domain.request.RegisterRequest;
import com.taki.user.domain.vo.MembershipVO;
import com.taki.user.service.MembershipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * @param uid 用户Id
     * @return 用户信息
     * @author Long
     * @date: 2021/12/20 13:56
     */
    @GetMapping("/userInfo/{id}")
    @ApiOperation("用户信息")
    public ResponseData<MembershipVO> getUserInfo(@PathVariable("id") Long uid){

        return ResponseData.success(membershipService.getUserInfo(uid).clone(MembershipVO.class));
    }


    /** 
     * @description: 修改密码
     * @param 
     * @return 处理结果
     * @author Long
     * @date: 2021/12/23 20:17
     */
    @PostMapping("/changePassword")
    public ResponseData<Boolean> changePassword( @RequestBody ChangePasswordRequest request){


        membershipService.changePassword(request);

        return ResponseData.success();
    }
}
