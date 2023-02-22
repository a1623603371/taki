package com.taki.careerplan.domain.request;

import com.taki.common.page.BasePage;
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
public class CookbookQueryRequest extends BasePage {

    private Long cookbookId;


    private Long userId;

    private  Boolean success;
}
