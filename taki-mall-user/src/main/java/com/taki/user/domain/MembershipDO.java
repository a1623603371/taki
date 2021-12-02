package com.taki.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.time.LocalDateTime;
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
 * @since 2021-12-02
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("membership")
@ApiModel(value = "MembershipDO对象", description = "")
public class MembershipDO extends Model<MembershipDO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "uid", type = IdType.AUTO)
    private Long uid;

    @ApiModelProperty("用户名称")
    @TableField("username")
    private String username;

    @ApiModelProperty("用户账号")
    @TableField("`account`")
    private String account;

    @ApiModelProperty("密码")
    @TableField("`password`")
    private String password;

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

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    @TableField("update_time")
    private LocalDateTime updateTime;


    public static final String UID = "uid";

    public static final String USERNAME = "username";

    public static final String ACCOUNT = "account";

    public static final String PASSWORD = "password";

    public static final String PHONE = "phone";

    public static final String EMAIL = "email";

    public static final String QQ = "qq";

    public static final String WX = "wx";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    @Override
    public Serializable pkVal() {
        return this.uid;
    }

}
