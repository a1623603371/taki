package com.taki.market.exception;

import com.taki.common.exception.BaseErrorCodeEnum;
import com.taki.common.exception.ServiceException;

/**
 * @ClassName MarketBizException
 * @Description
 * @Author Long
 * @Date 2022/2/18 14:32
 * @Version 1.0
 */
public class MarketBizException extends ServiceException {
    public MarketBizException(String message) {
        super(message);
    }

    public MarketBizException(Integer code, String message) {
        super(code, message);
    }

    public MarketBizException(BaseErrorCodeEnum errorEnum) {
        super(errorEnum);
    }

    public MarketBizException(BaseErrorCodeEnum errorEnum, Object data) {
        super(errorEnum, data);
    }

    public MarketBizException(BaseErrorCodeEnum errorCodeEnum, String message, Object data) {
        super(errorCodeEnum, message, data);
    }

    public MarketBizException(Integer errorCode, String message, Object data) {
        super(errorCode, message, data);
    }
}
