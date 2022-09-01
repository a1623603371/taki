package com.taki.enums;

/**
 * @ClassName ThreadWayEnum
 * @Description 线程模型 枚举
 * @Author Long
 * @Date 2022/9/1 22:24
 * @Version 1.0
 */
public enum ThreadWayEnum {
    /**
     * 异步执行
     */
    ASYNC(1,"异步执行"),
    SYNC(2,"同步执行");


    ThreadWayEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;

    private final String desc;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
