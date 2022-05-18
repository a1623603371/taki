package com.taki.pay.exception;

import com.taki.common.exception.BaseErrorCodeEnum;
import com.taki.common.exception.ServiceException;

/**
 * @ClassName PayBizException
 * @Description 支付异常
 * @Author Long
 * @Date 2022/5/18 15:55
 * @Version 1.0
 */
public class PayBizException extends ServiceException {
    public PayBizException(String message) {
        super(message);
    }

    public PayBizException(Integer code, String message) {
        super(code, message);
    }

    public PayBizException(BaseErrorCodeEnum errorEnum) {
        super(errorEnum);
    }

    public PayBizException(BaseErrorCodeEnum errorEnum, Object data) {
        super(errorEnum, data);
    }

    public PayBizException(BaseErrorCodeEnum errorCodeEnum, String message, Object data) {
        super(errorCodeEnum, message, data);
    }

    public PayBizException(Integer errorCode, String message, Object data) {
        super(errorCode, message, data);
    }
}
