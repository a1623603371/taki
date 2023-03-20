package com.taki.common.enums;

import lombok.Getter;

/**
 * @ClassName BiglogTypeEnum
 * @Description TODO
 * @Author Long
 * @Date 2023/3/5 18:45
 * @Version 1.0
 */
@Getter
public enum BinlogTypeEnum {
    INSERT("新增","INSERT"),
    UPDATE("修改","UPDATE"),
    DELETE("删除","DELETE");


    BinlogTypeEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    private final String name;

    private final String value;

    /*** 
     * @description: 根据枚举类型取得枚举类型
     * @param typeValue 枚举类型的值
     * @return  com.taki.common.enums.BinlogTypeEnum
     * @author Long
     * @date: 2023/3/5 18:49
     */ 

    public static BinlogTypeEnum finByValue(String typeValue){
        for (BinlogTypeEnum binlogType : values()) {
            if (binlogType.getValue().equals(typeValue)){
                return binlogType;
            }
        }
        return null;
    }

    /*** 
     * @description:  根据枚举类型 的值取得类型的名称
     * @param d
     * @return  java.lang.String
     * @author Long
     * @date: 2023/3/5 18:52
     */ 

    public static String getNameByValue(String  typeValue){

        for (BinlogTypeEnum binlogType : values()) {

            if (binlogType.getValue().equals(typeValue)){
                return binlogType.getName();
            }
        }
        return null;
    }


}
