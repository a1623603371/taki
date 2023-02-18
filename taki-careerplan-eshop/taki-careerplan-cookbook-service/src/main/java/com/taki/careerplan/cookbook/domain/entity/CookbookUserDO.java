package com.taki.careerplan.cookbook.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 菜谱作者表
 * </p>
 *
 * @author long
 * @since 2023-02-18
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("cookbook_user")
public class CookbookUserDO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 作者名称
     */
    @TableField("user_name")
    private String userName;

    /**
     * 头像
     */
    @TableField("`profile`")
    private String profile;

    /**
     * 个⼈签名
     */
    @TableField("personal")
    private String personal;

    /**
     * ⽣⽇
     */
    @TableField("birthday")
    private String birthday;

    /**
     * 性别
     */
    @TableField("sex")
    private Integer sex;

    /**
     * 创建⼈
     */
    @TableField("create_user")
    private Integer createUser;

    /**
     * 修改⼈
     */
    @TableField("update_user")
    private Integer updateUser;


    public static final String USER_NAME = "user_name";

    public static final String PROFILE = "profile";

    public static final String PERSONAL = "personal";

    public static final String BIRTHDAY = "birthday";

    public static final String SEX = "sex";

    public static final String CREATE_USER = "create_user";

    public static final String UPDATE_USER = "update_user";

}
