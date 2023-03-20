package com.taki.careerplan.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName SaveOrUpdateSkuDTO
 * @Description TODO
 * @Author Long
 * @Date 2023/2/24 19:10
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveOrUpdateSkuDTO {

    private Boolean success;

    private Long skuId;
}
