package com.taki.core.error;

import com.taki.core.enums.CodeEnum;
import lombok.Data;

/**
 * @ClassName ServiceException
 * @Description 业务异常
 * @Author Long
 * @Date 2021/11/24 21:50
 * @Version 1.0
 */
@Data
public class ServiceException extends Exception{

   // private int httpStatus = 550;

    private int code = 550;

    private String message;

    private Object data;

    public ServiceException(CodeEnum errorEnum, Object data) {
      //  this.httpStatus = errorEnum.getHttpStatus();
        this.code = errorEnum.getCode();
        this.message = errorEnum.getMessage();
        this.data = data;
    }

    public ServiceException(CodeEnum errorEnum, String message, Object data) {
      //  this.httpStatus = errorEnum.getHttpStatus();
        this.code = errorEnum.getCode();
        this.message = message;
        this.data = data;
    }
}
