package com.taki.common.utli;

import cn.hutool.http.HttpUtil;

import com.taki.common.exception.ErrorCodeEnum;
import com.taki.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName HttpClientUtils
 * @Description HttpClient 工具类
 * @Author Long
 * @Date 2021/12/6 11:32
 * @Version 1.0
 */
@Slf4j
public class HttpClientUtils {

    /**
     * JSON 请求方式
     */
    private static final MediaType jsonType = MediaType.parse("application/json; charset = utf-8");
    /**
     * 流传输
     */
    private static final MediaType mediaType = MediaType.parse("application/octet-stream");

    private static volatile OkHttpClient okHttpClientProxy = null;
    /**
     * HttpClient
     */
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(1000, 5L, TimeUnit.MINUTES))
            .retryOnConnectionFailure(false)
            .build();


    /**
     * @description: POST 请求
     * @param: ip IP地址
     * @param:port 端口
     * @param:url 请求url
     * @param:params 请求参数
     * @param:heads 请求头
     * @return:
     * @author Long
     * @date: 2021/12/6 12:01
     */
    public static String requestPostForm(String ip, int port, String url, HashMap<String, String> params, HashMap<String, String> heads) throws Exception {
        OkHttpClient httpClient = okHttpClient;

        if (!StringUtils.isEmpty(ip)) {
            httpClient = createHttpClientProxy(ip, port);
        }

        FormBody.Builder formBody = new FormBody.Builder();
        // 封装请求参数
        if (!ObjectUtils.isEmpty(params)) {
            params.forEach((key, value) -> {
                formBody.add(key, value);
            });
        }
        RequestBody requestBody = formBody.build();

        Request.Builder requestBuilder = new Request.Builder();
        // 封装请求头
        if (ObjectUtils.isEmpty(heads)) {
            heads.forEach((key, value) -> {
                requestBuilder.addHeader(key, value);
            });
        }
        requestBuilder.url(url);
        requestBuilder.post(requestBody);
        // 构建请求
        Request request = requestBuilder.build();

        Long startTime = System.currentTimeMillis();
        Response response = httpClient.newCall(request).execute();

        Long endTime = System.currentTimeMillis();

        if (!response.isSuccessful()) {
            log.warn("HTTP POST 连接失败;url = {}", response.code(), url);
            throw new ServiceException(ErrorCodeEnum.ERROR_REMOTE_SERVER, response.body().toString(), null);
        }
        if (response.code() != 200) {
            log.warn("HTTP POST 请求失败;[errorCode ={}],url = {}", response.code(), url);
        }

        log.info("HTTP 请求耗时{}", (endTime - startTime));

        return response.body().string();

    }


    /**
     * @description: json 方式请求
     * @param:
     * @return: 接口返回数据
     * @author Long
     * @date: 2021/12/6 14:38
     */
    public static String requestPostJson(String ip, int port, String url, HashMap<String, String> params, String json) throws ServiceException {
        OkHttpClient httpClient = okHttpClient;

        if (!StringUtils.isEmpty(ip)) {
            httpClient = createHttpClientProxy(ip, port);
        }
        Long startTime = System.currentTimeMillis();
        RequestBody requestBody = RequestBody.create(jsonType, json);

        Request request = new Request.Builder().url(url).post(requestBody).build();
        Response response;
        try {
            response = httpClient.newCall(request).execute();
            Long endTime = System.currentTimeMillis();

            if (!response.isSuccessful()) {
                log.warn("HTTP POST 连接失败;url = {}", response.code(), url);
                throw new ServiceException(ErrorCodeEnum.ERROR_REMOTE_SERVER, response.body().toString(), null);
            }
            if (response.code() != 200) {
                log.warn("HTTP POST 请求失败;[errorCode ={}],url = {}", response.code(), url);
            }

            log.info("HTTP 请求耗时{}", (endTime - startTime));

            return response.body().toString();
        } catch (IOException e) {
            log.error("HTTP POST 发生异常;url={}", url);
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @description: get 获取数据
     * @param: ip IP地址
     * @param:port 端口
     * @param:url 请求URL
     * @param:paramMap 请求参数
     * @return: java.lang.String
     * @author Long
     * @date: 2021/12/6 15:19
     */
    public static String requestGet(String ip, int port, String url, HashMap<String, String> paramMap) throws ServiceException {
        OkHttpClient httpClient = okHttpClient;
        if (!StringUtils.isEmpty(ip)) {
            httpClient = createHttpClientProxy(ip, port);
        }

        if (StringUtils.isEmpty(url)) {
            log.error("URL 为null");
            return null;
        }
        Request.Builder requestBuilder = new Request.Builder();
        if (!ObjectUtils.isEmpty(paramMap)) {
            paramMap.forEach((key, value) -> {
                requestBuilder.header(key, value);
            });
        }
        Request request = requestBuilder.get().url(url).build();
        try {
            Response response = httpClient.newCall(request).execute();

            if (response.code() == 200) {
                log.info("HTTP GET 请求调用成功 [URL = {}]", url);
                return response.body().toString();
            } else {
                log.warn("HTTP GET 请求调用失败 [ERROR_CODE = {},URL = {}]", response.code(), url);
            }

        } catch (IOException e) {
            log.error("请求调用失败 url = {}", url);
            throw new ServiceException(ErrorCodeEnum.ERROR_REMOTE_SERVER, "调用 URL = " + url + "失败", null);
        }
        return null;

    }

    /**
     * @description: 创建 代理 HttpClient
     * @param: ip ip地址
     * @param:port 端口号
     * @return: 代理HttpClient
     * @author Long
     * @date: 2021/12/6 14:19
     */
    private static OkHttpClient createHttpClientProxy(String ip, int port) {
        if (okHttpClientProxy == null) {
            synchronized (HttpUtil.class) {
                if (okHttpClientProxy == null) {
                    okHttpClientProxy = new OkHttpClient().newBuilder()
                            //设置代理
                            .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port)))
                            .connectTimeout(120, TimeUnit.SECONDS)
                            .readTimeout(120, TimeUnit.SECONDS)
                            .writeTimeout(120, TimeUnit.SECONDS)
                            .connectionPool(new ConnectionPool(1000, 5L, TimeUnit.MINUTES))
                            // 连接失败是否重试
                            .retryOnConnectionFailure(false)
                            .build();
                }
                log.info("创建代理 HTTP Client");
            }

        }

        return okHttpClientProxy;
    }

}
