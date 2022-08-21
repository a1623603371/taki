package com.taki.wms.filter;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @ClassName MyFilter
 * @Description TODO
 * @Author Long
 * @Date 2022/8/14 17:44
 * @Version 1.0
 */
@Slf4j
public class MyFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("MyFilter.doFilter");

        if (servletRequest instanceof HttpServletRequest){
            log.info("IP:"+servletRequest.getRemoteAddr());
            log.info("URL:" + ((HttpServletRequest) servletRequest).getRequestURI());
        }

        filterChain.doFilter(servletRequest,servletResponse);

    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("start application init filter");
    }

    @Override
    public void destroy() {
        log.info("stop application clod filter");
    }
}
