package com.taki.careerplan.cookbook.controller;

import com.taki.careerplan.cookbook.service.CookbookUserService;
import com.taki.careerplan.domain.dto.CookbookUserDTO;
import com.taki.careerplan.domain.dto.SaveOrUpdateUserDTO;
import com.taki.careerplan.domain.request.CookbookUserQueryRequest;
import com.taki.careerplan.domain.request.SaveOrUpdateUserRequest;
import com.taki.common.utli.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName CookBookController
 * @Description TODO
 * @Author Long
 * @Date 2023/2/18 16:48
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/user")
public class CookbookUserController {

    @Autowired
    private CookbookUserService cookbookUserService;

    /*** 
     * @description:  保存 或修改菜谱作者
     * @param 
     * @return  com.taki.common.utli.ResponseData<java.lang.Boolean>
     * @author Long
     * @date: 2023/2/18 16:51
     */ 
    @PostMapping("/saveOrUpdateUser")
    public ResponseData<SaveOrUpdateUserDTO> saveOrUpdateUser(@RequestBody SaveOrUpdateUserRequest request){
        return ResponseData.success(cookbookUserService.saveOrUpdateUser(request));

    }

    /***
     * @description: 获取菜谱用户信息
     * @param request 获取 菜谱信息请求
     * @return  com.taki.common.utli.ResponseData<com.taki.careerplan.domain.dto.CookbookUserDTO>
     * @author Long
     * @date: 2023/2/18 21:25
     */
    @PostMapping("/getUserInfo")
    public ResponseData<CookbookUserDTO> getUserInfo(CookbookUserQueryRequest request){
        return ResponseData.success(cookbookUserService.getUserInfo(request));

    }

}
