package com.taki.common.core;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName AbstractObject
 * @Description 基础POJO 类
 * @Author Long
 * @Date 2021/12/13 17:28
 * @Version 1.0
 */

public abstract class AbstractObject {

    /** 
     * @description: 浅度克隆
     * @param: clazz 目标对象class 对象
     * @return: 目标对象实体
     * @author Long
     * @date: 2021/12/13 17:29
     */ 
    public <T> T clone (Class<T> clazz){
        try {
            T target = clazz.newInstance();

            BeanCopierUtils.copyProperties(this,target);

            return getTarget(target);

        }catch (Exception e) {
            throw new RuntimeException("error",e);
        }
    }
    
    /** 
     * @description: 浅度克隆时对原对象list属性处理
     * @param target
     * @return  T
     * @author Long
     * @date: 2021/12/13 17:52
     */ 
    private <T> T getTarget(T target) throws Exception {
        Class<?> targetClazz = target.getClass();
        Field[] fields =targetClazz.getFields();
        for (Field field : fields) {
            field.setAccessible(true);
            // 判断实体类型 某个字段是否是list
            if (field.getType() != List.class){
                continue;
            }

            List<?>list = (List<?>) field.get(target);

            if (list == null || list.size() == 0){
                continue;
            }

            Class targetListGenericTypeClazz = getTargetListGenericType(field);

            if(targetListGenericTypeClazz != null && !isAbstractObjectClass(targetListGenericTypeClazz)){
                continue;
            }

            String name = field.getName();

            String setMethodName ="set" + name.substring(0,1).toUpperCase() + field.getType();

            Method setFieldMethod = targetClazz.getMethod(setMethodName,field.getType());

            setFieldMethod.invoke(target,new ArrayList<>());

        }
        return target;
    }

    /** 
     * @description:  判断某个的对象类是否继承了AbstractObject
     * @param targetListGenericTypeClazz clazz 对象
     * @return  boolean
     * @author Long
     * @date: 2021/12/13 20:12
     */ 
    private boolean isAbstractObjectClass(Class targetListGenericTypeClazz) {
        // 目标可能没有继承AbstractObject类
        if (targetListGenericTypeClazz != null){
            String superClazzTypeName = targetListGenericTypeClazz.getClass().getTypeName();
            if (superClazzTypeName.equals(Object.class.getTypeName())){
                return false;
            }

            if (superClazzTypeName.equals(AbstractObject.class.getTypeName())){
                return true;
            }else {
                return isAbstractObjectClass(targetListGenericTypeClazz.getSuperclass());
            }
        }
        return false;
    }

    /** 
     * @description:  获取List 集合 上的泛型
     * @param field 目标字段
     * @return  java.lang.Class
     * @author Long
     * @date: 2021/12/13 18:00
     */ 
    private Class getTargetListGenericType(Field field) {
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType){
            ParameterizedType parameterizedType = (ParameterizedType) genericType;

            return (Class) parameterizedType.getActualTypeArguments()[0];
        }

        return null;
    }
}
