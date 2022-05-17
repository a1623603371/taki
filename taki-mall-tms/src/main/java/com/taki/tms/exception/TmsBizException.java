package com.taki.tms.exception;

import com.taki.common.exception.BaseErrorCodeEnum;
import com.taki.common.exception.ServiceException;

/**
 * @ClassName TmsBizException
 * @Description tms
 * @Author Long
 * @Date 2022/5/17 15:07
 * @Version 1.0
 */
public class TmsBizException extends ServiceException {
    public TmsBizException(String message) {
        super(message);
    }

    public TmsBizException(Integer code, String message) {
        super(code, message);
    }

    public TmsBizException(BaseErrorCodeEnum errorEnum) {
        super(errorEnum);
    }

    public TmsBizException(BaseErrorCodeEnum errorEnum, Object data) {
        super(errorEnum, data);
    }

    public TmsBizException(BaseErrorCodeEnum errorCodeEnum, String message, Object data) {
        super(errorCodeEnum, message, data);
    }

    public TmsBizException(Integer errorCode, String message, Object data) {
        super(errorCode, message, data);
    }
}
