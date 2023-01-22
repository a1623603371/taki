package com.taki.user.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import java.io.Serializable;
import java.math.BigDecimal;

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


    @TableId(value = "uid", type = IdType.AUTO)
    private Long uid;



    @TableField("account_type")
    private Integer accountType;


    @TableField("membership_level")
    private Integer membershipLevel;


    /**
     * 联系活跃天数
     */
    @TableField("active_count")
    private Integer activeCount;


    /**
     * 总活跃天数
     */
    @TableField("total_active_count")
    private Integer totalActiveCount;

    /**
     * 订单总金额
     */
    @TableField("tatali")
    private BigDecimal totalAmount;



    @TableField("uuid")
    private String uuid;


    @TableField("username")
    private String username;


    @TableField("avatar")
    private String avatar;


    @TableField("`account`")
    private String account;


    @TableField("`password`")
    private String password;

    @TableField("`status`")
    private Boolean status;


    @TableField("phone")
    private String phone;


    @TableField("email")
    private String email;


    @TableField("qq")
    private String qq;


    @TableField("wx")
    private String wx;


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
