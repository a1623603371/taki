package com.taki.order.enums;

import com.taki.common.exception.BaseErrorCodeEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName BusinessIdentifierEnum
 * @Description 业务线 枚举类
 * @Author Long
 * @Date 2022/1/3 15:09
 * @Version 1.0
 */
@Getter
public enum BusinessIdentifierEnum {
    SELF_MALL(1,"自营商城");


    private Integer code;

    private String msg;

     BusinessIdentifierEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public static Map<Integer,String> toMap (){
         return BusinessIdentifierEnum.toMap();

    }


    public static BusinessIdentifierEnum getByCode(Integer code){
        for (BusinessIdentifierEnum element : BusinessIdentifierEnum.values()) {

            if (code.equals(element)){
                return element;
            }
        }
        return  null;
    }


    public static Set<Integer> allowableValues(){
        Set<Integer> allowableValues = new HashSet<>(values().length);

        for (BusinessIdentifierEnum businessIdentifierEnum : BusinessIdentifierEnum.values()) {

                allowableValues.add(businessIdentifierEnum.getCode());
        }
        return allowableValues;
    }
}
