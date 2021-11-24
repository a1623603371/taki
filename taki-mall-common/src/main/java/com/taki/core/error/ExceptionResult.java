package com.taki.core.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName ExceptionResult
 * @Description 异常信息 返回实体
 * @Author Long
 * @Date 2021/11/24 21:25
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExceptionResult {
    // 时间
    private Date timestamp;
    // 描述
    private String respMsg;
    // 错误异常信息
    private String message;
    // 实际错误异常名称
    private String exceptionName;
    // URL
    private String path;
}
