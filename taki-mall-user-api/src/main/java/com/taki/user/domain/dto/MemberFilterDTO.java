package com.taki.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName MemberFilteer
 * @Description TODO
 * @Author Long
 * @Date 2022/10/3 13:28
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberFilterDTO implements Serializable {

    /**
     * 账号类型
     */
    private Integer accountType;

    /**
     * 会员等级
     */
    private Integer membershipLevel;

    /**
     * 联系活跃天数
     */
    private Integer activeCount;


    /**
     * 总活跃天数
     */
    private Integer totalActiveCount;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;
}
