package com.taki.careerplan.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Long
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepDetail {

    /**
     * 步骤，表示第几步
     */
    private Integer step;

    /**
     * 步骤内容
     */
    private String content;

    /**
     * 步骤图片
     */
    private String img;
}
