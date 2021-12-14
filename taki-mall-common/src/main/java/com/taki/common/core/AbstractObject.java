package com.taki.common.core;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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
     * @description:  克隆对象
     * @param target 目标对象
     * @return  T
     * @author Long
     * @date: 2021/12/14 14:09
     */ 
    public <T> T clone(T target){
        try {
          BeanCopierUtils.copyProperties(this,target);
          return getTarget(target);
        }catch (Exception e){
            throw new RuntimeException("error",e);
        }
    }

    /**
     * @description:  深度克隆
     * @param targetClazz 目标对象class 对象
     * @param cloneDirection
     * @return  T
     * @author Long
     * @date: 2021/12/14 14:19
     */
    public <T> T clone(Class<T> targetClazz,CloneDirection cloneDirection){

        try {
            //先完成基本字段的浅克隆
            T target = targetClazz.newInstance();
            BeanCopierUtils.copyProperties(this,target);


            // 完成内部AbstractObject,List<AbstractObject>类型字段的深度克隆
            Class<?>thisClazz =this.getClass();
            List<Field> thisFields = listThisField(null,thisClazz);

            for (Field thisField : thisFields) {
                thisField.setAccessible(true);

                if (!Collection.class.isAssignableFrom(thisField.getType())){
                    Class<?>sourceFieldClazz =thisField.getType();

                    if (sourceFieldClazz == String.class || sourceFieldClazz == BigDecimal.class
                        || sourceFieldClazz == Integer.class || "int".equals(sourceFieldClazz.toString())
                        || sourceFieldClazz == Long.class || "long".equals(sourceFieldClazz.toString())
                        || sourceFieldClazz == Double.class || "double".equals(sourceFieldClazz.toString())
                        || sourceFieldClazz == Short.class || "short".equals(sourceFieldClazz.toString())
                        || sourceFieldClazz ==  Float.class || "float".equals(sourceFieldClazz.toString())
                        || sourceFieldClazz == Boolean.class || "boolean".equals(sourceFieldClazz.toString())
                        || sourceFieldClazz == Byte.class || "byte".equals(sourceFieldClazz.toString())
                        || sourceFieldClazz == Date.class || sourceFieldClazz == java.sql.Date.class
                        || sourceFieldClazz == LocalDateTime.class || sourceFieldClazz == LocalDate.class
                        || sourceFieldClazz == Timestamp.class) {
                        continue;
                    }

                    // 判断某个字段是否AbstractObject类型的
                    try {
                    if (!(thisField.getType().newInstance() instanceof  AbstractObject)){
                        continue;
                    }
                    }catch (Exception e){
                        if (e instanceof InstantiationException){
                            continue;
                        }
                        throw new RuntimeException("error",e);
                    }

                    AbstractObject sourceObj = (AbstractObject) thisField.get(this);

                    if (sourceObj == null){
                        continue;
                    }

                    Field targetField = null;

                    try {
                    targetField =getTargetClazzField(targetField,targetClazz);
                    }catch (NoSuchFieldException e){
                        continue;
                    }

                    if (targetField != null){
                        Class<?> cloneTargetClazz =targetField.getType();

                        AbstractObject clonedObj = (AbstractObject) sourceObj.clone(cloneTargetClazz,cloneDirection);

                        // 获取设置克隆好的对象的方法名称
                        String name =thisField.getName();

                        String setMethodName = "set"+ name.substring(0,1).toUpperCase() + name.substring(1);
                            //getMethod() 方法可以获取当前类与父类中所有的public 方法
                        Method setFieldMethod =targetClazz.getMethod(setMethodName,targetField.getType()) ;

                        setFieldMethod.invoke(target,clonedObj);

                    }

                }else {
                    Collection<?> list = (Collection<?>) thisField.get(this);

                    if (list == null || list.size() == 0){
                        continue;
                    }
                    //获取List泛型
                    Field targetField = null;

                    try {
                        targetField =getTargetClazzField(targetField,targetClazz);
                    }catch (NoSuchFieldException e){
                        continue;
                    }

                    if (targetField != null){
                        Class<?> cloneTargetClazz =getTargetListGenericType(targetField);
                        // 将list集合克隆到目标list集合中
                        Collection clonedList = (Collection) thisField.get(this).getClass().newInstance();
                        cloneList(list,clonedList,cloneTargetClazz,cloneDirection);

                        // 获取设置克隆好的list 的方法名称
                        String name = targetField.getName();

                        String setMethodName = "set" + name.substring(0,1).toUpperCase() + name.substring(1);

                        Method setFieldMethod = targetClazz.getMethod(setMethodName,targetField.getType());

                        setFieldMethod.invoke(target,clonedList);

                    }

                }
                
            }

        return target;
        }catch (Exception e){
            throw new RuntimeException("error",e);
        }
    }

    /*** 
     * @description:  将 list 克隆到另一个list
     * @param sourceList 数据源对象
     * @param clonedList 目标集合对象
     * @param cloneTargetClazz 目标集合对象中元素类型
     * @param cloneDirection 深度克隆
     * @return  void
     * @author Long
     * @date: 2021/12/14 15:23
     */ 
    private <T> void cloneList(Collection sourceList, Collection clonedList, Class<?> cloneTargetClazz, CloneDirection cloneDirection) {

        sourceList.forEach( object ->{
            if ( object instanceof  AbstractObject){
                AbstractObject targetObject = (AbstractObject) object;
                AbstractObject cloneObject = (AbstractObject) targetObject.clone(cloneTargetClazz,cloneDirection);
                clonedList.add(cloneObject);
            }else {
                // 非 List< ?extends AbstractObject> 类型的集合字段 直接复用原对象的字段值
                clonedList.add(object);
            }
        });
    }

    /**
     * @description:  如果目标有继承父类需要递归获取目标字段
     * @param thisField 源对象中某个字段
     * @param targetClazz  目标对象clazz 对象
     * @return  java.lang.reflect.Field
     * @author Long
     * @date: 2021/12/14 15:00
     */
    private <T> Field getTargetClazzField(Field thisField, Class<T> targetClazz) throws NoSuchFieldException {
        Field targetField =null;

        try {
            targetField =targetClazz.getDeclaredField(thisField.getName());
        }catch (NoSuchFieldException e){
            // 目标类有可能没有继承AbstractObject类

            if(targetClazz.getSuperclass() == null){
                String targetSuperClazzTypeName =targetClazz.getSuperclass().getTypeName();

                if (!targetSuperClazzTypeName.equals(Object.class.getTypeName()) && !targetSuperClazzTypeName.equals(AbstractObject.class.getTypeName())){
                        // 递归
                    targetField = getTargetClazzField(thisField,targetClazz.getSuperclass());
                }
            }

            if (targetField == null){
                throw e;
            }

        }
        return targetField;

    }

    /**
     * @description: 递归获取当前以及父类的字段
     * @param thisFields 当前类以及父类的所有字段
     * @param thisClazz 原始类 class 对象
     * @return
     * @author Long
     * @date: 2021/12/14 14:31
     */
    private List<Field> listThisField(List<Field> thisFields, Class<?> thisClazz) {
        if (thisFields == null){
            thisFields = new ArrayList<>(Arrays.asList(thisClazz.getDeclaredFields()));
        }else{
            thisFields.addAll(Arrays.asList(thisClazz.getDeclaredFields()));
        }

        if (!thisClazz.getSuperclass().getTypeName().equals(AbstractObject.class.getTypeName())){
            listThisField(thisFields,thisClazz.getSuperclass());
        }
        return thisFields;
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
