package com.taki.user.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author long
 * @since 2021-12-16
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("membership")
@ApiModel(value = "MembershipDO对象", description = "")
public class MembershipDO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "uid", type = IdType.AUTO)
    private Long uid;

    @ApiModelProperty("第三方平台用户唯一ID")
    @TableField("uuid")
    private String uuid;

    @ApiModelProperty("用户名称")
    @TableField("username")
    private String username;

    @ApiModelProperty("用户头像")
    @TableField("avatar")
    private String avatar;

    @ApiModelProperty("用户账号")
    @TableField("`account`")
    private String account;

    @ApiModelProperty("密码")
    @TableField("`password`")
    private String password;

    @ApiModelProperty("状态 0 正常 1 封禁")
    @TableField("`status`")
    private Boolean status;

    @ApiModelProperty("手机号")
    @TableField("phone")
    private String phone;

    @ApiModelProperty("邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty("QQ")
    @TableField("qq")
    private String qq;

    @ApiModelProperty("微信")
    @TableField("wx")
    private String wx;

    @ApiModelProperty("用户来源")
    @TableField("`source`")
    private String source;


    public static final String UID = "uid";

    public static final String UUID = "uuid";

    public static final String USERNAME = "username";

    public static final String AVATAR = "avatar";

    public static final String ACCOUNT = "account";

    public static final String PASSWORD = "password";

    public static final String STATUS = "status";

    public static final String PHONE = "phone";

    public static final String EMAIL = "email";

    public static final String QQ = "qq";

    public static final String WX = "wx";

    public static final String SOURCE = "source";

}
