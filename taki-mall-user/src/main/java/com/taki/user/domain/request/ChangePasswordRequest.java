package com.taki.user.domain.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName ChangePasswordRequest
 * @Description 修改密码
 * @Author Long
 * @Date 2021/12/23 20:18
 * @Version 1.0
 */
@Data
public class ChangePasswordRequest {

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("旧密码")
    private String oldPassword;

    @ApiModelProperty("新密码")
    private String confirmPassword;

    @ApiModelProperty("验证码")
    private String code;



}
