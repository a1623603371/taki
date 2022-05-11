package com.taki.inventory.tcc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName TccResultHolder
 * @Description 存储TCC第一阶段执行结果，用于解决TCC 幂等 空回滚悬挂 问题
 * @Author Long
 * @Date 2022/5/11 21:54
 * @Version 1.0
 */
public class TccResultHolder {

    /**
     * 标识 TCC try阶段开始执行结果
      */
 private static final String TRY_START = "TRY_START";


    /**
     *标识TCC  try阶段执行成功的标识
     */
 private static  final  String TRY_SUCCESS = "TRY_SUCCESS";

    /**
     * 保存 TCC事务执行过程的状态
     */
 private static Map<Class<?>,Map<String,String>> map = new ConcurrentHashMap<>();


  /**
   * @description: 标识try阶段开始执行
   * @param tccClass
   * @param bizKey 业务唯一标识
   * @param xid
   * @return  void
   * @author Long
   * @date: 2022/5/11 22:00
   */
 public static  void tagTryStart(Class<?> tccClass,String bizKey,String xid){

        setResult(tccClass,bizKey,xid,TRY_START);

 }


 /*** 
  * @description:  标记try阶段执行成功
  * @param tccClass
  * @param bizKey
  * @param xid
  * @return  void
  * @author Long
  * @date: 2022/5/11 22:18
  */ 
 public static void tagTrySuccess(Class<?> tccClass,String bizKey,String xid){

     setResult(tccClass,bizKey,xid,TRY_SUCCESS);
     
 }


    private static void setResult(Class<?> tccClass, String bizKey,String xid,String v) {
        Map<String,String> results = map.get(tccClass);

        if (results == null){
            synchronized (map){
                if (results == null){
                    results = new ConcurrentHashMap<>();
                    map.put(tccClass,results);
                }
            }
        }

        results.put(getTccExecution(xid,bizKey),v); // 保存当前分布式事务Id



    }

    private static String getTccExecution(String xid, String bizKey) {
     return xid + "::" + bizKey;
    }


}
