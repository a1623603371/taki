package com.taki.elasitcserach.service;

import com.taki.elasitcserach.domain.request.FullTextSearchRequest;
import com.taki.elasitcserach.domain.request.StructuredSearchRequest;
import org.elasticsearch.action.search.SearchResponse;

import java.io.IOException;

/**
 * @ClassName ProductService
 * @Description 商品检查服务
 * @Author Long
 * @Date 2023/3/22 21:59
 * @Version 1.0
 */
public interface ProductService {

    /*** 
     * @description: 全文检索接口
     * @param request
     * @return  org.elasticsearch.action.search.SearchResponse
     * @author Long
     * @date: 2023/3/22 22:12
     */ 
    SearchResponse fullTextSearch(FullTextSearchRequest request) throws IOException;
    
    /*** 
     * @description: 商品 结构化 搜索接口
     * @param request
     * @return  org.elasticsearch.action.search.SearchResponse
     * @author Long
     * @date: 2023/3/24 20:59
     */ 
    SearchResponse structuredSearch(StructuredSearchRequest request) throws IOException;
}
