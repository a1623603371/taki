package com.taki.wms.exception;

import com.taki.common.exception.BaseErrorCodeEnum;
import com.taki.common.exception.ServiceException;

/**
 * @ClassName WmsBizException
 * @Description TODO
 * @Author Long
 * @Date 2022/5/17 14:03
 * @Version 1.0
 */
public class WmsBizException  extends ServiceException {

    public WmsBizException(String message) {
        super(message);
    }

    public WmsBizException(Integer code, String message) {
        super(code, message);
    }

    public WmsBizException(BaseErrorCodeEnum errorEnum) {
        super(errorEnum);
    }

    public WmsBizException(BaseErrorCodeEnum errorEnum, Object data) {
        super(errorEnum, data);
    }

    public WmsBizException(BaseErrorCodeEnum errorCodeEnum, String message, Object data) {
        super(errorCodeEnum, message, data);
    }

    public WmsBizException(Integer errorCode, String message, Object data) {
        super(errorCode, message, data);
    }
}
