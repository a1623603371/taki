package com.taki.common.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CustomerAuditResult
 * @Description 客服审核
 * @Author Long
 * @Date 2022/5/18 20:45
 * @Version 1.0
 */
@Getter
public enum CustomerAuditResult {

    ACCEPT(1,"客服审核通过"),
    REJECT(2,"客服审核拒绝");

    CustomerAuditResult(Integer code, String msg) {
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
