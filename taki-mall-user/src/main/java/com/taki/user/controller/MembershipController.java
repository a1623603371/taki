package com.taki.user.controller;


import com.taki.core.utlis.ResponseData;
import com.taki.user.domain.vo.MembershipVO;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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




    public ResponseData<MembershipVO> register(@RequestBody MembershipVO membership){


        return null;
    }


}
