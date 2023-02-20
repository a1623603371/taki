package com.taki.careerplan.domain.request;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName CookbookQueryRequest
 * @Description TODO
 * @Author Long
 * @Date 2023/2/20 17:37
 * @Version 1.0
 */
@Data
@Builder
public class CookbookQueryRequest {

    private Long cookbookId;

    private  Boolean success;
}
