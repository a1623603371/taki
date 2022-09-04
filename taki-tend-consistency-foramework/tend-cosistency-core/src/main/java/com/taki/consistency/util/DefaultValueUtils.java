package com.taki.consistency.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * @ClassName DefaultValueUtils
 * @Description 默认工具类
 * @Author Long
 * @Date 2022/9/4 15:17
 * @Version 1.0
 */
@Slf4j
public class DefaultValueUtils {

    
    /*** 
     * @description:  获取参数的值
     * @param value 给定值
     * @param defaultValue 默认值
     * @return  java.lang.String
     * @author Long
     * @date: 2022/9/4 15:18
     */ 
    public static String getOrDefault(String value,String defaultValue){

        if(StringUtils.isEmpty(value)){
            return defaultValue;
        }

        return value;

        
    }

    /*** 
     * @description:  获取参数的值
     * @param value 给定值
     * @param  defaultValue 默认值
     * @return  java.lang.Integer
     * @author Long
     * @date: 2022/9/4 15:20
     */ 
    public static  Integer getOrDefault(Integer value,Integer defaultValue){

        if (ObjectUtils.isEmpty(value)){
            return value;
        }
        return defaultValue;
    }

    /*** 
     * @description:  获取参数的值
     * @param value 给定值
     * @param  defaultValue 默认值
     * @return  java.lang.Integer
     * @author Long
     * @date: 2022/9/4 15:20
     */
    public static Long getOrDefault(Long value,Long defaultValue){
        if (ObjectUtils.isEmpty(value)){
            return value;
        }
        return defaultValue;
    }

    /***
     * @description:  获取参数的值
     * @param value 给定值
     * @param  defaultValue 默认值
     * @return  java.lang.Integer
     * @author Long
     * @date: 2022/9/4 15:20
     */
    public static Boolean getOrDefault(Boolean value,Boolean defaultValue){
        if (ObjectUtils.isEmpty(value)){
            return value;
        }
        return defaultValue;
    }

}
