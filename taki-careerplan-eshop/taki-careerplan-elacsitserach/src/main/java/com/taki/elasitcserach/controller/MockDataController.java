package com.taki.elasitcserach.controller;

import com.taki.common.utli.ResponseData;
import com.taki.elasitcserach.domain.dto.MockData1Dto;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @ClassName MockDataController
 * @Description TODO
 * @Author Long
 * @Date 2023/3/20 23:07
 * @Version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/api/mockData")
public class MockDataController {


    private static  final String dataFileName ="100k_products.text";


    @Autowired
    private RestHighLevelClient restHighLevelClient;
    
    /*** 
     * @description:  单线程插入模拟的商品数据
     * @param 
     * @return
     * @author Long
     * @date: 2023/3/20 23:16
     */ 
    @PostMapping("/mockData1")
    public ResponseData<Map<String,Object>> mockData1 (@RequestBody MockData1Dto request) throws IOException {

        if (request.validateParams()){
            return ResponseData.error("参数错误");
        }
        String indexName = request.getIndexName();
        int batchTimes = request.getBatchTimes();
        int batchSize = request.getBatchSize();
        // 1.从text文件里面加载 10w条商品 数量
        List<Map<String,Object>> skuList = loadSkusFormTxt();

        Long startTime = System.currentTimeMillis();

        //

        return ResponseData.success();
    }

    /*** 
     * @description: 读取 text文件中的sku数据
     * @param 
     * @return  java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @author Long
     * @date: 2023/3/20 23:23
     */ 
    private List<Map<String, Object>> loadSkusFormTxt() throws IOException {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(dataFileName);
        InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        List<Map<String,Object>> sukList = new ArrayList<>();

        //读取文件内容（一共100k条商品数据）
        //10001,房屋卫士自流平美缝剂瓷砖地砖专用双组份真瓷胶防水填缝剂镏金色,品质建材,398.00,上海,540785126782
        String line;
        Random random = new Random();
        while ((line = bufferedReader.readLine()) !=null){

            String[] segments = line.split(",");
            int id = Integer.parseInt(segments[0]);
            String sukName = segments[1];
            String category = segments[2].replace("会场","").replace("主会场","").replace("风格好店","");
            int basePrice  = Integer.parseInt(segments[3].substring(0,segments[3].indexOf(".")));

            if (basePrice <= 100){
                    basePrice = 200;
            }

            // 10 个字段

            Map<String,Object> sku = new HashMap<>();
            sku.put("skuId",id);
            sku.put("skuName",sukName);
            sku.put("category",category);
            sku.put("basePrice",basePrice);
            sku.put("vipPrice",basePrice  - 100);

            sku.put("saleCount",random.nextInt(100_000));
            sku.put("commentCount",random.nextInt(100_000));
            sku.put("skuImgUrl","http://sku_img_url.png");
            sku.put("createTime","2023-03-20 23:35:00");
            sku.put("updateTime","2023-03-20 23:35:00");
            sukList.add(sku);
        }
        return sukList;

    }
}
