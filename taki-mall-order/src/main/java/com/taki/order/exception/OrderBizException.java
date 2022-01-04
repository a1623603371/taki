package com.taki.order.exception;

import com.taki.common.exception.BaseErrorCodeEnum;
import com.taki.common.exception.ErrorCodeEnum;
import com.taki.common.exception.ServiceException;

/**
 * @ClassName OrdeerBizException
 * @Description TODO
 * @Author Long
 * @Date 2022/1/4 10:42
 * @Version 1.0
 */
public class OrderBizException extends ServiceException {


    public OrderBizException(String message) {
        super(message);
    }

    public OrderBizException(Integer code, String message) {
        super(code,message);
    }

    public OrderBizException(BaseErrorCodeEnum errorEnum) {
        super(errorEnum);
    }

    public OrderBizException(BaseErrorCodeEnum errorEnum, Object data) {
        super(errorEnum, data);
    }

    public OrderBizException(BaseErrorCodeEnum errorCodeEnum, String message, Object data) {
        super(errorCodeEnum, message, data);
    }

    public OrderBizException(Integer errorCode, String message, Object data) {
        super(errorCode, message, data);
    }
}
