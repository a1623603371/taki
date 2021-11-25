package com.taki.user.controller;

import com.taki.core.ResponseResult;
import com.taki.core.enums.CodeEnum;
import com.taki.core.error.ServiceException;
import com.taki.core.utlis.ResponseData;
import com.taki.user.service.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

 @Autowired
 private UserServiceImpl userService;

    @GetMapping("/test")
    @ApiOperation("测试方法")
    public ResponseData<String> test() throws ServiceException {


        return ResponseData.success( userService.test());
    }
}
