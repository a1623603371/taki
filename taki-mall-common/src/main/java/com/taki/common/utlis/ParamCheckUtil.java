package com.taki.common.utlis;

import com.taki.common.exception.BaseErrorCodeEnum;
import com.taki.common.exception.ErrorCodeEnum;
import com.taki.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @ClassName ParamCheckUtil
 * @Description 参数效验工具类
 * @Author Long
 * @Date 2022/1/2 19:57
 * @Version 1.0
 */
@Slf4j
public class ParamCheckUtil {
    /**
     *  验证对象是否为空
     * @param o 验证对象
     * @throws Exception
     */
    public  static void checkObjectNonNull(Object o) {
        if (Objects.isNull(o)){
            throw new ServiceException(ErrorCodeEnum.SERVER_ILLEGAL_ARGUMENT_ERROR);
        }
    }

    /***
     * @description:  验证对象是否为空
     * @param o 验证对象
     *@param errorCodeEnum 错误枚举
     *@param args 参数
     * @return  void
     * @author Long
     * @date: 2022/1/2 20:08
     */
    public static void  checkObjectNonNull(Object o,BaseErrorCodeEnum errorCodeEnum,Object ... args){

        if (Objects.isNull(o)){
            throw new ServiceException(errorCodeEnum.getErrorCode(),errorCodeEnum.getErrorMsg(),args);
        }

    }

    /**
     * @description: 验证字符串是否为空
     * @param s 验证字符串
     * @return  void
     * @author Long
     * @date: 2022/1/2 20:08
     */
    public  static void checkStringNonEmpty(String s) {

        if (StringUtils.isNotBlank(s)) {
            throw new ServiceException(ErrorCodeEnum.SERVER_ILLEGAL_ARGUMENT_ERROR);

        }
    }


    /***
     * @description:  验证字符串是否为空
     * @param s 验证字符串
     *@param errorCodeEnum 错误枚举
     *@param args 参数
     * @return  void
     * @author Long
     * @date: 2022/1/2 20:08
     */
    public static void  checkStringNonEmpty(String s,BaseErrorCodeEnum errorCodeEnum,Object ... args){

        if (StringUtils.isNotBlank(s)){
            throw new ServiceException(errorCodeEnum.getErrorCode(),errorCodeEnum.getErrorMsg(),args);
        }

    }

    /**
     * @description:  验证基本数字是否重复
     * @param i 验证基本数字
     * @param i allowableValues 去重set
     * @param i baseErrorCodeEnum 错误枚举
     * @param i args 参数
     * @return  void
     * @author Long
     * @date: 2022/1/2 20:12
     */
    public  static  void checkIntAllowableValues(Integer i, Set<Integer> allowableValues, BaseErrorCodeEnum errorCodeEnum,Object args){
        if (Objects.nonNull(i) && !allowableValues.contains(i)){
            throw new ServiceException(errorCodeEnum.getErrorCode(),errorCodeEnum.getErrorMsg(),args);
        }
    }
    
    /** 
     * @description: 比较基本数字大小
     * @param i 比较基本数字
     * @param min 对比的最小基本数字
     * @param errorCodeEnum 错误枚举
     * @param args 参数
     * @return  void
     * @author Long
     * @date: 2022/1/2 20:18
     */ 
    public  static void checkIntMin(Integer i,int min,BaseErrorCodeEnum errorCodeEnum,Object args){

        if (Objects.isNull(i) || i < min ){
            throw new ServiceException(errorCodeEnum.getErrorCode(), errorCodeEnum.getErrorMsg(), args);
        }
    }

    /** 
     * @description:  对比集合是否为空
     * @param collection 集合
     * @return  void
     * @author Long
     * @date: 2022/1/2 20:22
     */ 
    public static  void checkCollectionNonEmpty(Collection<?> collection){
        if (CollectionUtils.isEmpty(collection)){
            throw new ServiceException(ErrorCodeEnum.SERVER_ILLEGAL_ARGUMENT_ERROR);
        }
    }

    /**
     * @description: 对比集合是否为空指定对应错误
     * @param collection 集合
     * @param errorCodeEnum  错误枚举
     * @param args 参数
     * @return  void
     * @author Long
     * @date: 2022/1/2 20:24
     */
    public static  void checkCollectionNonEmpty(Collection<?> collection,BaseErrorCodeEnum errorCodeEnum,Object ... args){
        if (CollectionUtils.isEmpty(collection)){
            throw new ServiceException(errorCodeEnum.getErrorCode(), errorCodeEnum.getErrorMsg(), args);
        }
    }
    
    /*** 
     * @description: 对比 两个set 集合 是否相同
     * @param intSet 对比 set 集合
     * @param intSet allowableValues 被对比set 集合
     * @param intSet errorCodeEnum 错误枚举
     * @param intSet args 参数
     * @return  void
     * @author Long
     * @date: 2022/1/2 21:05
     */ 
    public static void checkIntSetAllowableValues(Set<Integer> intSet,Set<Integer> allowableValues,BaseErrorCodeEnum errorCodeEnum,Object ... args){

        if (CollectionUtils.isEmpty(intSet) && !diffSet(intSet,allowableValues).isEmpty()){
            throw new ServiceException(errorCodeEnum.getErrorCode(), errorCodeEnum.getErrorMsg(), args);
        }
    }

    /** 
     * @description: 检查 set 集合 最大数量
     * @param setPram set 集合
     * @param  maxSize 最大数量
     * @param  baseErrorCodeEnum 错误信息
     * @param  args 参数
     * @return  void
     * @author Long
     * @date: 2022/2/26 19:20
     */ 
    public static  void  checkSetMaxSize(Set<?> setPram,Integer maxSize,BaseErrorCodeEnum baseErrorCodeEnum,Object... args){
        if(!CollectionUtils.isEmpty(setPram) &&  setPram.size() > maxSize){
            throw new ServiceException(baseErrorCodeEnum.getErrorCode(),baseErrorCodeEnum.getErrorMsg());
        }

    }
    /***
     * @description: 去重
     * @param intSet 去重 set 集合
     * @param intSet2  被去重的set 集合
     * @return  boolean
     * @author Long
     * @date: 2022/1/2 21:09
     */
    private static Set<Integer> diffSet(Set<Integer> intSet, Set<Integer> intSet2) {

        Set<Integer> result =new HashSet<>();

        result.addAll(intSet);
        result.removeAll(intSet2);

        return result;
    }
}
