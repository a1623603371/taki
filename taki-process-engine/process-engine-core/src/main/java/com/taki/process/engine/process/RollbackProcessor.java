package com.taki.process.engine.process;

/**
 * @ClassName RollbackProcessor
 * @Description TODO
 * @Author Long
 * @Date 2023/4/10 23:04
 * @Version 1.0
 */
public  abstract class RollbackProcessor extends AbstractProcessor {


    /*** 
     * @description:  回滚操作
     * @param context
     * @return  void
     * @author Long
     * @date: 2023/4/11 20:32
     */ 
    protected abstract void rollback(ProcessContext context);


}
