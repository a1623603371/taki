package com.taki.order.enums;

import lombok.Getter;

/**
 * @ClassName CommentStatusEnum
 * @Description 评论状态枚举
 * @Author Long
 * @Date 2022/1/7 9:40
 * @Version 1.0
 */
@Getter
public enum CommentStatusEnum {

    NO(0,"未评论"),
        YES(1,"已评论");

    private Integer code;

    private String msg;

     CommentStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
