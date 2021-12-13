package com.taki.common.utlis;

import com.taki.common.exception.BaseErrorCodeEnum;
import com.taki.common.exception.ErrorCodeEnum;
import com.taki.common.exception.ErrorCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName ResponseData
 * @Description 统一放回实体类
 * @Author Long
 * @Date 2021/11/24 17:46
 * @Version 1.0
 */
@Data
// 无参构造
@NoArgsConstructor
// 有参构造
@AllArgsConstructor
public class ResponseData<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 请求成功
     */
    private static final boolean REQUEST_SUCCESS = true;

    /**
     * 请求失败
     */
    private static  final  boolean REQUEST_FAIL = false;

    /**
     *默认状态码
     */
    private static final  Integer DEFAULT_CODE = 200;

    /**
     * 请求是否成功
     */
    private Boolean success;

    /**
     * 状态码
     */
    private Integer code;

    /**
     *错误提示语
     */
    private String message;

    /**
     *业务数据
     */
    private T data;




    /**
     * @description:  响应成功
     * @param: data 数据
     * @return: responseData
     * @author Long
     * @date: 2021/11/24 17:52
     */
    public  static <T> ResponseData<T> success(){

        return new ResponseData<T>(REQUEST_SUCCESS,DEFAULT_CODE,null,null);
    }

    /**
     * @description: 响应成功
     * @param: message 自定义信息
     * @param:data 数据
     * @return: responseData
     * @author Long
     * @date: 2021/11/24 17:55
     */
    public  static <T> ResponseData<T> success(T data){

        return new ResponseData<T>(REQUEST_SUCCESS,DEFAULT_CODE,null,data);
    }

    /**
     * @description:  系统错误响应
     * @param: codeEnum 状态码
     * @return: responseData
     * @author Long
     * @date: 2021/11/24 17:57
     */
    public static <T> ResponseData<T> error(BaseErrorCodeEnum codeEnum){
        return new ResponseData<T>(REQUEST_FAIL,codeEnum.getErrorCode(),codeEnum.getErrorMsg(),null);
    }

    /**
     * @description:  系统错误响应
     * @param: codeEnum 状态码
     * @return: responseData
     * @author Long
     * @date: 2021/11/24 17:57
     */
    public static <T> ResponseData<T> error(BaseErrorCodeEnum codeEnum,T data){
        return new ResponseData<T>(REQUEST_FAIL,codeEnum.getErrorCode(),codeEnum.getErrorMsg(),data);
    }

    /**
     * @description:  系统错误响应 自定义错误
     * @param: codeEnum 状态码
     *  @param:  message 异常信息
     * @return: responseData
     * @author Long
     * @date: 2021/11/24 17:57
     */
    public static <T> ResponseData<T> error(Integer code,String message){
        return new ResponseData<T>(REQUEST_FAIL,code,message,null);
    }

}
