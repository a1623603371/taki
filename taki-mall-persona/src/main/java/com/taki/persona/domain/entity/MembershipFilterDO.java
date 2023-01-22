package com.taki.persona.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 会员中心的会员轨迹表
 * </p>
 *
 * @author long
 * @since 2022-10-13
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("membership_filter")
public class MembershipFilterDO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableField("account_id")
    private Long accountId;

    /**
     * 账号类型
     */
    @TableField("account_type")
    private Integer accountType;

    /**
     * 会员等级
     */
    @TableField("membership_level")
    private Integer membershipLevel;

    /**
     * 连续活跃天数
     */
    @TableField("active_count")
    private Integer activeCount;

    /**
     * 一个月内活跃天数
     */
    @TableField("total_active_count")
    private Integer totalActiveCount;

    /**
     * 订单总金额，单位：分
     */
    @TableField("total_amount")
    private Integer totalAmount;

    /**
     * 创建人
     */
    @TableField("create_user")
    private Integer createUser;

    /**
     * 更新人
     */
    @TableField("update_user")
    private Integer updateUser;


    public static final String ACCOUNT_ID = "account_id";

    public static final String ACCOUNT_TYPE = "account_type";

    public static final String MEMBERSHIP_LEVEL = "membership_level";

    public static final String ACTIVE_COUNT = "active_count";

    public static final String TOTAL_ACTIVE_COUNT = "total_active_count";

    public static final String TOTAL_AMOUNT = "total_amount";

    public static final String CREATE_USER = "create_user";

    public static final String UPDATE_USER = "update_user";

}
