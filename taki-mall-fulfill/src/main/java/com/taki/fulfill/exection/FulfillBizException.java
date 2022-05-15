package com.taki.fulfill.exection;

import com.taki.common.exception.BaseErrorCodeEnum;
import com.taki.common.exception.ServiceException;

/**
 * @ClassName FulfillBizException
 * @Description TODO
 * @Author Long
 * @Date 2022/5/15 18:24
 * @Version 1.0
 */
public class FulfillBizException extends ServiceException {
    public FulfillBizException(String message) {
        super(message);
    }

    public FulfillBizException(Integer code, String message) {
        super(code, message);
    }

    public FulfillBizException(BaseErrorCodeEnum errorEnum) {
        super(errorEnum);
    }

    public FulfillBizException(BaseErrorCodeEnum errorEnum, Object data) {
        super(errorEnum, data);
    }

    public FulfillBizException(BaseErrorCodeEnum errorCodeEnum, String message, Object data) {
        super(errorCodeEnum, message, data);
    }

    public FulfillBizException(Integer errorCode, String message, Object data) {
        super(errorCode, message, data);
    }
}
