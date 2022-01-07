package com.taki.order.enums;

import lombok.Getter;

/**
 * @ClassName SnapshotTypeEnum
 * @Description 快照類型
 * @Author Long
 * @Date 2022/1/7 13:49
 * @Version 1.0
 */
@Getter
public enum SnapshotTypeEnum {

    ORDER_COUPON(1,"訂單優惠券信息"),
    ORDER_AMOUNT(2,"訂單費用信息"),
    ORDER_ITEM(3,"訂單條目");



    private  Integer code;


    private String msg;

    SnapshotTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
