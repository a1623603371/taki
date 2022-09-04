package com.taki.consistency.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.asm.Type;
import org.springframework.cglib.core.ClassInfo;
import org.springframework.cglib.core.ReflectUtils;


import java.util.HashMap;
import java.util.StringJoiner;

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
    
    /*** 
     * @description:  获取入参类名称数组
     * @param signature aop切片切入的方法签名对象
     * @return 签名类字符（多个逗号分割）
     * @author Long
     * @date: 2022/9/1 21:29
     */ 
    public static String getArgsClassNames(Signature signature){
        MethodSignature methodSignature = (MethodSignature) signature;
        Class<?>[] parameterTypes = methodSignature.getParameterTypes();
        StringBuilder parameterStrTypes = new StringBuilder();
        for (int i = 0; i < parameterTypes.length; i++) {
            parameterStrTypes.append(parameterTypes[i].getName());
            if (parameterTypes.length != (i + 1)){
                parameterStrTypes.append(",");

            }
        }
        return parameterStrTypes.toString();
    }

    /***
     * @description: 获取被拦截方法得全限定名名称，格式，类路径 #方法名（参数1的类型，参数2的类型，...参数N的类型）
     * @param point 切点
     * @param  argsClazz 入参的Class对象
     * @return 被拦截的全限定名
     * @author Long
     * @date: 2022/9/1 21:36
     */
    public static String getTargetMethodFullyQualifiedName(JoinPoint  point,Class<?>[] argsClazz){
        StringJoiner methodSignNameJoiner = new StringJoiner("","","");
        methodSignNameJoiner.add(point.getTarget().getClass().getName())
                            .add("#")
                            .add(point.getSignature().getName());
        methodSignNameJoiner.add("(");

        for (int i = 0; i < argsClazz.length ; i++) {
            String className = argsClazz[i].getName();
            methodSignNameJoiner.add(className);
            if (argsClazz.length != (i + 1)){
                methodSignNameJoiner.add(",");
            }
        }
        methodSignNameJoiner.add(")");
        return  methodSignNameJoiner.toString();
    }

    /*** 
     * @description: 获取各个参数的Class对象数组
     * @param args 目标方法参数
     * @return  参数类对象数组
     * @author Long
     * @date: 2022/9/1 21:43
     */ 
    public static   Class<?>[] getArgsClass(Object[] args){

        Class<?>[] clazz = new Class<?>[args.length];

        for (int i = 0; i < args.length; i++) {

            if (!args[i].getClass().isPrimitive()){
                //获取的是封装类型而不是基础类型
                String result  = args[i].getClass().getName();
                Class<?> typeClazz = PRIMITIVE_MAP.get(result);
                clazz[i] = ObjectUtil.isEmpty(typeClazz) ? args[i].getClass() : typeClazz;
            }

        }

        return clazz;
        
    }

    /*** 
     * @description:  获取类的全路径
     * @param clazz  要获取的类
     * @return  类路径
     * @author Long
     * @date: 2022/9/1 21:50
     */ 
    public static String getFullyQualifiedClassName(Class<?> clazz){

        if(ObjectUtil.isEmpty(clazz)){
            return "";
        }

        return clazz.getName();
        
    }

    /***
     * @description: 效验目标类是否实现了目标接口
     * @param targetClass 要检查类
     * @param  targetInterfaceClassName 目标接口类名称（包全路径）
     * @return  结果
     * @author Long
     * @date: 2022/9/1 21:52
     */
    public  static Boolean isRealizeTargetInterface(Class<?> targetClass,String targetInterfaceClassName){

        ClassInfo classInfo = ReflectUtils.getClassInfo(targetClass);

        for (Type anInterface : classInfo.getInterfaces()) {
                if (anInterface.getClassName().equals(targetInterfaceClassName)){
                    return true;
                }
        }
        return false;
    }


}
