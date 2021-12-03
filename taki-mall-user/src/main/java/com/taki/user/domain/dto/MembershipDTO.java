package com.taki.user.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.taki.user.domain.MembershipDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author long
 * @since 2021-12-02
 */
@Getter
@Setter
@Accessors(chain = true)

public class MembershipDTO  {


    @ApiModelProperty("主键")
    private Long uid;

    @ApiModelProperty("用户名称")
    private String username;

    @ApiModelProperty("用户账号")
    private String account;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("QQ")
    private String qq;

    @ApiModelProperty("微信")
    private String wx;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;



}
