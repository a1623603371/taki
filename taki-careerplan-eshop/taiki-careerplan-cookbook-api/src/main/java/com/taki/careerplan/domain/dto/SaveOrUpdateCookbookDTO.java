package com.taki.careerplan.domain.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName SaveOrUpdateCookbookDTO
 * @Description TODO
 * @Author Long
 * @Date 2023/2/18 22:52
 * @Version 1.0
 */
@Data
@Builder
public class SaveOrUpdateCookbookDTO {

    private  Boolean success;

    private Long cookbookId;
}
