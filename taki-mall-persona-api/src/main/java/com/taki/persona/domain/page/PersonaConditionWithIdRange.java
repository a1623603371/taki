package com.taki.persona.domain.page;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName PersonaFilterConditionDTO
 * @Description TODO
 * @Author Long
 * @Date 2022/10/12 19:51
 * @Version 1.0
 */
@Data
@Builder
public class PersonaConditionWithIdRange implements Serializable {


    private static final long serialVersionUID = 6289339753183009871L;


    /**
     * 会员等级
     */
    private Integer memberLevel;

    /**
     * 会员积分
     */
    private Long memberPoint;


    /**
     * 起始id
     */
    private Long offset;

    /**
     * 结束id
     */
    private Long limit;
}
