package com.taki.order.enums;

import lombok.Getter;

/**
 * @ClassName PayStatusEnum
 * @Description 支付状态枚举
 * @Author Long
 * @Date 2022/1/7 10:51
 * @Version 1.0
 */
@Getter
public enum PayStatusEnum {

    UNPAID(10,"未支付"),
    PAID(20,"已支付");

    private Integer code;

    private String smg;

    PayStatusEnum(Integer code, String smg) {
        this.code = code;
        this.smg = smg;
    }
}
