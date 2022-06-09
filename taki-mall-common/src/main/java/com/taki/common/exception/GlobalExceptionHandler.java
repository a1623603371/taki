package com.taki.common.exception;

import com.taki.common.utlis.ExceptionUtils;
import com.taki.common.utlis.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;
import java.util.MissingFormatArgumentException;

/**
 * @ClassName GlobalExceptionHandler
 * @Description 统一异常处理
 * @Author Long
 * @Date 2021/11/24 21:38
 * @Version 1.0
 */
@RestControllerAdvice
@Slf4j
@Order
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
    public ResponseData<Object>  handle(Exception ex, HttpServletRequest request){

        ExceptionResult.ExceptionResultBuilder result = ExceptionResult.builder();
        log.error("[系统未知错误]",ex);
         result =  ExceptionResult.builder().timestamp(new Date())
                .message(ex.getMessage())
          //      .trace(trace)
                .exceptionName(ex.getClass().getName())
                .path(request.getRequestURI());

        return ResponseData.error(ErrorCodeEnum.SYSTEM_ERROR,result.build());
    }



    /** 
     * @description: 1001 HTTP 请求 方法类型错误
     * @param e
     * @return  c=
     * @author Long
     * @date: 2022/6/9 14:07
     */ 
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public  ResponseData<Object> handle(HttpRequestMethodNotSupportedException e,HttpServletRequest request){
        ExceptionResult.ExceptionResultBuilder result = ExceptionResult.builder();
        log.error("[客户端 HTTP 请求方法类型错误]",e);
        result =  ExceptionResult.builder().timestamp(new Date())
                .message(e.getMessage())
                //      .trace(trace)
                .exceptionName(e.getClass().getName())
                .path(request.getRequestURI());
        return ResponseData.error(ErrorCodeEnum.CLIENT_HTTP_METHOD_ERROR,result.build());
    }

    /*** 
     * @description: 1002  客户端请求参数效验不通过
     * @param e
     * @param request
     * @return
     * @author Long
     * @date: 2022/6/9 14:10
     */ 
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public  ResponseData<Object> handle(MethodArgumentNotValidException e, HttpServletRequest request){
        ExceptionResult.ExceptionResultBuilder result = ExceptionResult.builder();
        log.error("[客户端 HTTP 请求参数效验不通过]",e);
        result =  ExceptionResult.builder()
                .respMsg(handle(e.getBindingResult().getFieldErrors()))
                .timestamp(new Date())
                .message(e.getMessage())
                //      .trace(trace)
                .exceptionName(e.getClass().getName())
                .path(request.getRequestURI());
        return ResponseData.error(ErrorCodeEnum.CLIENT_REQUEST_BODY_CHECK_ERROR,result.build());
    }


    private String handle(List<FieldError> fieldErrors) {
        StringBuilder sb = new StringBuilder();
        for (FieldError obj : fieldErrors) {
            sb.append(obj.getField());
            sb.append("=[");
            sb.append(obj.getDefaultMessage());
            sb.append("]  ");
        }
        return sb.toString();
    }


    /**
     * @description: 1003 客户端请求体JSON 格式 错误 字段类型 不匹配
     * @param e
     * @param  request
     * @return
     * @author Long
     * @date: 2022/6/9 14:14
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public  ResponseData<Object> handle(HttpMessageNotReadableException e, HttpServletRequest request){
        ExceptionResult.ExceptionResultBuilder result = ExceptionResult.builder();
        log.error("[客户端请求体JSON 格式 错误 字段类型 不匹配]",e);
        result =  ExceptionResult.builder()
                .timestamp(new Date())
                .message(e.getMessage())
                //      .trace(trace)
                .exceptionName(e.getClass().getName())
                .path(request.getRequestURI());
        return ResponseData.error(ErrorCodeEnum.CLIENT_REQUEST_BODY_FORMAT_ERROR,result.build());
    }

    /**
     * @description: 1004 客户端URL 中 的参数类型错误
     * @param e
     * @param request
     * @return
     * @author Long
     * @date: 2022/6/9 14:16
     */
    @ExceptionHandler(BindException.class)
    public  ResponseData<Object> handle(BindException e, HttpServletRequest request){
        ExceptionResult.ExceptionResultBuilder result = ExceptionResult.builder();
        log.error("[客户端URL 中 的参数类型错误]",e);
        result =  ExceptionResult.builder()
                .timestamp(new Date())
                .message(e.getMessage())
                //      .trace(trace)
                .exceptionName(e.getClass().getName())
                .path(request.getRequestURI());
        return ResponseData.error(ErrorCodeEnum.CLIENT_PATH_VARIABLE_ERROR,result.build());
    }

    /** 
     * @description: 1005 客户端请求参数效验不通过
     * @param e
     * @param request
     * @return
     * @author Long
     * @date: 2022/6/9 14:18
     */ 
    @ExceptionHandler(ConstraintViolationException.class)
    public  ResponseData<Object> handle(ConstraintViolationException e, HttpServletRequest request){
        ExceptionResult.ExceptionResultBuilder result = ExceptionResult.builder();
        log.error("[客户端请求参数效验不通过]",e);
        result =  ExceptionResult.builder()
                .timestamp(new Date())
                .message(e.getMessage())
                //      .trace(trace)
                .exceptionName(e.getClass().getName())
                .path(request.getRequestURI());
        return ResponseData.error(ErrorCodeEnum.CLIENT_PATH_VARIABLE_ERROR,result.build());
    }

    /** 
     * @description: 1006  客户端请求缺少必填字段
     * @param e
     * @param request
     * @return
     * @author Long
     * @date: 2022/6/9 14:20
     */ 
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public  ResponseData<Object> handle(MissingServletRequestParameterException e, HttpServletRequest request){
        ExceptionResult.ExceptionResultBuilder result = ExceptionResult.builder();
        log.error("[客户端请求缺少必填字段]",e);
        String errorMsg = null;
        String parameterName = e.getParameterName();
        if (!"".equals(parameterName)) {
            errorMsg = parameterName + "不能为空";
        }
        result =  ExceptionResult.builder()
                .respMsg(errorMsg)
                .timestamp(new Date())
                .message(e.getMessage())
                //      .trace(trace)
                .exceptionName(e.getClass().getName())
                .path(request.getRequestURI());
        return ResponseData.error(ErrorCodeEnum.CLIENT_REQUEST_PARAM_REQUIRED_ERROR,result.build());
    }
    
    /** 
     * @description: 业务方法参数检查不通过
     * @param e
     * @param request
     * @return
     * @author Long
     * @date: 2022/6/9 14:22
     */ 

    @ExceptionHandler(IllegalArgumentException.class)
    public  ResponseData<Object> handle(IllegalArgumentException e, HttpServletRequest request){
        ExceptionResult.ExceptionResultBuilder result = ExceptionResult.builder();
        log.error("[业务方法参数检查不通过]",e);
     
        result =  ExceptionResult.builder()
                .timestamp(new Date())
                .message(e.getMessage())
                //      .trace(trace)
                .exceptionName(e.getClass().getName())
                .path(request.getRequestURI());
        return ResponseData.error(ErrorCodeEnum.SERVER_ILLEGAL_ARGUMENT_ERROR,result.build());
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
    public ResponseData<Object> handle(ServiceException ex, HttpServletRequest request){

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
