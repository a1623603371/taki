package com.taki.common.enums;

import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName DeleteStatusEnum
 * @Description DO 删除 状态
 * @Author Long
 * @Date 2022/5/18 20:49
 * @Version 1.0
 */
@Getter
public enum DeleteStatusEnum {
    NO(0,"未删除"),
    YES(1,"已删除");

    DeleteStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private  Integer code;

    private String msg;

    public static Map<Integer, String> toMap(){
        Map<Integer, String> map = new HashMap<>(16);
        for (DeleteStatusEnum element : DeleteStatusEnum.values() ){
            map.put(element.getCode(),element.getMsg());
        }
        return map;
    }

    public static DeleteStatusEnum getByCode(Integer code){
        for(DeleteStatusEnum element : DeleteStatusEnum.values()){
            if (code.equals(element.getCode())) {
                return element;
            }
        }
        return null;
    }
}
