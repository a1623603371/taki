package com.taki.user.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName MembershipFilterDO
 * @Description TODO
 * @Author Long
 * @Date 2022/10/3 21:15
 * @Version 1.0
 */
@Data
@TableName("membership_filter")
public class MembershipFilterDO {


    @TableId(value = "uid", type = IdType.AUTO)
    private Long id;


    @TableField("account_id")
    private Long accountId;

    @TableField("account_type")
    private Integer accountType;

    @TableField("membership_level")
    private Integer membershipLevel;

    @TableField("active_count")
    private Integer activeCount;

    @TableField("total_active_count")
    private Integer totalActiveCount;


    @TableField("total_amount")
    private BigDecimal totalAmount;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
