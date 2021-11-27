package com.taki.core.error;

import com.taki.core.enums.CodeEnum;
import com.taki.core.utlis.ExceptionUtils;
import com.taki.core.utlis.ResponseData;
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
     * @description: 处理异常信息
     * @param: ex
        request
     * @return: java.lang.Object
     * @author Long
     * @date: 2021/11/24 21:42
     */
    @ExceptionHandler(Exception.class)
    //@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseData<Object>  exceptionResponse(Exception ex, HttpServletRequest request){

        ExceptionResult.ExceptionResultBuilder result = ExceptionResult.builder();
        String trace =  ExceptionUtils.getStackTrace(ex);

        log.info(ex.getMessage());
        log.error(trace);
        if (ex instanceof NullPointerException){
            result.message("空指针异常");
        }
        ExceptionResult.builder().timestamp(new Date())
                .message(ex.getMessage())
                .trace(trace)
                .exceptionName(ex.getClass().getName())
                .path(request.getRequestURI());

        return ResponseData.error(CodeEnum.SYSTEM_ERROR,result.build());
    }

    @ExceptionHandler(ServiceException.class)
    //@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseData<Object> exceptionResponse(ServiceException ex, HttpServletRequest request){

        String trace =  ExceptionUtils.getStackTrace(ex);

        log.info(ex.getMessage());
        log.error(trace);
        ExceptionResult result = ExceptionResult.builder().timestamp(new Date())
                .message(ex.getMessage())
                .trace(trace)
                .exceptionName(ex.getClass().getName())
                .path(request.getRequestURI()).build();
        return  ResponseData.error(CodeEnum.SYSTEM_ERROR,result);
    }

}
