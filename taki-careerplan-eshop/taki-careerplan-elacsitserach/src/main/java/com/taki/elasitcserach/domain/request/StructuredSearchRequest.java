package com.taki.elasitcserach.domain.request;

import lombok.Data;

import java.util.Map;

/**
 * @ClassName StructuredSearchRequest
 * @Description TODO
 * @Author Long
 * @Date 2023/3/23 15:57
 * @Version 1.0
 */
@Data
public class StructuredSearchRequest {

    /**
     * 索引名称
     */
    private String indexName;

    /**
     * Query DSL，ES查询语法，是按照JSON来组织的
     * 按照elasticSearch的规范 query DSL 是一个json 对象
     *解析的时候转换成JSON字符串，客户端api可以直接解析字符串
     */
    private Map<String,Object> queryDsl;


    private int pageNum;

    private  int pageSize;
}
