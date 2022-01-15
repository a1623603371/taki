package com.taki.common.utlis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taki.common.config.ObjectMapperImpl;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.spi.http.HttpContext;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @ClassName ServletUtil
 * @Description  获取 Servlet 相关 工具类
 * @Author Long
 * @Date 2022/1/15 15:19
 * @Version 1.0
 */
public class ServletUtil {

    /***
     * @description:  获取 servletRequest
     * @param
     * @return  servletRequest
     * @author Long
     * @date: 2022/1/15 15:22
     */
    public static HttpServletRequest getRequest(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        return attributes.getRequest();
    }

    /***
     * @description:  获取 ServletResponse
     * @param
     * @return  ServletResponse
     * @author Long
     * @date: 2022/1/15 15:22
     */
    public static HttpServletResponse getResponse(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getResponse();
    }

    /** 
     * @description: 响应JSON 结果
     * @param response 响应
     * @param  obj 数据
     * @return  void
     * @author Long
     * @date: 2022/1/15 15:24
     */ 
    public  static void writeJsonMessage(HttpServletResponse response,Object obj){

        ObjectMapper objectMapper  = new ObjectMapperImpl();
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        try (PrintWriter writer =  response.getWriter()){
            writer.print(objectMapper.writeValueAsBytes(obj));

            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
