package com.taki.customer.exception;

import com.taki.common.exception.BaseErrorCodeEnum;
import com.taki.common.exception.ServiceException;

/**
 * @ClassName CustomerBizException
 * @Description TODO
 * @Author Long
 * @Date 2022/7/30 20:07
 * @Version 1.0
 */
public class CustomerBizException extends ServiceException {
    public CustomerBizException(String message) {
        super(message);
    }

    public CustomerBizException(Integer code, String message) {
        super(code, message);
    }

    public CustomerBizException(BaseErrorCodeEnum errorEnum) {
        super(errorEnum);
    }

    public CustomerBizException(BaseErrorCodeEnum errorEnum, Object data) {
        super(errorEnum, data);
    }

    public CustomerBizException(BaseErrorCodeEnum errorCodeEnum, String message, Object data) {
        super(errorCodeEnum, message, data);
    }

    public CustomerBizException(Integer errorCode, String message, Object data) {
        super(errorCode, message, data);
    }
}
