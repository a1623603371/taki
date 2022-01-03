package com.taki.order.enums;

import com.taki.common.enums.AmountTypeEnum;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AmountTypeEnum
 * @Description 金额类型
 * @Author Long
 * @Date 2022/1/3 16:27
 * @Version 1.0
 */
@Getter
public enum AccountTypeEnum {
    THIRD(1,"第三方"),
    OTHER(127,"其他");


    private Integer code;

    private String msg;


    AccountTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    /**
     * 生成 map
     * @return
     */
    public  static Map<Integer,String> toMap(){
        Map<Integer,String> map = new HashMap<>();
        for (AccountTypeEnum element : AccountTypeEnum.values()) {
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
    public static AccountTypeEnum getByCode (Integer code){

        for (AccountTypeEnum element : AccountTypeEnum.values()) {

            if (element.equals(code)){
                return element;
            }


        }
        return null;
    }
}
