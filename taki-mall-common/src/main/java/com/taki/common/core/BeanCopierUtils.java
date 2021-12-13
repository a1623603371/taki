package com.taki.common.core;

import org.springframework.cglib.beans.BeanCopier;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName BeanCopierUtils
 * @Description 克隆实体工具类
 * @Author Long
 * @Date 2021/12/13 17:33
 * @Version 1.0
 */
public class BeanCopierUtils {


    private static  final Map<String, BeanCopier> beanCopierCacheMap =new HashMap<>();


    /**
     * @description: 将 source 对象的属性拷贝到 target对象中
     * @param source 实际对象
     * @param target 明白对象
     * @return  void
     * @author Long
     * @date: 2021/12/13 17:43
     */
    public static  void copyProperties(Object source,Object target){
        String  cacheKey = source.getClass().toString() + target.getClass().toString();

        BeanCopier copier = null;

        if (!beanCopierCacheMap.containsKey(cacheKey)){
            synchronized (BeanCopierUtils.class){
                if (!beanCopierCacheMap.containsKey(cacheKey)){
                    copier =BeanCopier.create(source.getClass(),target.getClass(),false);
                    beanCopierCacheMap.put(cacheKey,copier);
                }else {
                    copier = beanCopierCacheMap.get(cacheKey);
                }
            }
        }else {
            copier = beanCopierCacheMap.get(cacheKey);
        }
        copier.copy(source,target,null);
    }

}
