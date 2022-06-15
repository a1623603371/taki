package com.taki.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName GateWayConfiguration
 * @Description gateWay 网关配置
 * @Author Long
 * @Date 2022/6/15 12:34
 * @Version 1.0
 */
@Configuration
public class GateWayConfiguration {

    private final List<ViewResolver> viewResolvers;

    private final ServerCodecConfigurer serverCodecConfigurer;


    public GateWayConfiguration(ObjectProvider<List<ViewResolver>> viewResolversProvider, ServerCodecConfigurer serverCodecConfigurer) {
        this.viewResolvers = viewResolversProvider.getIfAvailable();
        this.serverCodecConfigurer = serverCodecConfigurer;
    }


    /** 
     * @description: 配置限流的异常处理器
     * @param 
     * @return
     * @author Long
     * @date: 2022/6/15 12:37
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler(){
        return new SentinelGatewayBlockExceptionHandler(viewResolvers,serverCodecConfigurer);
    }

    /** 
     * @description: 初始化自定义限流异常页面
     * @param 
     * @return  void
     * @author Long
     * @date: 2022/6/15 12:39
     */ 
    @PostConstruct
    public void initBlockHandlers(){
        BlockRequestHandler blockRequestHandler = new BlockRequestHandler() {
            @Override
            public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {

                Map<String,Object> map = new HashMap<>();
                map.put("success",false);
                map.put("errorCode",200);
                map.put("errorMessage","接口限流");

                return ServerResponse
                        .status(HttpStatus.OK)
                        .header(HttpHeaders.CONTENT_TYPE,"application/json;charset=UTF-8")
                        .body(BodyInserters.fromValue(map));
            }
        };
        GatewayCallbackManager.setBlockHandler(blockRequestHandler);
    }
}
