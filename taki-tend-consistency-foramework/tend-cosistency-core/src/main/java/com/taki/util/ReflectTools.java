package com.taki.util;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * @ClassName ReflectTools
 * @Description 反射工具类
 * @Author Long
 * @Date 2022/8/31 23:56
 * @Version 1.0
 */
@Slf4j
public class ReflectTools {
    
    /*** 
     * @description: 初始化基础数据类型的Map
     * @param null
     * @return  
     * @author Long
     * @date: 2022/8/31 23:58
     */ 
    private static  final HashMap<String,Class<?>>  PRIMITIVE_MAP = new HashMap<String,Class<?>>(){
        {
            put("java.lang.Integer",int.class);
            put("java.lang.Double",double.class);
            put("java.lang.Float",float.class);
            put("java.lang.Long",long.class);
            put("java.lang.Short",short.class);
            put("java.lang.Boolean",boolean.class);
            put("java.lang.Char",char.class);

        }

    };

    /*** 
     * @description:  构造参数类型数组
     * @param parameterTypes 参数类型数组
     * @return  参数类型
     * @author Long
     * @date: 2022/9/1 0:01
     */ 
    public static Class<?>[] buildTypeClassArray(String[] parameterTypes){

        Class<?>[] parameterTypeClassArray = new Class<?>[parameterTypes.length];

        for (int i = parameterTypeClassArray.length -1; i >= 0; i-- ) {
            try {
                parameterTypeClassArray[i] =Class.forName(parameterTypes[i]);
            }catch (ClassNotFoundException  e){
                e.printStackTrace();
            }
        }
        return parameterTypeClassArray;

    }

    /*** 
     * @description: 根据类名称查询类对象
     * @param className 类名称
     * @return  java.lang.Class<?>
     * @author Long
     * @date: 2022/9/1 0:06
     */ 
    public  static Class<?> getClassByName(String className){

        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error("发生类查询不到，加载类为：{}",className,e);
            return null;
        }

    }

    /*** 
     * @description:  根据类名称获取对象
     * @param className 名称
     * @return
     * @author Long
     * @date: 2022/9/1 0:08
     */ 
    public static Class<?> checkClassByName(String className){

        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /*** 
     * @description:  构造方法入参
     * @param parameterText 方法入参JSON字符串 会解析成JSON数组
     * @param parameterTypeClassArray 参数类型的类数组
     * @return  java.lang.Object[]
     * @author Long
     * @date: 2022/9/1 0:10
     */ 
    public  static Object[] buildArgs(String parameterText,Class<?>[] parameterTypeClassArray){

        JSONArray paramJsonArray = JSONUtil.parseArray(parameterText);

        Object[] args = new Object[paramJsonArray.size()];

        for (int i = paramJsonArray.size() - 1 ; i >= 0  ; i--) {
            if (paramJsonArray.getStr(i).startsWith("{")){
                //将数据转换为对应的数据类型，json字符串格式的每一个入参对象（json字符串） + 入参对象的Class对象
                //转换为一个对象
                args[i] = JSONUtil.toBean(paramJsonArray.getStr(i),parameterTypeClassArray[i]);

            }else {
                args[i] = paramJsonArray.get(i);
            }
            
        }

        return args;

    }

}
