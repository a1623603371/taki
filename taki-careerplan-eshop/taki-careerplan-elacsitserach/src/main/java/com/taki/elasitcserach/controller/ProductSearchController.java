package com.taki.elasitcserach.controller;

import com.taki.common.utli.ResponseData;
import com.taki.elasitcserach.domain.request.FullTextSearchRequest;
import com.taki.elasitcserach.domain.request.StructuredSearchRequest;
import com.taki.elasitcserach.service.ProductService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ProductSearchController
 * @Description TODO
 * @Author Long
 * @Date 2023/3/22 21:57
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/product")
public class ProductSearchController {

    @Autowired
    private ProductService productService;

    /***
     * @description: 全文 检索
     * @param request
     * @return  com.taki.common.utli.ResponseData<java.util.Map<java.lang.String,java.lang.Object>>
     * @author Long
     * @date: 2023/3/22 22:08
     */
    @PostMapping("/fullTextSearch")
    public ResponseData<Map<String,Object>> fullTextSearch(@RequestBody FullTextSearchRequest request) throws IOException {

        SearchResponse searchResponse =  productService.fullTextSearch(request);
        Map<String,Object> resultMap = new HashMap<>();
        SearchHit[] hits = searchResponse.getHits().getHits();
        long totalCount = searchResponse.getHits().getTotalHits().value;
        resultMap.put("hits",hits);
        resultMap.put("totalCount",totalCount);
        resultMap.put("pageNum",request.getPageNum());
        resultMap.put("pageSize",request.getPageSize());
        return  ResponseData.success(resultMap);
    }

    
    /*** 
     * @description:  商品 结构化 搜索接口
     * @param request
     * @return  com.taki.common.utli.ResponseData<java.util.Map<java.lang.String,java.lang.Object>>
     * @author Long
     * @date: 2023/3/23 16:01
     */ 
    @PostMapping("/structuredSearch")
    public  ResponseData<Map<String,Object>> structuredSearch(@RequestBody StructuredSearchRequest request) throws IOException {

        SearchResponse searchResponse = productService.structuredSearch(request);

        Map<String,Object> resultMap = new HashMap<>();
        SearchHit[] hits = searchResponse.getHits().getHits();
        long totalCount = searchResponse.getHits().getTotalHits().value;
        resultMap.put("hits",hits);
        resultMap.put("totalCount",totalCount);
        resultMap.put("pageNum",request.getPageNum());
        resultMap.put("pageSize",request.getPageSize());

        return ResponseData.success(resultMap);
    }

}
