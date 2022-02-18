package com.taki.product.exception;

import com.taki.common.exception.BaseErrorCodeEnum;
import com.taki.common.exception.ServiceException;

/**
 * @ClassName ProductBizException
 * @Description TODO
 * @Author Long
 * @Date 2022/2/17 16:15
 * @Version 1.0
 */
public class ProductBizException extends ServiceException {
    public ProductBizException(String message) {
        super(message);
    }

    public ProductBizException(Integer code, String message) {
        super(code, message);
    }

    public ProductBizException(BaseErrorCodeEnum errorEnum) {
        super(errorEnum);
    }

    public ProductBizException(BaseErrorCodeEnum errorEnum, Object data) {
        super(errorEnum, data);
    }

    public ProductBizException(BaseErrorCodeEnum errorCodeEnum, String message, Object data) {
        super(errorCodeEnum, message, data);
    }

    public ProductBizException(Integer errorCode, String message, Object data) {
        super(errorCode, message, data);
    }
}
