package com.taki.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 *  用户实体类
 * </p>
 *
 * @author long
 * @since 2021-11-26
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("user")
@ApiModel(value = "UserDO对象", description = "")
public class UserDO extends Model<UserDO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "uid", type = IdType.AUTO)
    private Long uid;

    @ApiModelProperty("用户账号")
    @TableField("`account`")
    private String account;

    @ApiModelProperty("手机号")
    @TableField("phone")
    private String phone;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty("修改时间")
    @TableField("update_time")
    private Date updateTime;


    public static final String UID = "uid";

    public static final String ACCOUNT = "account";

    public static final String PHONE = "phone";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    @Override
    public Serializable pkVal() {
        return this.uid;
    }

}
