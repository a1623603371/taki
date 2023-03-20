package com.taki.elasitcserach.domain.request;

import lombok.Data;

/**
 * @ClassName AutoCompleteRequest
 * @Description TODO
 * @Author Long
 * @Date 2023/3/20 22:03
 * @Version 1.0
 */
@Data
public class AutoCompleteRequest {

    /**
     * 索引名称
     */
 private String indexName;

    /**
     * 字段名称
     */
 private   String fieldName;

    /**
     * 需要补全的词（用户输入内容）
     */
 private String text;


    /**
     * 返回对个补全后词
     */
 private int count;
}
