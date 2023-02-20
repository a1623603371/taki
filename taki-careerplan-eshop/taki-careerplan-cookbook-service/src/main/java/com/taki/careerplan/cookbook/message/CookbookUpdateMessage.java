package com.taki.careerplan.cookbook.message;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName CookbookUpdateMessage
 * @Description TODO
 * @Author Long
 * @Date 2023/2/20 16:57
 * @Version 1.0
 */
@Data
@Builder
public class CookbookUpdateMessage {

    private Long cookbookId;



    private Long userId;
}
