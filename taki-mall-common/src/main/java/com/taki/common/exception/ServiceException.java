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

    public ServiceException(String message) {
        super(message);
        this.errorCode = DEFAULT_ERROR_CODE;
        this.errorMessage = message;
    }

    public ServiceException(Integer code,String message) {
        super(message);
        this.errorCode = code;
        this.errorMessage = message;
    }

    public ServiceException(BaseErrorCodeEnum errorEnum) {
        super(errorEnum.getErrorMsg());
        this.errorCode = errorEnum.getErrorCode();
        this.errorMessage = errorEnum.getErrorMsg();
    }

    public ServiceException(BaseErrorCodeEnum errorEnum, Object data) {
        super(errorEnum.getErrorMsg());
        this.errorCode = errorEnum.getErrorCode();
        this.errorMessage = errorEnum.getErrorMsg();
        this.data = data;
    }

    public ServiceException(BaseErrorCodeEnum errorCodeEnum, String message, Object data) {
        super(message);
        this.errorCode = errorCodeEnum.getErrorCode();
        this.errorMessage = message;
        this.data = data;
    }
    public ServiceException(Integer errorCode, String message, Object data) {
        super(message);
        this.errorCode = errorCode;
        this.errorMessage = message;
        this.data = data;
    }

}
