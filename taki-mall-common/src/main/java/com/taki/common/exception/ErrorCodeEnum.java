package com.taki.common.exception;

/**
 * @ClassName CodeEnum
 * @Description 状态码
 * @Author Long
 * @Date 2021/11/24 17:23
 * @Version 1.0
 */
public enum ErrorCodeEnum implements BaseErrorCodeEnum {

    /**
     * 系统未知错误
     */
    SYSTEM_UNKNOWN_ERROR(-1,"系统未知错误"),

    // ======客户端异常
    /**
     *客户端HTTP 请求方法错误
     * org.springframework.web.HttpRequestMethodNotSupportedException
     */
    CLIENT_HTTP_METHOD_ERROR(1001,"客户端HTTP请求方法错误"),
    /**
     * 客户端 request body 参数错误
     * 未能通过Hibernamte 校验的异常处理
     *org.springframework.web.bind.MethodArgumentNotValidException
     */
    CLIENT_REQUEST_BODY_CHECK_ERROR(1002,"客户端请求体JSON格式错误或字段类型不匹配"),


    /**
     * 客户端@RequestBody请求体JSON格式错误或者字段类型错误
     *
     * org.springframework.http.converter.HttpMessageNotReadableException
     *
     *  1、参数类型不对:{"test":"abc"}，本身类型是Long
     *  2、{"test":}  test属性没有给值
     */
    CLIENT_REQUEST_BODY_FORMAT_ERROR(1003,"客户端请求体JSON格式错误字段类型不匹配"),

    /**
     * 客户端@PathVariable 参数错误
     *
     * 一般类型不匹配 ，比如 long类型，客户端给了一个无法转换的 Long字符串
     *org.springframework.validation.BindException
     */
    CLIENT_PATH_VARIABLE_ERROR(1004,"客户端URL中参数错误"),

    /**
     * 客户端 @RequestParm参数不通过
     * 主要是未能通过Hibernate Validator校验的异常处理
     * javax.validation.ConstraintViolationException
     */
    CLIENT_REQUEST_PARAM_CHECK_ERROR(1005,"客户端校验不通过"),

    /**
     * 客户端@RequestParam参数必填
     * 入参中的@RequestParam注解设置了必填，但是客户端没有给值
     * javax.validation.ConstraintViolationException
     */
    CLIENT_REQUEST_PARAM_REQUIRED_ERROR(1006, "客户端请求缺少必填的参数"),

    /**
     * 通用的业务方法入参检查错误
     * java.lang.IllegalArgumentException
     */
    SERVER_ILLEGAL_ARGUMENT_ERROR(2001, "业务方法参数检查不通过"),

    SYSTEM_ERROR(500,"服务系统异常，请稍等"),

    PARAMETER_ERROR(550,"参数异常,认证失败"),


    PASS_NOT_SAFE(541,"密码请设置6-18位的字母数字组合，需含字母和数字"),

    ERROR_REMOTE_SERVER(501,"远程API调用失败"),

    BUSINESS_ERROR(540,"业务报错"),

    USER_HAS_ERROR(530,"用户名已存在");




    private int errorCode;

    private String errorMessage;

    ErrorCodeEnum(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getCode() {
        return errorCode;
    }

    public void setCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return errorMessage;
    }

    public void setMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public Integer getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMsg() {
        return errorMessage;
    }
}
