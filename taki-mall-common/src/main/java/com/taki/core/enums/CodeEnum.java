package com.taki.core.enums;

/**
 * @ClassName CodeEnum
 * @Description 状态码
 * @Author Long
 * @Date 2021/11/24 17:23
 * @Version 1.0
 */
public enum CodeEnum {
    //成功状态码
    SUCCESS(200,"ok"),

    SYSTEM_ERROR(500,"服务系统异常，请稍等"),

    PARAMETER_ERROR(550,"参数异常,认证失败"),

    USER_HAS_ERROR(530,"用户名已存在");


    private int code;

    private String message;

    CodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
