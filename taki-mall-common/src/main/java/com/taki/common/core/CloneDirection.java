package com.taki.common.core;

/**
 * @ClassName CloneDirection
 * @Description 克隆方向枚举
 * @Author Long
 * @Date 2021/12/14 14:21
 * @Version 1.0
 */
public enum CloneDirection {

    FORWARD(1),
    OPPOSITE(2);

    private Integer code;

    CloneDirection(Integer code) {
        this.code = code;
    }
}
