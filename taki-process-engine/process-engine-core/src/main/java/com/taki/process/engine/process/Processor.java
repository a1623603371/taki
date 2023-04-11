package com.taki.process.engine.process;

/**
 * @ClassName Processor
 * @Description TODO
 * @Author Long
 * @Date 2023/4/10 22:40
 * @Version 1.0
 */
public interface Processor {
    /*** 
     * @description: 执行逻辑
     * @param processContext 上下文
     * @return  void
     * @author Long
     * @date: 2023/4/11 14:55
     */ 
    void process(ProcessContext processContext);



    /*** 
     * @description:  触发异常
     * @param context 上下文
     * @param  throwable 异常
     * @return  void
     * @author Long
     * @date: 2023/4/11 14:55
     */ 
    void caughtException(ProcessContext context,Throwable  throwable);


    /*** 
     * @description: 设置名字
     * @param name 名字
     * @return  void
     * @author Long
     * @date: 2023/4/11 14:57
     */ 
    void setName(String name);
    
    /*** 
     * @description:  获取名字
     * @param 
     * @return  java.lang.String
     * @author Long
     * @date: 2023/4/11 17:24
     */ 
    String getName();
}
