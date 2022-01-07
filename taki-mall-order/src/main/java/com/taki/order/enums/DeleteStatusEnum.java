package com.taki.order.enums;

import lombok.Getter;

/**
 * @ClassName DeleteStatusEnum
 * @Description 删除状态枚举
 * @Author Long
 * @Date 2022/1/7 9:37
 * @Version 1.0
 */
@Getter
public enum DeleteStatusEnum {

    NO(0,"未删除"),
        YES(1,"已删除");


    private Integer code;

    private String msg;

     DeleteStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
