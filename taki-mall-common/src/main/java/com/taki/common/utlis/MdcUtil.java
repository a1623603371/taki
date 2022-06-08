package com.taki.common.utlis;

import com.taki.common.core.CoreConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;


/**
 * @ClassName MdcUtil
 * @Description 对MDC进行一层封装
 * @Author Long
 * @Date 2022/6/8 12:18
 * @Version 1.0
 */
@Slf4j
public class MdcUtil {

    private static  final String USER_CUSTOMIZED_FLAG_KEY = "USER_CUSTOMIZED_FLAG";

    private  static  final String USER_CUSTOMIZED_FLAG_VALUE = "USER_CUSTOMIZED_FLAG";

    public MdcUtil() {
    }

    /** 
     * @description: 获取当前线程的traceId
     * @param
     * @return  java.lang.String
     * @author Long
     * @date: 2022/6/8 12:21
     */ 
    public static String getTraceId(){

        return MDC.get(CoreConstants.TRACE_ID);
    }

    /**
     *初始化当前线程的traceId
     * @return
     */
    public  static  String getUkId(){
        String id = String.valueOf(SnowFlake.generateId());

        return id;

    }

    /*** 
     * @description: 初始化当前线程的traceId
     * @param 
     * @return  void
     * @author Long
     * @date: 2022/6/8 13:40
     */ 
    public  static void initTraceId(){

        String id  = String.valueOf(SnowFlake.generateId());

        MDC.put(CoreConstants.TRACE_ID,id);
        
    }

    /*** 
     * @description: 初始化当前线程的traceId ,根据父线程的traceId
     * @param parentTraceId
     * @return  void
     * @author Long
     * @date: 2022/6/8 13:42
     */ 
    public static  void  initTraceId(String  parentTraceId){
            String id = parentTraceId + SnowFlake.generateIdStr();

            MDC.put(CoreConstants.TRACE_ID,id);
    }


    /**
     *获取当前线程的traceId，如果没有，初始化
     * @return
     */
    public static String getOrInitTraceId(){

        String id = MDC.get(CoreConstants.TRACE_ID);
        if (StringUtils.isBlank(id)){
            id = String.valueOf(SnowFlake.generateId());
            MDC.put(CoreConstants.TRACE_ID,id);
        }

        return id;

    }

    /**
     * 设置当前线程的 traceId
     * @param traceId
     */
    public static  void setUserTraceId(String traceId){

        if (StringUtils.isBlank(traceId)){
        traceId = SnowFlake.generateIdStr();
        }

        MDC.put(CoreConstants.TRACE_ID, traceId);
        MDC.put(USER_CUSTOMIZED_FLAG_KEY,USER_CUSTOMIZED_FLAG_VALUE);

    }

    /**
     * 设置当前线程的traceId
     * @return
     */
    public static  Boolean isUserTraceId(){

        String value = MDC.get(USER_CUSTOMIZED_FLAG_KEY);

        if (StringUtils.isNotEmpty(value) && value.equals(USER_CUSTOMIZED_FLAG_VALUE)){
            return true;
        }

        return false;

    }

    /** 
     * @description: 设置 当前线程 traceId
     * @param traceId
     * @return  void
     * @author Long
     * @date: 2022/6/8 13:50
     */ 
    public  static void setTraceId(String traceId){

        if(!StringUtils.isBlank(traceId)){
        MDC.put(CoreConstants.TRACE_ID, traceId);
        }else {
            MDC.remove(CoreConstants.TRACE_ID);
        }
    }

    /*** 
     * @description:  移除当前线程  traceId
     * @param 
     * @return  void
     * @author Long
     * @date: 2022/6/8 13:52
     */ 
  public static  void  removeTraceId(){

        MDC.remove(CoreConstants.TRACE_ID);
  }


    /**
     * 清除
     */
  public static  void clear(){

        MDC.clear();

  }
}
