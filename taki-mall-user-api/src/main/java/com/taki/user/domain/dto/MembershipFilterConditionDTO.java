package com.taki.user.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName MembershipFilterConditionDTO
 * @Description TODO
 * @Author Long
 * @Date 2022/10/3 13:27
 * @Version 1.0
 */
@Data
@Builder
public class MembershipFilterConditionDTO implements Serializable {


    private Integer memberLevel;

    private Long memberPoint;
}
