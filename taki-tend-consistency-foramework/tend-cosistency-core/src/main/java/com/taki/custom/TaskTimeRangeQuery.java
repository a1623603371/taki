package com.taki.custom;

import java.time.LocalDateTime;

/**
 * @ClassName TaskTimeRangeQuery
 * @Description 任务执行时间范围查询器接口
 * 如果业务范围需要定制，实现该接口
 * @Author Long
 * @Date 2022/8/31 23:41
 * @Version 1.0
 */
public interface TaskTimeRangeQuery {

    /*** 
     * @description:  获取查询任务的初始化时间
     * @param 
     * @return  java.time.LocalDateTime
     * @author Long
     * @date: 2022/8/31 23:42
     */ 
    LocalDateTime getStartTime();

    /***
     * @description:  获取查询任务结束时间
     * @param
     * @return  java.time.LocalDateTime
     * @author Long
     * @date: 2022/8/31 23:43
     */
    LocalDateTime getEndTime();

    /*** 
     * @description:  每次最多查询对少个未完成的任务处理
     * @param 
     * @return  java.lang.Long
     * @author Long
     * @date: 2022/8/31 23:44
     */ 
    Long limitTaskCount();

    /*** 
     * @description: 如果没有实现类，框架默认实现，获取查询任务的初始化时间
     * @param 
     * @return  java.time.LocalDateTime
     * @author Long
     * @date: 2022/8/31 23:45
     */ 
    static LocalDateTime getStartTimeByStatic(){

        return LocalDateTime.now().minusDays(1);
    }

    /*** 
     * @description:  如果没有实现类，框架默认实现，获取查询任务的结束时间
     * @param
     * @return  java.time.LocalDateTime
     * @author Long
     * @date: 2022/8/31 23:48
     */ 
    static LocalDateTime  getEndTimeByStatic(){

        return LocalDateTime.now();
    }

    /*** 
     * @description:  如果没有实现类，框架默认，每次查询未完成的任务限制查询条数
     * @param 
     * @return  java.lang.Long
     * @author Long
     * @date: 2022/9/1 22:08
     */ 
    static Long limitTaskCountByStatic(){
            
        return 1000L;
    }
}
