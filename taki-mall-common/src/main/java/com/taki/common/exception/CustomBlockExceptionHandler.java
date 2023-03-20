package com.taki.common.exception;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.taki.common.utli.ResponseData;
import com.taki.common.utli.ServletUtil;
import org.springframework.stereotype.Component;
import sun.security.x509.AuthorityInfoAccessExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName CustomBlockExceptionHander
 * @Description TODO
 * @Author Long
 * @Date 2023/3/13 16:36
 * @Version 1.0
 */
@Component
public class CustomBlockExceptionHandler implements BlockExceptionHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {

        ResponseData<Object>  responseData =  null;

        if (e instanceof FlowException){
            responseData = ResponseData.error(20002,"限流控制");
        }
        if (e instanceof DegradeException){
            responseData = ResponseData.error(20003,"降级控制");
        }
        if (e instanceof AuthorityException){
            responseData = ResponseData.error(20003,"授权控制");
        }
        httpServletResponse.setStatus(200);
        // 输出到前端响应到 前端
        ServletUtil.writeJsonMessage(httpServletResponse,responseData);
    }
}
