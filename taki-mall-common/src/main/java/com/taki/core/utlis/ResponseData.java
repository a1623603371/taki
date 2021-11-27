package com.taki.core.utlis;

import com.taki.core.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class ResponseData<T> {

    private Integer code;

    private String message;

    private T data;

    /**
     * @description: 解析响应码
     * @param: codeEnum
     * @return: void
     * @author Long
     * @date: 2021/11/24 17:49
     */
    private void parserEnum(CodeEnum codeEnum){
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMessage();

    }

    /**
     * @description:  响应成功
     * @param: data 数据
     * @return: responseData
     * @author Long
     * @date: 2021/11/24 17:52
     */
    public  static <T> ResponseData<T> success(T data){
        ResponseData<T> responseData = new ResponseData<>();
        responseData.parserEnum(CodeEnum.SUCCESS);
        responseData.setData(data);
        return responseData;
    }

    /**
     * @description: 响应成功
     * @param: message 自定义信息
     * @param:data 数据
     * @return: responseData
     * @author Long
     * @date: 2021/11/24 17:55
     */
    public  static <T> ResponseData<T> success(String message,T data){
        ResponseData<T> responseData = new ResponseData<>();
        responseData.setCode(CodeEnum.SUCCESS.getCode());
        responseData.setMessage(message);
        responseData.setData(data);
        return responseData;
    }

    /**
     * @description:  系统错误响应
     * @param: codeEnum 状态码
     *  @param:  data 数据 可以为null 或者异常信息
     * @return: responseData
     * @author Long
     * @date: 2021/11/24 17:57
     */
    public static <T> ResponseData<T> error(CodeEnum codeEnum,T data){
        ResponseData<T> responseData = new ResponseData<>();
        responseData.parserEnum(codeEnum);
        responseData.setData(data);
        return responseData;
    }
    /**
     * @description:
     * @param: codeEnum 状态码
     * @param: message  自定义错误信息
     *  @param:data
     * @return:
     * @author Long
     * @date: 2021/11/24 18:01
     */
    public  static <T> ResponseData<T>  generator(int code,String message,T data){
        ResponseData<T> responseData = new ResponseData<>();
        responseData.setCode(code);
        responseData.setMessage(message);
        responseData.setData(data);
        return responseData;
    }
}
