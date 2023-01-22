package com.taki.push.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName PersonaFilterConditionDTO
 * @Description TODO
 * @Author Long
 * @Date 2022/10/6 15:17
 * @Version 1.0
 */
@Data
public class PersonaFilterConditionDTO implements Serializable {


    private static final long serialVersionUID = -335319110574715880L;


    /**
     * 账号类型
     */
    private Integer accountType;

    /**
     * 会员等级
     */
    private Integer memberLevel;

    /**
     * 会员积分
     */
    private Long memberPoint;


    /**
     *连续活跃天数
     */
    private Integer activeCount;

    /**
     * 一个月活跃天数
     */
    private Integer totalActiveCount;

    /**
     * 订单总金额，元
     * */
    private BigDecimal totalAmount;

}
