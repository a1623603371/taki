package com.taki.common.utlis;

import com.taki.common.constants.DateFormatConstants;
import javafx.scene.input.DataFormat;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName DateFormatUtils
 * @Description 日期格式工具类
 * @Author Long
 * @Date 2022/1/2 23:01
 * @Version 1.0
 */
public class DateFormatUtils {

    private static DateFormat dateFormat = new SimpleDateFormat(DateFormatConstants.DATE_FORMAT_PATTERN);

    private static  DateFormat dateTimeFormat = new SimpleDateFormat(DateFormatConstants.DATE_TIME_FORMAT_PATTERN);

    private static DateFormat timeFormat = new SimpleDateFormat(DateFormatConstants.TIME_FORMAT_PATTERN);

    /** 
     * @description: 日期格式化(不包含时间) yyyy-MM-dd
     * @param date 时间
     * @return 日期格式
     * @author Long
     * @date: 2022/1/2 23:17
     */ 
    public static String format(Date date){
        return dateFormat.format(date);
    }

    /** 
     * @description: 日期格式化（包含时间） yyyy-MM-dd HH:mm:ss
     * @param date
     * @return  年月日时间格式
     * @author Long
     * @date: 2022/1/2 23:21
     */ 
    public static String formatDateTime(Date date){
        return  dateTimeFormat.format(date);
    }

    /** 
     * @description: 日期格式化（不包含年月日） yyyy-MM-dd HH:mm:ss
     * @param date
     * @return  时间格式
     * @author Long
     * @date: 2022/1/2 23:23
     */ 
    public static String formatTime(Date date){
        return timeFormat.format(date);
    }

    /**
     * @description: 日期格式
     * @param date 日期
     * @param pattern 自定义格式
     * @return
     * @author Long
     * @date: 2022/1/2 23:25
     */
    public static String format(Date date,String pattern){
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    return simpleDateFormat.format(date);
    }

}
