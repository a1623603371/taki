package com.taki.elasitcserach.domain.request;

import lombok.Data;

import java.util.Map;

/**
 * @ClassName FullTextSearchRequest
 * @Description TODO
 * @Author Long
 * @Date 2023/3/22 22:02
 * @Version 1.0
 */
@Data
public class FullTextSearchRequest {

    /**
     * 索引名称
     */
    private String indexName;

    /**
     * 查询参数
     * key 为字段的名字，value为字段的关键字
     *可以指定从那些字段里检索
     */
    private Map<String,String> queryTexts;

    /**
     * 高亮字段
     */
    private String highLightField;

    /**
     * 当前 页
     */
    private int pageNum;

    /**
     *
     */
    private int pageSize;
}
