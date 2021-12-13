package com.taki.common.mybatis;


import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.core.toolkit.AES;

/**
 * @ClassName DBDataEncryption
 * @Description 加密 数据库 连接数据 和 密码
 *加密配置 mpw: 开头紧接加密内容（ 非数据库配置专用 YML 中其它配置也是可以使用的 ）
 * spring:
 *   datasource:
 *     url: mpw:qRhvCwF4GOqjessEB3G+a5okP+uXXr96wcucn2Pev6Bf1oEMZ1gVpPPhdDmjQqoM
 *     password: mpw:Hzy5iliJbwDHhjLs1L0j6w==
 *     username: mpw:Xb+EgsyuYRXw7U7sBJjBpA==
 *Jar 启动参数（ idea 设置 Program arguments,服务器可以设置为启动环境变量 ）
 *     --mpw.key=d1104d7c3b616f0b
 *
 * @Author Long
 * @Date 2021/12/3 22:59
 * @Version 1.0
 */
public class DBDataEncryption {

    public static void main(String[] args) {
        String randomKey = AES.generateRandomKey();


        String result = AES.encrypt("hello word", randomKey);

        System.out.println(result);
    }
}
