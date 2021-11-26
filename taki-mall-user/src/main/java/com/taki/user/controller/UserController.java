package com.taki.user.controller;


import com.taki.core.utlis.ResponseData;
import com.taki.user.domain.UserDO;
import com.taki.user.service.UserService;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName UserController
 * @Description 用户模块 Controller 组件
 * @Author Long
 * @Date 2021/11/25 11:07
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户模块API")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation("保存用户")
    @PostMapping("/save")
    public ResponseData save(@RequestBody UserDO user){
        userService.save(user);
        return ResponseData.success("ok");
    }


    @ApiOperation("获取用户集合")
    @GetMapping("/")
    public ResponseData<List<UserDO>> getUsers(){
        return ResponseData.success(userService.list());
    }
}
