package com.taki.push.enums;

import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName InformTypeEnum
 * @Description 通知类型
 * @Author Long
 * @Date 2022/10/5 15:37
 * @Version 1.0
 */
@Getter
public enum InformTypeEnum {

    /**
     * 短信
     */
    SMS(1,"短信"),


    APP(2,"APP"),

    EMAIL(3,"邮箱");

    private Integer code;

    private String msg;

    InformTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public static Map<Integer,String> toMap (){

        Map<Integer,String> map = new HashMap<>(16);

        for (InformTypeEnum inform : InformTypeEnum.values()) {
            map.put(inform.getCode(),inform.getMsg());
        }

        return map;

    }


    public static  InformTypeEnum getByCode(Integer code){

        for (InformTypeEnum inform : InformTypeEnum.values()) {

            if (inform.getCode().equals(code)){
                return inform;
            }

        }

        return null;

    }


    public static Set<Integer> allowableValues(){
    Set<Integer> allowableValues = new HashSet<>(values().length);

        for (InformTypeEnum inform : values()) {

            allowableValues.add(inform.getCode());
            
        }

        return allowableValues;
    }
}
