package com.taki.common.utli;

import java.time.*;
import java.util.Date;

/**
 * @ClassName DateUtils
 * @Description TODO
 * @Author Long
 * @Date 2022/10/9 22:33
 * @Version 1.0
 */
public class DateUtils {


    /*** 
     * @description:  localDateTime 转date
     * @param dateTime
     * @return  java.util.Date
     * @author Long
     * @date: 2022/10/9 22:35
     */ 
    public static Date convertDate(LocalDateTime dateTime){

        if (dateTime == null) {
            return null;
        }
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /***
     * @description:  date 转 localDateTime
     * @param date
     * @return  java.time.LocalDateTime
     * @author Long
     * @date: 2022/10/9 22:36
     */
    public static  LocalDateTime  convertLocalDateTime(Date date){

        if (date == null){
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /***
     * @description: date 转 localdate
     * @param date
     * @return  java.time.LocalDate
     * @author Long
     * @date: 2022/10/9 22:38
     */
    public static LocalDate convertLocalDate(Date date){

        if(date == null) {
            return null;
        }

        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

    }


    /***
     * @description: date 转 localTime
     * @param date
     * @return  java.time.LocalTime
     * @author Long
     * @date: 2022/10/9 22:57
     */
    public static LocalTime convertLocalTime(Date date){
        if (date == null){
            return null;
        }

        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();

    }


    /***
     * @description: 计算 两个时间的时间差
     * @param start 开始时间
     * @param   end 结束时间
     * @return  java.lang.Long
     * @author Long
     * @date: 2022/10/9 23:00
     */
    public static Long betweenMinutes(Date start,Date end){

        return betweenMinutes(convertLocalDateTime(start),convertLocalDateTime(end));
    }

    /*** 
     * @description:  计算 两个时间的时间差
     * @param start
     * @param  end
     * @return  java.lang.Long
     * @author Long
     * @date: 2022/10/9 22:55
     */ 
    public static Long betweenMinutes(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null ){
            return 0L;
        }
        Duration duration = Duration.between(start,end);


        return duration.toMinutes();
    }
}
