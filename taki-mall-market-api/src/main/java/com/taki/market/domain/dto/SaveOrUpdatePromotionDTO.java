package com.taki.market.domain.dto;

import lombok.Data;

/**
 * @ClassName SaveOrUpdatePromotionDTO
 * @Description 创建/修改活动返回结果
 * @Author Long
 * @Date 2022/9/25 21:06
 * @Version 1.0
 */
@Data
public class SaveOrUpdatePromotionDTO {
    /**
     * 新增/修改结果
     */
    private Boolean success;

    /**
     * 活动类型
     */
    private Integer type;

    /**
     * 活动 名称
     */
    private String name;

    /**
     * 活动结果
     */
    private String rule;

    /**
     * 活动创建、修改人
     */
    private Integer createUser;
}
