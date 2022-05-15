package com.taki.common.utlis;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taki.common.core.AbstractObject;
import com.taki.common.core.CloneDirection;
import com.taki.common.core.PageResult;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @ClassName ObjectUtil
 * @Description 对象拷贝工具
 * @Author Long
 * @Date 2022/1/5 10:55
 * @Version 1.0
 */
public class ObjectUtil {




    /** 
     * @description: 转换集合 - 浅克隆
     * @param sourceList 源数据
     * @param  targetClass 转换类
     * @return 转换类集合
     * @author Long
     * @date: 2022/1/5 10:58
     */ 
    public static <T>List<T> convertList(List<? extends AbstractObject> sourceList, Class<T> targetClass){
        if (sourceList == null) {
            return null;
        }
        List<T>targetList = new ArrayList<>();
        sourceList.forEach(source ->{
            targetList.add(source.clone(targetClass));
        });

        return targetList;
    }

    /**
     * @description:  转换集合 深度克隆
     * @param sourceList 源数据
     * @param targetClass 转换 类
     * @param cloneDirection  转换方向
     * @return  转换类集合
     * @author Long
     * @date: 2022/1/5 11:04
     */
    public static <T>List<T> convertList(List<? extends AbstractObject> sourceList, Class<T> targetClass, CloneDirection cloneDirection){

        if (sourceList == null) {
            return null;
        }
        List<T> targetList = new ArrayList<>();

        sourceList.forEach(source ->{
           targetList.add(source.clone(targetClass,cloneDirection));

        });
        return targetList;
    }

    /**
     * @description: 转换集合 - 浅克隆
     * @param sourceList 源数据
     * @param  targetClass 转换类
     * @return 转换类集合
     * @author Long
     * @date: 2022/1/5 10:58
     */
    public static <T>List<T> convertList(List<? extends AbstractObject> sourceList, Class<T> targetClass, Consumer<T> consumer){
        if (sourceList == null) {
            return null;
        }
        List<T>targetList = new ArrayList<>();
        sourceList.forEach(source ->{
            targetList.add(source.clone(targetClass));
        });
        targetList.forEach(consumer);
        return targetList;
    }


    /**
     * @description:  转换集合 深度克隆
     * @param sourceList 源数据
     * @param targetClass 转换 类
     * @param cloneDirection  转换方向
     * @return  转换类集合
     * @author Long
     * @date: 2022/1/5 11:04
     */
    public static <T>List<T> convertList(List<? extends AbstractObject> sourceList, Class<T> targetClass, CloneDirection cloneDirection, Consumer<T> consumer){

        if (sourceList == null) {
            return null;
        }
        List<T> targetList = new ArrayList<>();

        sourceList.forEach(source ->{
            targetList.add(source.clone(targetClass,cloneDirection));

        });
        targetList.forEach(consumer);
        return targetList;
    }

    /**
     * @description: 转换分页数据 浅度克隆
     * @param sourcePage 源数据分页
     * @param targetClass 转换类
     * @return
     * @author Long
     * @date: 2022/1/5 11:14
     */
    public static <T>IPage<T>convertPage(IPage<? extends AbstractObject> sourcePage,Class<T> targetClass){

        if (sourcePage == null){
             return null;
        }
        List<? extends AbstractObject> sourceList = sourcePage.getRecords();
        List<T> targetList = convertList(sourceList,targetClass);

        IPage newPage = new Page(sourcePage.getCurrent(),sourcePage.getSize(),sourcePage.getTotal(),sourcePage.searchCount());
        newPage.setRecords(targetList);
        return newPage;
    }

    /**
     * @description: 转换分页数据 深度克隆
     * @param sourcePage 源数据分页
     * @param targetClass 转换类
     * @return
     * @author Long
     * @date: 2022/1/5 11:14
     */
    public static <T>IPage<T>convertPage(IPage<? extends AbstractObject> sourcePage,Class<T> targetClass,CloneDirection cloneDirection){

        if (sourcePage == null){
            return null;
        }
        List<? extends AbstractObject> sourceList = sourcePage.getRecords();
        List<T> targetList = convertList(sourceList,targetClass,cloneDirection);
        IPage newPage = new Page(sourcePage.getCurrent(),sourcePage.getSize(),sourcePage.getTotal(),sourcePage.searchCount());
        newPage.setRecords(targetList);
        return newPage;
    }


    /**
     * @description: 转换分页数据 浅度克隆
     * @param sourcePage 源数据分页
     * @param targetClass 转换类
     * @return
     * @author Long
     * @date: 2022/1/5 11:14
     */
    public static <T>IPage<T>convertPage(IPage<? extends AbstractObject> sourcePage,Class<T> targetClass,Consumer consumer){

        if (sourcePage == null){
            return null;
        }
        List<? extends AbstractObject> sourceList = sourcePage.getRecords();
        List<T> targetList = convertList(sourceList,targetClass);
        targetList.forEach(consumer);

        IPage newPage = new Page(sourcePage.getCurrent(),sourcePage.getSize(),sourcePage.getTotal(),sourcePage.searchCount());
        newPage.setRecords(targetList);

        return newPage;
    }


    /**
     * @description: 转换分页数据 深度克隆
     * @param sourcePage 源数据分页
     * @param targetClass 转换类
     * @return
     * @author Long
     * @date: 2022/1/5 11:14
     */
    public static <T>IPage<T>convertPage(IPage<? extends AbstractObject> sourcePage,Class<T> targetClass,CloneDirection cloneDirection,Consumer consumer){

        if (sourcePage == null){
            return null;
        }
        List<? extends AbstractObject> sourceList = sourcePage.getRecords();
        List<T> targetList = convertList(sourceList,targetClass,cloneDirection);
        targetList.forEach(consumer);
        IPage newPage = new Page(sourcePage.getCurrent(),sourcePage.getSize(),sourcePage.getTotal(),sourcePage.searchCount());
        newPage.setRecords(targetList);
        return newPage;
    }

    /**
     * @description: 转换自定义分页数据 浅度克隆
     * @param sourcePageResult  源分页数据
     * @param targetClass 转换类
     * @return  定义分页数据
     * @author Long
     * @date: 2022/1/5 11:47
     */
    public static <T>PageResult<T> convertPageResult(PageResult<? extends AbstractObject> sourcePageResult,Class<T> targetClass){

        if (sourcePageResult == null){
            return null;
        }
        List<? extends AbstractObject> sourceList = sourcePageResult.getContent();
        List<T> targetList =  convertList(sourceList,targetClass);
        PageResult<T> targetPageBean = new PageResult<T>(targetList);
        targetPageBean.setNumber(sourcePageResult.getNumber());
        targetPageBean.setSize(sourcePageResult.getSize());
        targetPageBean.setTotalElements(sourcePageResult.getTotalElements());
        targetPageBean.setTotalPages(sourcePageResult.getTotalPages());
        targetPageBean.setNumberOfElements(sourcePageResult.getNumberOfElements());
        return targetPageBean;

    }

    /**
     * @description: 转换自定义分页数据 深度克隆
     * @param sourcePageResult  源分页数据
     * @param targetClass 转换类
     * @return  定义分页数据
     * @author Long
     * @date: 2022/1/5 11:47
     */
    public static <T>PageResult<T> convertPageResult(PageResult<? extends AbstractObject> sourcePageResult,Class<T> targetClass,CloneDirection cloneDirection){

        if (sourcePageResult == null){
            return null;
        }
        List<? extends AbstractObject> sourceList = sourcePageResult.getContent();
        List<T> targetList =  convertList(sourceList,targetClass,cloneDirection);
        PageResult<T> targetPageBean = new PageResult<T>(targetList);
        targetPageBean.setNumber(sourcePageResult.getNumber());
        targetPageBean.setSize(sourcePageResult.getSize());
        targetPageBean.setTotalElements(sourcePageResult.getTotalElements());
        targetPageBean.setTotalPages(sourcePageResult.getTotalPages());
        targetPageBean.setNumberOfElements(sourcePageResult.getNumberOfElements());
        return targetPageBean;

    }

    /**
     * @description: 转换自定义分页数据 浅度克隆
     * @param sourcePageResult  源分页数据
     * @param targetClass 转换类
     * @return  定义分页数据
     * @author Long
     * @date: 2022/1/5 11:47
     */
    public static <T>PageResult<T> convertPageResult(PageResult<? extends AbstractObject> sourcePageResult,Class<T> targetClass,Consumer<T> consumer){

        if (sourcePageResult == null){
            return null;
        }
        List<? extends AbstractObject> sourceList = sourcePageResult.getContent();
        List<T> targetList =  convertList(sourceList,targetClass);
        targetList.forEach(consumer);
        PageResult<T> targetPageBean = new PageResult<T>(targetList);
        targetPageBean.setNumber(sourcePageResult.getNumber());
        targetPageBean.setSize(sourcePageResult.getSize());
        targetPageBean.setTotalElements(sourcePageResult.getTotalElements());
        targetPageBean.setTotalPages(sourcePageResult.getTotalPages());
        targetPageBean.setNumberOfElements(sourcePageResult.getNumberOfElements());

        return targetPageBean;

    }


    /**
     * @description: 转换自定义分页数据 深度克隆
     * @param sourcePageResult  源分页数据
     * @param targetClass 转换类
     * @return  定义分页数据
     * @author Long
     * @date: 2022/1/5 11:47
     */
    public static <T>PageResult<T> convertPageResult(PageResult<? extends AbstractObject> sourcePageResult,Class<T> targetClass,CloneDirection cloneDirection,Consumer<T> consumer){

        if (sourcePageResult == null){
            return null;
        }
        List<? extends AbstractObject> sourceList = sourcePageResult.getContent();
        List<T> targetList =  convertList(sourceList,targetClass,cloneDirection);
        targetList.forEach(consumer);
        PageResult<T> targetPageBean = new PageResult<T>(targetList);
        targetPageBean.setNumber(sourcePageResult.getNumber());
        targetPageBean.setSize(sourcePageResult.getSize());
        targetPageBean.setTotalElements(sourcePageResult.getTotalElements());
        targetPageBean.setTotalPages(sourcePageResult.getTotalPages());
        targetPageBean.setNumberOfElements(sourcePageResult.getNumberOfElements());
        return targetPageBean;

    }


}
