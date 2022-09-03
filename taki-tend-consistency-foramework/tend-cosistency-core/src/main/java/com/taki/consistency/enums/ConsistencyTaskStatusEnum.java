package com.taki.consistency.enums;

/**
 * @ClassName ConsistencyTaskStatusEnum
 * @Description 一致性任务状态枚举类
 * @Author Long
 * @Date 2022/9/1 22:18
 * @Version 1.0
 */
public enum ConsistencyTaskStatusEnum {
    /**
     * 0 初始化 ，1开始执行 ， 2 执行失败 ， 3 执行成功
     * */
    INIT(0),
    START(1), // 这个状态为中间状态，为了防止在执行过程中，程序蹦了没有 改成下个 fail 或 success 状态
    FAIL(2),
    SUCCESS(3);

    ConsistencyTaskStatusEnum(int code) {
        this.code = code;
    }

    private final int code;

    public int getCode() {
        return code;
    }
}
