package com.taki.elasitcserach.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.taki.common.utli.DateUtils;
import com.taki.common.utli.ResponseData;
import com.taki.elasitcserach.domain.dto.MockData1Dto;
import com.taki.elasitcserach.domain.dto.MockData2DTO;
import com.taki.elasitcserach.domain.dto.MockData3DTO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;

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

        // 真正再生产环境下，不可能单线程导入大批量，一个batch做导入，这个方法实现主要是用来根
        //多线程做比较

        //2.每次随机取出batchSize个商品数据，然后批量插入，一共执行batchTimes次

        for (int i = 0; i < batchTimes; i++) {

            BulkRequest bulkRequest = buildSkuBulkRequest(indexName,batchSize,skuList);
            restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT); //把指定的多条数据打包成一个bulk插入到es中
            log.info("插入{}条商品数据",batchSize);
        }

        Long endTime = System.currentTimeMillis();

        //3记录统计数据

        int totalCount = batchSize * batchTimes;
        long elapsedSeconds = (endTime - startTime) / 1000;
        long perSecond = totalCount / elapsedSeconds;
        log.info("此次共导入[{}]条数据，耗时【{}】，平均每秒导入【{}】条数据",totalCount,elapsedSeconds,perSecond);

        Map<String,Object> result = new LinkedHashMap<>();
        result.put("startTime", DateUtil.format(new Date(startTime), DatePattern.NORM_DATETIME_FORMAT));
        result.put("endTime",DateUtil.format(new Date(endTime),DatePattern.NORM_DATETIME_FORMAT));
        result.put("totalCount",totalCount);
        result.put("elapsedSeconds",elapsedSeconds);

        return ResponseData.success(result);
    }


    @PostMapping("/mockData2")
    public ResponseData<Map<String,Object>> mockData2(@RequestBody MockData2DTO request) throws IOException, InterruptedException {

        if (request.validateParams()){
            return ResponseData.error("参数有误");
        }

        String indexName = request.getIndexName();
        int batchTimes = request.getBatchTimes();
        int batchSize = request.getBatchSize();
        int threadCount = request.getThreadCount();
        List<Map<String,Object>> skuList = loadSkusFormTxt();

        CountDownLatch countDownLatch = new CountDownLatch(request.getBatchTimes()); // 倒计时 -》一个线程完成进行 countDown 所有线程都完成了才算结束

        Semaphore semaphore = new Semaphore(threadCount);

        // 信号量，一个线程可以尝试从semaphore获取一个信号量，如果获取不到就阻塞等待，获取到了就可以执行
        // 用完就归还，最多能获取threadcount个线程，去获取信号量

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threadCount,threadCount * 2,60, TimeUnit.SECONDS,new SynchronousQueue<>());

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < batchTimes; i++) { // batch数量可以是线程数量是要多的

        // 保证一直有threadCount个线程同时执行批量操作
            // 先获取一个信号量，获取到了就执行批量操作，获取不到就这里等空余的信号量
            //如果说 threadCount 个 线程数量 对应的semaphore信号量耗尽
        semaphore.acquireUninterruptibly();

        threadPoolExecutor.submit(()->{
        BulkRequest bulkRequest = buildSkuBulkRequest(indexName,batchSize,skuList);

            try {
                restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);
                log.info("线程【{}】插入【{}】条数据",Thread.currentThread().getName(),batchTimes);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                semaphore.release();
                countDownLatch.countDown();
            }

        });
        }

        long endTime = System.currentTimeMillis();

        // 在这里等待 最后一个批次处理完成
        countDownLatch.wait();

        // 手动执行 关闭线程池
        threadPoolExecutor.shutdown();

        int totalCount = batchSize * batchTimes;
        long elapsedSeconds = (endTime - startTime) / 1000;
        long perSecond = totalCount / elapsedSeconds;
        log.info("此次共导入[{}]条数据，耗时【{}】，平均每秒导入【{}】条数据",totalCount,elapsedSeconds,perSecond);

        Map<String,Object> result = new LinkedHashMap<>();
        result.put("startTime", DateUtil.format(new Date(startTime), DatePattern.NORM_DATETIME_FORMAT));
        result.put("endTime",DateUtil.format(new Date(endTime),DatePattern.NORM_DATETIME_FORMAT));
        result.put("totalCount",totalCount);
        result.put("elapsedSeconds",elapsedSeconds);

        return ResponseData.success(result);

    }
    @PostMapping("/mockData3")
    public ResponseData<Map<String,Object>> mockData3(@RequestBody MockData3DTO request) throws IOException, InterruptedException {

        if (request.validateParams()){
            return ResponseData.error("参数有误");
        }
        String indexName = request.getIndexName();
        int batchTimes = request.getBatchTimes();
        int batchSize = request.getBatchSize();
        List<String> skuNameList =  loadSkuNamesFromTxt();
        long startTime = System.currentTimeMillis();
        //2.从第一条数据开始导入
        int index = 0;
        for (int i = 0; i < batchTimes; i++) {
            BulkRequest bulkRequest = new BulkRequest(indexName);

            for (int j = 1; j < batchSize; j++) {
                String skuName = skuNameList.get(index);
                IndexRequest indexRequest = new IndexRequest().source(XContentType.JSON,"word1",skuName,"word2",skuName);
                System.out.println(skuName);
                bulkRequest.add(indexRequest);
                index++;
            }
            log.info("开始插入【{}】条suggest数据",batchSize);
            restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);
            log.info("完成插入【{}】条suggest数据",batchSize);
        }
        long endTime = System.currentTimeMillis();
        //3. 记录统计消息
        int totalCount = batchSize * batchTimes;
        long elapsedSeconds = (endTime - startTime) / 1000;
        long perSecond = totalCount / elapsedSeconds;

        log.info("此次共导入【{}】条suggest数据，耗时【{}】秒,平均每秒导入【{}】条数据",totalCount,elapsedSeconds,perSecond);

        Map<String,Object> result = new HashMap<>();
        result.put("startTime",DateUtil.format(new Date(startTime),DatePattern.NORM_DATETIME_FORMAT));
        result.put("endTime",DateUtil.format(new Date(endTime),DatePattern.NORM_DATETIME_FORMAT));
        result.put("elapsedSeconds",elapsedSeconds);
        result.put("perSecond",perSecond);
        return ResponseData.success(result);
    }
    /*** 
     * @description: 读取txt文件中sku数据名称
     * @param
     * @return  void
     * @author Long
     * @date: 2023/3/22 21:04
     */ 
    private List<String> loadSkuNamesFromTxt() throws IOException {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(dataFileName);
        InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream);
        BufferedReader bufferedReader =  new BufferedReader(inputStreamReader);

        List<String> skuNameList = new ArrayList<>();


        // 读取文件内容（一共100k的商品数量）
        String line;

        while ((line = bufferedReader.readLine()) != null){
            String[] segments = line.split(",");
            String skuName = segments[1];
            skuNameList.add(skuName);
        }

        return skuNameList;

    }

    /*** 
     * @description: 多线程插入模拟的商品数据
     * @param request
     * @return  com.taki.common.utli.ResponseData<java.util.Map<java.lang.String,java.lang.Object>>
     * @author Long
     * @date: 2023/3/21 18:13
     */ 
    @PostMapping("/indexAllProductData")
    public ResponseData<Map<String,Object>> indexAllProductData(@RequestBody MockData2DTO request) throws InterruptedException {

        if (request.validateParams()){
            return ResponseData.error("参数错误");
        }


        int batchCount = 1;
        int batchSize = 100_000;
        int bulkSize = request.getBatchSize();

        int bulkCount = batchSize / bulkSize +1; // 100000 / 150 = 666.66 可以

        int threadCount  = request.getThreadCount();

        long startTime = System.currentTimeMillis();


        for (int batchIndex = 1; batchIndex <= batchCount ; batchIndex++) {

            List<Map<String,Object>> batchList = queryProductBatchFromDataBase(batchIndex,batchSize);

            CountDownLatch countDownLatch = new CountDownLatch(bulkCount);

            Semaphore semaphore = new Semaphore(threadCount);

            ThreadPoolExecutor threadPoolExecutor  = new ThreadPoolExecutor(threadCount,threadCount * 2,60,TimeUnit.SECONDS,new SynchronousQueue<>());

            int bulkDataCurrentIndex = 0;

            for (int bulkIndex = 1; bulkIndex < bulkCount ; bulkIndex++) {

                List<Map<String,Object>> bulkList = new ArrayList<>();

                List<String> skuNameBulkList = new ArrayList<>();

                for (int  bulkDataIndex = bulkDataCurrentIndex; bulkDataIndex <  bulkDataCurrentIndex + bulkSize;bulkDataIndex++){
                        if (batchList.get(batchIndex) == null){
                            break;
                        }
                    bulkList.add(batchList.get(batchIndex));
                    skuNameBulkList.add(String.valueOf(batchList.get(bulkIndex).get("skuName")));
                }
                    bulkDataCurrentIndex += bulkSize;
                    semaphore.acquireUninterruptibly();
                    threadPoolExecutor.submit(()->{
                        try {
                        if (bulkList.size() > 0){
                            BulkRequest productIndexBulkRequest = buildProductIndexBulkRequest(bulkList);
                                restHighLevelClient.bulk(productIndexBulkRequest,RequestOptions.DEFAULT);
                        }
                        if (skuNameBulkList.size() > 0){
                            BulkRequest suggestIndexBulkRequest = buildSuggestIndexBulkRequest(skuNameBulkList);
                            restHighLevelClient.bulk(suggestIndexBulkRequest,RequestOptions.DEFAULT);
                        }
                        log.info("线程【{}】插入【{}】条数据",Thread.currentThread().getName(),bulkList.size());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally {
                            semaphore.release();
                            countDownLatch.countDown();
                        }
                    });

            }
            countDownLatch.wait();
            threadPoolExecutor.shutdown();

        }

        long endTime = System.currentTimeMillis();

        int totalCount = batchSize * batchCount;
        long elapsedSeconds = (endTime - startTime) / 1000;
        long perSecond = totalCount / elapsedSeconds;
        log.info("此次共导入[{}]条商品数据，耗时[{}]秒，平均每秒导入[{}]条数据", totalCount, elapsedSeconds, perSecond);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("startTime", DateUtil.format(new Date(startTime), DatePattern.NORM_DATETIME_PATTERN));
        result.put("endTime", DateUtil.format(new Date(endTime), DatePattern.NORM_DATETIME_PATTERN));
        result.put("totalCount", totalCount);
        result.put("elapsedSeconds", elapsedSeconds);
        result.put("perSecond", perSecond);
        return ResponseData.success(result);
    }

    private BulkRequest buildSuggestIndexBulkRequest(List<String> skuNameBulkList) {
        BulkRequest bulkRequest = new BulkRequest("suggest_index");

        for (int i = 0; i < skuNameBulkList.size(); i++) {
            String skuName = skuNameBulkList.get(i);
            IndexRequest indexRequest = new IndexRequest().source(XContentType.JSON,"word1",skuName,"word2",skuName);
            bulkRequest.add(indexRequest);
        }
        return bulkRequest;


    }

    private BulkRequest buildProductIndexBulkRequest(List<Map<String, Object>> bulkList) {

        BulkRequest bulkRequest = new BulkRequest("product_index");

        for (int i = 0; i < bulkList.size() ; i++) {
            Map<String,Object> productDataMap = bulkList.get(i);

            List<Object> productDataList = new ArrayList<>();

            productDataMap.forEach((key,value)->{
                productDataList.add(key);
                productDataList.add(value);
            });

            IndexRequest indexRequest = new IndexRequest().source(XContentType.JSON,productDataList.toArray());
            bulkRequest.add(indexRequest);
        }

        return bulkRequest;
    }


    private List<Map<String, Object>> queryProductBatchFromDataBase(int batchIndex, int batchSize) {

        // 根据第几个batch，每个batch 多少条数据，从数据库里去做一个sql 查询，把一批一批的数据查询出来
        return  new ArrayList<>();
    }

    /***
     * @description:  从 10w 个 里面随机 选择 batchSize 个，然后封装成一个批量插入的 BulkReqeust 对象
     * @param indexName
     * @param  batchSize
     * @param skuList
     * @return  org.elasticsearch.action.bulk.BulkRequest
     * @author Long
     * @date: 2023/3/21 17:14
     */
    private BulkRequest buildSkuBulkRequest(String indexName, int batchSize, List<Map<String, Object>> skuList) {
        BulkRequest bulkRequest = new BulkRequest(indexName);

        Random random = new Random();

        for (int i = 0; i < batchSize; i++) {

            int idx = random.nextInt(100_000); // 0 - 100000
            Map<String,Object> map = skuList.get(idx);

            List<Object> list = new ArrayList<>();

            map.forEach((key,value)->{
                list.add(key);
                list.add(value);
            });
            IndexRequest indexRequest = new IndexRequest().source(XContentType.JSON,list.toArray());
            bulkRequest.add(indexRequest);
        }

        return bulkRequest;
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
