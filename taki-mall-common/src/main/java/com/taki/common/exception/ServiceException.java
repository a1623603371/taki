package com.taki.common.exception;

import lombok.Data;

/**
 * @ClassName ServiceException
 * @Description 自定义业务异常
 * @Author Long
 * @Date 2021/11/24 21:50
 * @Version 1.0
 */
@Data
public class ServiceException extends RuntimeException{

   // private int httpStatus = 550;
    /**
     * 默认错误码
     */
    private static  final Integer DEFAULT_ERROR_CODE = -1;

    /**
     * 错误码
     */
    private Integer errorCode ;
    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 业务数据
     */
    private Object data;

    public ServiceException(String message, Integer code) {
        super(message);
        this.errorCode = code;
        this.errorMessage = message;
    }

    public ServiceException(ErrorCodeEnum errorEnum) {
        super(errorEnum.getMessage());
        this.errorCode = errorEnum.getCode();
        this.errorMessage = errorEnum.getMessage();
    }

    public ServiceException(ErrorCodeEnum errorEnum, Object data) {
        super(errorEnum.getMessage());
        this.errorCode = errorEnum.getCode();
        this.errorMessage = errorEnum.getMessage();
        this.data = data;
    }

    public ServiceException(ErrorCodeEnum errorEnum, String message, Object data) {
        super(message);
        this.errorCode = errorEnum.getCode();
        this.errorMessage = message;
        this.data = data;
    }
}
