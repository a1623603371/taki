package com.taki.elasitcserach.service.impl;

import com.alibaba.fastjson.JSON;
import com.taki.elasitcserach.domain.request.FullTextSearchRequest;
import com.taki.elasitcserach.domain.request.StructuredSearchRequest;
import com.taki.elasitcserach.service.ProductService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.ml.inference.NamedXContentObject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.*;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.SearchModule;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * @ClassName P
 * @Description TODO
 * @Author Long
 * @Date 2023/3/22 22:12
 * @Version 1.0
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;



    @Override
    public SearchResponse fullTextSearch(FullTextSearchRequest request) throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.trackTotalHits(true);

        //1.构造match条件
        request.getQueryTexts().forEach((field,text)->{
            searchSourceBuilder.query(QueryBuilders.matchQuery(field,text));
        });

        //2设置搜索高亮配置
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field(request.getHighLightField());
        highlightBuilder.postTags("<span stype=color:red>");// 搜索结果，商品标题（跟搜索词匹配的部分显示为红色）
        highlightBuilder.postTags("</span>");
        highlightBuilder.numOfFragments(0);
        searchSourceBuilder.highlighter(highlightBuilder);

        //3.设置搜索分页
        int from = (request.getPageNum() - 1) * request.getPageSize();
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(request.getPageSize());

        //4. 封装搜索请求
        SearchRequest searchRequest = new SearchRequest(request.getIndexName());
        searchRequest.source(searchSourceBuilder);

        // 5.查询elasticserach
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        // 6. 对结果进行高亮处理
        SearchHits hits = searchResponse.getHits();
        hits.forEach(hit ->{
            HighlightField highlightField = hit.getHighlightFields().get(request.getHighLightField());
            Map<String,Object> sourceMap  = hit.getSourceAsMap();
            Text[] fragments = highlightField.fragments();
            StringBuilder builder = new StringBuilder();
            for (Text fragment : fragments) {
                builder.append(fragment.toString());
            }
            sourceMap.put(request.getHighLightField(),builder.toString());
        });
        return searchResponse;
    }

    @Override
    public SearchResponse structuredSearch(StructuredSearchRequest request) throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.trackTotalHits(true);

        //1解析queryDSL
        String queryDsl = JSON.toJSONString(request.getQueryDsl());
        SearchModule searchModule = new SearchModule(Settings.EMPTY,false, Collections.emptyList());
        NamedXContentRegistry namedXContentRegistry = new NamedXContentRegistry(searchModule.getNamedXContents());
        XContent xContent = XContentFactory.xContent(XContentType.JSON);
        XContentParser xContentParser = xContent.createParser(namedXContentRegistry,LoggingDeprecationHandler.INSTANCE,queryDsl);
        searchSourceBuilder.parseXContent(xContentParser);

        //2.设置搜索分页参数
        int from = (request.getPageNum() -1) * request.getPageSize();
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(request.getPageSize());
        // 3.封装搜索请求
        SearchRequest searchRequest = new SearchRequest(request.getIndexName());
        searchRequest.source(searchSourceBuilder);
        return restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
    }
}
