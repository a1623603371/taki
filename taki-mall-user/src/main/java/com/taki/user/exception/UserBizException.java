package com.taki.user.exception;

import com.taki.common.exception.BaseErrorCodeEnum;
import com.taki.common.exception.ServiceException;

/**
 * @ClassName TmsBizException
 * @Description tms
 * @Author Long
 * @Date 2022/5/17 15:07
 * @Version 1.0
 */
public class UserBizException extends ServiceException {
    public UserBizException(String message) {
        super(message);
    }

    public UserBizException(Integer code, String message) {
        super(code, message);
    }

    public UserBizException(BaseErrorCodeEnum errorEnum) {
        super(errorEnum);
    }

    public UserBizException(BaseErrorCodeEnum errorEnum, Object data) {
        super(errorEnum, data);
    }

    public UserBizException(BaseErrorCodeEnum errorCodeEnum, String message, Object data) {
        super(errorCodeEnum, message, data);
    }

    public UserBizException(Integer errorCode, String message, Object data) {
        super(errorCode, message, data);
    }
}
