package com.taki.order.enums;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName AfterSaleStatusEnum
 * @Description 售后状态
 * @Author Long
 * @Date 2022/3/11 11:06
 * @Version 1.0
 */
public enum AfterSaleStatusEnum {

    UN_CREATE(0,"未创建"),
    COMMITED(10,"提交申请"),
    REVIEW_PASS(20,"审核通过"),
    REVIEW_REJECTED(30,"审核拒绝"),
    REFUNDING(40,"退款中"),
    REFUNDED(50,"退款成功"),
    FAILED(60,"退款失败"),
    CLOSED(70,"已关闭"),
    REOPEN(100,"重新提交申请"),
    REVOKE(127,"撤销成功");


    private Integer code;

    private String msg;

    AfterSaleStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static Map<Integer,String> toMap() {
        Map<Integer,String> map = new HashMap<>(16);
        for (AfterSaleStatusEnum element : AfterSaleStatusEnum.values() ){
            map.put(element.getCode(),element.getMsg());
        }
        return map;
    }

    public static AfterSaleStatusEnum getByCode(Integer code) {
        for(AfterSaleStatusEnum element : AfterSaleStatusEnum.values()){
            if (code.equals(element.getCode())) {
                return element;
            }
        }
        return null;
    }

    public static Set<Integer> allowableValues() {
        Set<Integer> allowableValues = new HashSet<>(values().length);
        for (AfterSaleStatusEnum statusEnum : values()) {
            allowableValues.add(statusEnum.getCode());
        }
        return allowableValues;
    }

}
