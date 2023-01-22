package com.taki.persona.domain.page;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName PersonaFilterConditionDTO
 * @Description TODO
 * @Author Long
 * @Date 2022/10/12 19:51
 * @Version 1.0
 */
@Data
@Builder
public class PersonaConditionPage implements Serializable {


    private static final long serialVersionUID = 6289339753183009871L;
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
     * 活跃天数
     */
    private Integer activeCount;

    /**
     * 一个月活跃天数
     */
    private Integer totalActiveCount;

    /**
     * 订单总金额：单位 元
     */
    private BigDecimal totalAmount;


    /**
     * 起始id
     */
    private Long offset;

    /**
     * 结束id
     */
    private Long limit;
}
