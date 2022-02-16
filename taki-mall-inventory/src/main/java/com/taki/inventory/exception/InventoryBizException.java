package com.taki.inventory.exception;

import com.taki.common.exception.BaseErrorCodeEnum;
import com.taki.common.exception.ServiceException;

/**
 * @ClassName InventoryBizException
 * @Description 库存服务自定义业务异常
 * @Author Long
 * @Date 2022/2/16 17:10
 * @Version 1.0
 */
public class InventoryBizException extends ServiceException {


    public InventoryBizException(String message) {
        super(message);
    }

    public InventoryBizException(Integer code, String message) {
        super(code, message);
    }

    public InventoryBizException(BaseErrorCodeEnum errorEnum) {
        super(errorEnum);
    }

    public InventoryBizException(BaseErrorCodeEnum errorEnum, Object data) {
        super(errorEnum, data);
    }

    public InventoryBizException(BaseErrorCodeEnum errorCodeEnum, String message, Object data) {
        super(errorCodeEnum, message, data);
    }

    public InventoryBizException(Integer errorCode, String message, Object data) {
        super(errorCode, message, data);
    }
}
