package com.taki.common.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName PayTypeEnum
 * @Description 支付类型 枚举
 * @Author Long
 * @Date 2022/1/3 17:04
 * @Version 1.0
 */
@Getter
public enum PayTypeEnum {

    WECHAT_TYPE(10,"微信支付"),
    ALI_TYPE(20,"支付宝支付");


    private Integer code;

    private String msg;


    PayTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    /**
     * 生成 map
     * @return
     */
    public  static Map<Integer,String> toMap(){
        Map<Integer,String> map = new HashMap<>();
        for (PayTypeEnum element : PayTypeEnum.values()) {
            map.put(element.getCode(), element.getMsg());
        }

        return map;
    }

    /**
     * @description: 根据code放回订单号 方向
     * @param code 订单方向
     * @return  订单号类型
     * @author Long
     * @date: 2022/1/2 22:12
     */
    public static PayTypeEnum getByCode (Integer code){

        for (PayTypeEnum element : PayTypeEnum.values()) {

            if (element.getCode().equals(code)){
                return element;
            }


        }
        return null;
    }
}
