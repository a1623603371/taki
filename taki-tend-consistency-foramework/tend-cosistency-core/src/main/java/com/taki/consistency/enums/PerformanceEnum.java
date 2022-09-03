package com.taki.consistency.enums;

/**
 * @ClassName PerformanceEnum
 * @Description 执行模式枚举
 * @Author Long
 * @Date 2022/8/31 23:22
 * @Version 1.0
 */
public enum PerformanceEnum {

    PERFORMANCE_RIGHT_NOW(1,"立即执行"),PERFORMANCE_SCHEDULE(2,"调度执行");


    private final Integer code;

    private final String msg;

    PerformanceEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
