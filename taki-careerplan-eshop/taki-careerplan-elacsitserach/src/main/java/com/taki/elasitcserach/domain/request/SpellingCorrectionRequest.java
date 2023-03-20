package com.taki.elasitcserach.domain.request;

import lombok.Data;

/**
 * @ClassName SpellingCorrectionRequest
 * @Description 拼音 更正请求
 * @Author Long
 * @Date 2023/3/20 22:53
 * @Version 1.0
 */
@Data
public class SpellingCorrectionRequest {

    /**
     * 索引 名称
     */
    private String indexName;

    /**
     * 字段 名称
     */
    private String fieldName;

    /**
     * 用户输入内容
     */
    private String text;
}
