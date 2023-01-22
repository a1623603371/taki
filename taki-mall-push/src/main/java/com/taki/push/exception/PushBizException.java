package com.taki.push.exception;

import com.taki.common.exception.BaseErrorCodeEnum;
import com.taki.common.exception.ServiceException;

/**
 * @ClassName ProductBizException
 * @Description TODO
 * @Author Long
 * @Date 2022/2/17 16:15
 * @Version 1.0
 */
public class PushBizException extends ServiceException {

    public PushBizException(String message) {
        super(message);
    }

    public PushBizException(Integer code, String message) {
        super(code, message);
    }

    public PushBizException(BaseErrorCodeEnum errorEnum) {
        super(errorEnum);
    }

    public PushBizException(BaseErrorCodeEnum errorEnum, Object data) {
        super(errorEnum, data);
    }

    public PushBizException(BaseErrorCodeEnum errorCodeEnum, String message, Object data) {
        super(errorCodeEnum, message, data);
    }

    public PushBizException(Integer errorCode, String message, Object data) {
        super(errorCode, message, data);
    }
}
