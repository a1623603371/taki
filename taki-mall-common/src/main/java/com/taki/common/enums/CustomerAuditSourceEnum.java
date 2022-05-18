package com.taki.common.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CustomerAuditSourceEnum
 * @Description 客服审核 来源
 * @Author Long
 * @Date 2022/5/18 20:48
 * @Version 1.0
 */
@Getter
public enum CustomerAuditSourceEnum {
    SELF_MALL(1,"自营商城");


    CustomerAuditSourceEnum(Integer code, String msg) {
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
