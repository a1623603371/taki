package com.taki.careerplan.domain.request;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName CookbookUserQueryRequest
 * @Description TODO
 * @Author Long
 * @Date 2023/2/18 21:26
 * @Version 1.0
 */
@Data
@Builder
public class CookbookUserQueryRequest {

    private Long userId;
}
