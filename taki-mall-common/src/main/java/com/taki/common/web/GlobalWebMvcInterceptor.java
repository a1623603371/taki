package com.taki.common.web;

import com.taki.common.core.CoreConstants;
import com.taki.common.utli.MdcUtil;
import com.taki.common.utli.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName GlobalWebMvcInterceptor
 * @Description 全局 Spring mvc 拦截组件
 * @Author Long
 * @Date 2022/6/9 14:26
 * @Version 1.0
 */
@Slf4j
public class GlobalWebMvcInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String traceId = request.getHeader(CoreConstants.TRACE_ID);

        if (traceId == null  ||  "".equals(traceId)){
            traceId = request.getParameter(CoreConstants.TRACE_ID);
        }

        if (traceId != null && !"".equals(traceId)){
            String _traceId = MdcUtil.getTraceId();

            if (_traceId == null ||  !_traceId.equals(traceId)){
                MdcUtil.setTraceId(traceId);
            }
        }else {
            //获取 traceId
            traceId = SnowFlake.generateIdStr();

            log.info("traceId:{}",traceId);
            MdcUtil.setUserTraceId(traceId);
        }

        // 处理其他全局参数  token 等

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MdcUtil.clear();
    }


}
