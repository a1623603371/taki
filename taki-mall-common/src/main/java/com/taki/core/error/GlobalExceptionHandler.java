package com.taki.core.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName GlobalExceptionHandler
 * @Description 统一异常处理
 * @Author Long
 * @Date 2021/11/24 21:38
 * @Version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @description: 处理异常信息
     * @param: ex
request
     * @return: java.lang.Object
     * @author Long
     * @date: 2021/11/24 21:42
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object exceptionResponse(Exception ex, HttpServletRequest request){

        ExceptionResult.ExceptionResultBuilder result = ExceptionResult.builder();



        if (ex instanceof NullPointerException){

        }


        return result.build();
    }
}
