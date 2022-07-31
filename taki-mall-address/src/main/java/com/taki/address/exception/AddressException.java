package com.taki.address.exception;

import com.taki.common.exception.BaseErrorCodeEnum;
import com.taki.common.exception.ServiceException;

/**
 * @ClassName AddressException
 * @Description 用户收货地址异常处理
 * @Author Long
 * @Date 2022/7/31 16:39
 * @Version 1.0
 */
public class AddressException extends ServiceException {
    public AddressException(String message) {
        super(message);
    }

    public AddressException(Integer code, String message) {
        super(code, message);
    }

    public AddressException(BaseErrorCodeEnum errorEnum) {
        super(errorEnum);
    }

    public AddressException(BaseErrorCodeEnum errorEnum, Object data) {
        super(errorEnum, data);
    }

    public AddressException(BaseErrorCodeEnum errorCodeEnum, String message, Object data) {
        super(errorCodeEnum, message, data);
    }

    public AddressException(Integer errorCode, String message, Object data) {
        super(errorCode, message, data);
    }
}
