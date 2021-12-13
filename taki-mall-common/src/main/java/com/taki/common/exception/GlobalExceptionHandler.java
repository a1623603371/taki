package com.taki.common.exception;

import com.taki.common.utlis.ExceptionUtils;
import com.taki.common.utlis.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @ClassName GlobalExceptionHandler
 * @Description 统一异常处理
 * @Author Long
 * @Date 2021/11/24 21:38
 * @Version 1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * @description: 处理系统异常信息
     * @param ex 异常信息
     *  @param  request 请求
     * @return: java.lang.Object
     * @author Long
     * @date: 2021/11/24 21:42
     */
    @ExceptionHandler(Exception.class)
    //@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseData<Object>  exceptionResponse(Exception ex, HttpServletRequest request){

        ExceptionResult.ExceptionResultBuilder result = ExceptionResult.builder();
       // String trace =  ExceptionUtils.getStackTrace(ex);
        log.error("[系统未知错误]",ex);
         result =  ExceptionResult.builder().timestamp(new Date())
                .message(ex.getMessage())
          //      .trace(trace)
                .exceptionName(ex.getClass().getName())
                .path(request.getRequestURI());
        return ResponseData.error(ErrorCodeEnum.SYSTEM_ERROR,result);
    }

    /** 
     * @description: 业务异常
     * @param ex 异常信息
     * @param request 请求
     * @return:
     * @author Long
     * @date: 2021/12/13 17:05
     */ 
    @ExceptionHandler(ServiceException.class)
    public ResponseData<Object> exceptionResponse(ServiceException ex, HttpServletRequest request){

      //  String trace =  ExceptionUtils.getStackTrace(ex);

        log.error("业务异常",ex);
      //  log.error(trace);
        ExceptionResult result = ExceptionResult.builder().timestamp(new Date())
                .message(ex.getMessage())
               // .trace(trace)
                .exceptionName(ex.getClass().getName())
                .path(request.getRequestURI())
                .build();

        return  ResponseData.error(ErrorCodeEnum.BUSINESS_ERROR,result);
    }

}
