package com.taki.common.exception;

/**
 * @ClassName BaseErrorCodeEnum
 * @Description 异常错误码枚举抽象定义
 * @Author Long
 * @Date 2021/12/13 15:19
 * @Version 1.0
 */
public interface BaseErrorCodeEnum {

    /**
     * @description:  获取 错误状态码
     * @param:
     * @return: 状态码
     * @author Long
     * @date: 2021/12/13 16:29
     */
    Integer getErrorCode();

    /**
     * @description:  获取错误信息
     * @param:
     * @return: 错误信息
     * @author Long
     * @date: 2021/12/13 16:29
     */
    String getErrorMsg();


}
