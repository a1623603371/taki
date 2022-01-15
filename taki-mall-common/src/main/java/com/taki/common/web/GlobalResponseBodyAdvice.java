package com.taki.common.web;



import com.taki.common.utlis.JsonUtil;
import com.taki.common.utlis.ResponseData;
import com.taki.common.utlis.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import springfox.documentation.swagger.web.ApiResourceController;

import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName ResponseResult
 * @Description 返回增强
 * @Author Long
 * @Date 2021/11/24 22:00
 * @Version 1.0
 */
@Slf4j
//@ControllerAdvice
public class GlobalResponseBodyAdvice<T> implements ResponseBodyAdvice<Object> {
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
        Class<?> declaringClass = aClass.getDeclaringClass();
        if (declaringClass.equals(ApiResourceController.class) ||  declaringClass.equals(null)){
            return false;
        }
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


        if (selectedContentType.equals(MediaType.APPLICATION_JSON)){
            return body;
        }

        if (body instanceof  ResponseData ){
            return body;
        }else if (body instanceof String){
            try {
                HttpServletResponse httpServletResponse = ServletUtil.getResponse();
                if (httpServletResponse != null){
                    ServletUtil.writeJsonMessage(httpServletResponse,ResponseData.success(body));
                    return null;
                }
            }catch (Exception e){
                log.warn("响应字符串对象前端异常",e);
            }

            return JsonUtil.object2Json(ResponseData.success(body));
        }

        return ResponseData.success(body);
    }


}
