package com.taki.common.utlis;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @ClassName ExceptionUtils
 * @Description 异常处理工具类
 * @Author Long
 * @Date 2021/11/25 14:20
 * @Version 1.0
 */
public class ExceptionUtils {

    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
