package com.taki.careerplan.cookbook.exception;

import com.taki.common.exception.BaseErrorCodeEnum;
import com.taki.common.exception.ServiceException;

/**
 * @ClassName AddressException
 * @Description 用户收货地址异常处理
 * @Author Long
 * @Date 2022/7/31 16:39
 * @Version 1.0
 */
public class CookBookException extends ServiceException {
    public CookBookException(String message) {
        super(message);
    }

    public CookBookException(Integer code, String message) {
        super(code, message);
    }

    public CookBookException(BaseErrorCodeEnum errorEnum) {
        super(errorEnum);
    }

    public CookBookException(BaseErrorCodeEnum errorEnum, Object data) {
        super(errorEnum, data);
    }

    public CookBookException(BaseErrorCodeEnum errorCodeEnum, String message, Object data) {
        super(errorCodeEnum, message, data);
    }

    public CookBookException(Integer errorCode, String message, Object data) {
        super(errorCode, message, data);
    }
}
