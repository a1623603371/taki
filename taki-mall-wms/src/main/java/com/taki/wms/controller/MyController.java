package com.taki.wms.controller;

import com.taki.wms.annotation.LoginRequired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName MyController
 * @Description TODO
 * @Author Long
 * @Date 2022/8/14 18:03
 * @Version 1.0
 */
@RestController
@RequestMapping("/my")
public class MyController {

    @LoginRequired
    @GetMapping("/test")
    public String myTest(){
        System.out.println("test");
        return "test";
    }
}
