package com.taki.elasitcserach.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName ElasticSearchConfig
 * @Description  es 客户端 bean
 * @Author Long
 * @Date 2023/3/20 23:09
 * @Version 1.0
 */
@Configuration
public class ElasticSearchConfig {

    @Value("${elasticsearch.addr}")
    private String addr;


    @Bean(destroyMethod =  "close")
    public RestHighLevelClient  restHighLevelClient(){

        String[] segments = addr.split(",");

        HttpHost[] esNodes = new HttpHost[segments.length];

        for (int i = 0; i < segments.length; i++) {
            String[] hostAndPort = segments[i].split(":");
            esNodes[i] = new HttpHost(hostAndPort[0],Integer.parseInt(hostAndPort[1]),"http");
        }
        return new RestHighLevelClient(RestClient.builder(esNodes));

    }
}
