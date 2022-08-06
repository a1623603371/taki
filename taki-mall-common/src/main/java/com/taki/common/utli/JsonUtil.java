package com.taki.common.utli;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taki.common.config.ObjectMapperImpl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JsonUtil
 * @Description Json 工具
 * @Author Long
 * @Date 2022/1/8 14:48
 * @Version 1.0
 */
@Slf4j
public class JsonUtil {

    private static  final ObjectMapper objectMapper = new ObjectMapperImpl();


    /**
     * @description: 对象转json
     * @param o 对象
     * @return json字符串
     * @author Long
     * @date: 2022/1/10 9:13
     */
    public static String object2Json(Object o){

        if (o == null){
            return null;
        }

        String json = null;

        try {
            json = objectMapper.writeValueAsString(o);
        }catch (Exception e){
            log.warn("object  to json error:{}",e);
        }

        return json;
    }

    /** 
     * @description: 将list对象集合转换成json 对象
     * @param objects
     * @return  java.util.List<T>
     * @author Long
     * @date: 2022/1/10 9:20
     */
    public static <T> List<String> listObject2ListJson(List<T> objects) {
        if (objects == null) {
            return null;
        }
        List<String> lists = new ArrayList<>();
        lists.forEach(object -> {
            lists.add(object2Json(object));
        });
        return lists;
    }

    /** 
     * @description: JSON字符串集合批量转换对象集合
     * @param jsons json 字符
     * @param clazz  对象类
     * @return  java.util.List<T>
     * @author Long
     * @date: 2022/1/10 9:24
     */ 
    public static <T> List<T> listJson2ListObject(List<String> jsons,Class<T> clazz){

        if (jsons == null){
            return null;
        }
        List<T> objects = new ArrayList<>();
        jsons.forEach(json->{

            objects.add(json2Object(json,clazz));

        });

        return objects;
    }
        
    /*** 
     * @description:  json 字符转换 object 对象
     * @param json json 字符串
     * @param clazz 转换目标对象类
     * @return  T
     * @author Long
     * @date: 2022/1/10 9:27
     */ 
    public static <T> T json2Object(String json, Class<T> clazz) {

        if (StringUtils.isBlank(json)){
            return null;
        }
        try {
            return objectMapper.readValue(json,clazz);
        }catch (Exception e){
            log.warn("json to class error:{}",e);
        }

        return null;
    }

    /*** 
     * @description: json字符串转指定类型的对象
     * @param json 对象json字符
     * @param tTypeReference 指定转换类型
     * @return  T
     * @author Long
     * @date: 2022/1/10 9:33
     */ 
    public static <T> T json2Object(String json, TypeReference<T> tTypeReference){
        if (json == null){
            return null;
        }
        try {
            return objectMapper.readValue(json,tTypeReference);
        }catch (Exception e){
            log.warn("json to object error:{}",e);
        }

        return null;
    }

    /**
     * @description:  获取数据
     * @param data
     * @param clazz
     * @return  T
     * @author Long
     * @date: 2022/1/10 9:44
     */
    public static <T> T getData(Object data,Class<T> clazz){
        if (data == null){
            return null;
        }
        try {
            String jsonStr = objectMapper.writeValueAsString(data);
            return objectMapper.readValue(jsonStr, clazz);
        }catch (Exception e){
            log.warn("data process json error:{}",e);
        }

        return null;
    }

    /**
     * @description: 获取数据
     * @param data
    tTypeReference
     * @return  T
     * @author Long
     * @date: 2022/1/10 9:51
     */
    public static <T> T getData(Object data,TypeReference<T> tTypeReference){
        if (data == null){
            return null;
        }
        try {
            String jsonStr = objectMapper.writeValueAsString(data);
            return objectMapper.readValue(jsonStr, tTypeReference);
        }catch (Exception e){
            log.warn("data process json error:{}",e);
        }

        return null;
    }


}
