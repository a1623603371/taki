package com.taki.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taki.core.enums.CodeEnum;
import com.taki.core.error.ExceptionResult;
import com.taki.core.utlis.ResponseData;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @ClassName ResponseResult
 * @Description 返回增强
 * @Author Long
 * @Date 2021/11/24 22:00
 * @Version 1.0
 */
@ControllerAdvice(basePackages = "")
public class ResponseResult<T> implements ResponseBodyAdvice<Object> {
    /**
     * @description: 是否开启结果响应拦截
     * @param: methodParameter 方法参数
     * @param:   aClass calss 对象
     * @return: 是否开启
     * @author Long
     * @date: 2021/11/24 22:01
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }
    /**
     * @description:  拦截响应
     * @param: o
     * @param:  methodParameter
     * @param:  mediaType
     * @param:  aClass
     * @param:  serverHttpRequest
     * @param:  serverHttpResponse
     * @return: java.lang.Object
     * @author Long
     * @date: 2021/11/24 22:02
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        System.out.println("进来了");
        if (returnType.getGenericParameterType().equals(String.class)){
            ObjectMapper objectMapper = new ObjectMapper();
            //这里 将数据封装成VO 对象放回

            try {
                return objectMapper.writeValueAsString(ResponseData.success(body));
            } catch (JsonProcessingException e) {
                // 加入自定义的统一异常处理
                e.printStackTrace();
            }
        }else if (body instanceof ExceptionResult){ // 判断是否是异常对象类型
           ResponseData.error(CodeEnum.SYSTEM_ERROR,body);
        }

        return ResponseData.success(body);
    }


}
