package com.taki.user.controller;



import com.taki.common.utlis.ResponseData;
import com.taki.user.domain.dto.MembershipDTO;
import com.taki.user.domain.request.RegisterRequest;
import com.taki.user.domain.vo.MembershipVO;
import com.taki.user.service.MembershipService;
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
    public ResponseData<MembershipVO> register(@RequestBody RegisterRequest request){

         MembershipDTO membershipDTO =  membershipService.register(request);

        return ResponseData.success(membershipDTO.clone(MembershipVO.class));
    }





}
