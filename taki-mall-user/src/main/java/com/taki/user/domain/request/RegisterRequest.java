package com.taki.user.domain.request;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @ClassName RegisterRequest
 * @Description 注册请求
 * @Author Long
 * @Date 2021/12/17 0:13
 * @Version 1.0
 */
@Data
public class RegisterRequest {

    @ApiModelProperty("用户名称")
    @NotBlank(message = "用户昵称不能为空")
    private String username;


    @ApiModelProperty("手机号")
    @NotBlank(message = "用户昵称不能为空")
    private String account;


    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;


    @ApiModelProperty("验证码")
    @NotBlank(message = "验证码不能为空")
    private String code;

    @ApiModelProperty("注册方式")
    @NotBlank(message = "注册方式为空")
    private String mode;





}
