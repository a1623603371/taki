package com.taki.order.config;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName RequestOriginParserImpl
 * @Description TODO
 * @Author Long
 * @Date 2022/6/21 17:32
 * @Version 1.0
 */
@Component
@Slf4j
public class RequestOriginParserImpl implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest) {
        String userId = httpServletRequest.getHeader("user_id");
        log.warn("RequestOriginParserImpl:userId:{}",userId);
        if (StringUtils.isBlank(userId)){
            log.warn("user_id is not empty");
        }
        return userId;
    }
}
