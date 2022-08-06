package com.taki.common.utli;

import com.alibaba.fastjson.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName LoggerFormat
 * @Description 日志格式
 * @Author Long
 * @Date 2022/6/8 12:06
 * @Version 1.0
 */
public class LoggerFormat {

    private final Map<String,String> logInfo = new LinkedHashMap<>();

    private static  final   String USER_TRACE_ID = "TraceId";


    public LoggerFormat() {


    }


    public static  LoggerFormat build(){
        return new LoggerFormat();
    }

    /***
     * @description: 日志备注
     * @param remark 备注
     * @return
     * @author Long
     * @date: 2022/6/8 12:12
     */
    public LoggerFormat remark(String remark){
        logInfo.put("remark",remark);
        return this;
    }

    /** 
     * @description: 日志数据
     * @param key
     * @param value
     * @return
     * @author Long
     * @date: 2022/6/8 12:14
     */ 
    public LoggerFormat data (String key,String value)
    {
        logInfo.put(key,value);

        return this;
        
    }


    /**
     * @description: 日志数据
     * @param key
     * @param  value
     * @return
     * @author Long
     * @date: 2022/6/8 12:16
     */
    public LoggerFormat data(String key,Object value){

        logInfo.put(key, JSONObject.toJSONString(value));
        return this;
    }

    /** 
     * @description:
     * @param 
     * @return  java.lang.String
     * @author Long
     * @date: 2022/6/8 12:17
     */ 
    public String finish (){
        StringBuilder sb = new StringBuilder();

        if (!logInfo.isEmpty()){
            Set<String> keys =logInfo.keySet();

            if (MdcUtil.isUserTraceId()){
                sb.append(USER_TRACE_ID).append("=[").append(MdcUtil.getTraceId()).append("]");
            }

            keys.forEach(key->{
                sb.append(key).append("[").append(logInfo.get(key)).append("]");
            });
        }

        return sb.toString();
    }
}
