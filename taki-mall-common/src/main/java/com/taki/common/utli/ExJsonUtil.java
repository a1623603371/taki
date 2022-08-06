package com.taki.common.utli;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @ClassName ExJsonUtil
 * @Description JSON
 * @Author Long
 * @Date 2022/3/3 21:49
 * @Version 1.0
 */
@Slf4j
public class ExJsonUtil {

    /**
     * @description: 合并 JSON
     * @param srcExJson
     * @param targetExrJson
     * @return  java.lang.String
     * @author Long
     * @date: 2022/3/3 21:51
     */
    public static String mergeExtJson(String  srcExJson,String targetExrJson){
        JSONObject srcJson = parseJson(srcExJson);
        JSONObject targetJSON =parseJson(targetExrJson);

        //原值为空，直接返回新值
        if (srcJson == null){
            return  targetExrJson;
        }

        //如果新值为空，直接返回
        if (targetJSON == null){
            return srcExJson;
        }

        //新的值覆盖原有的值

        srcJson.putAll(targetJSON);

        return srcJson.toJSONString();
    }


    /** 
     * @description: 合并字符
     * @param 
     * @return  java.lang.String
     * @author Long
     * @date: 2022/3/3 22:00
     */ 
    public static String mergeField(String  srcExtJson ,String  field,Object value){
        // 如果没写新加内容，就直接返回原值
        if (StringUtils.isBlank(field) || Objects.isNull(value)){
            return srcExtJson;
        }

        JSONObject srcJSON = parseJson(srcExtJson);

        JSONObject targetJSON = new JSONObject();

        targetJSON.put(field,value);
        //如果原值为空，直接返回值
        if (srcJSON == null){
            return targetJSON.toJSONString();
        }

        // 新值覆盖原值
        srcJSON.putAll(targetJSON);
        return srcJSON.toJSONString();
    }

    /**
     * @description: String转换JSON
     * @param srcExJson
     * @return
     * @author Long
     * @date: 2022/3/3 21:53
     */
    public  static JSONObject parseJson(String srcExJson) {
        try {
            return JSONObject.parseObject(srcExJson);
        }catch (Exception e){
            log.error("parse extInfo error !",e);
        }
        return null;
    }

    /** 
     * @description: String json 字符 转换为 对象
     * @param extJson  String json 字符
     * @param clazz 转换对象
     * @return  T
     * @author Long
     * @date: 2022/3/3 21:57
     */ 
    public static <T> T parseJson(String extJson,Class<T> clazz){
        try {
            if (StringUtils.isEmpty(extJson)){
                return null;
            }
            return JSONObject.parseObject(extJson
            ,clazz);
        }catch (Exception e){
            log.error("parse extInfo error!",e);
        }
        return null;
    }
}
