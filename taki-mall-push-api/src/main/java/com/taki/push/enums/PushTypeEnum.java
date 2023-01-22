package com.taki.push.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName PushTypeEnum
 * @Description 推送类型
 * @Author Long
 * @Date 2022/10/5 15:48
 * @Version 1.0
 */
@Getter
public enum PushTypeEnum {
   DELAY(1,"定时推送"),
    INSTANT(2,"实时推送");


    private Integer code;

    private String msg;

    PushTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Map<Integer,String> toMap (){

        Map<Integer,String> map = new HashMap<>(16);

        for (PushTypeEnum pushType : PushTypeEnum.values()) {
            map.put(pushType.getCode(),pushType.getMsg());
        }

        return map;

    }


    public static  PushTypeEnum getByCode(Integer code){

        for (PushTypeEnum pushType : PushTypeEnum.values()) {

            if (pushType.getCode().equals(code)){
                return pushType;
            }

        }

        return null;

    }


    public static Set<Integer> allowableValues(){
        Set<Integer> allowableValues = new HashSet<>(values().length);

        for (PushTypeEnum pushType : values()) {

            allowableValues.add(pushType.getCode());

        }

        return allowableValues;
    }
}
