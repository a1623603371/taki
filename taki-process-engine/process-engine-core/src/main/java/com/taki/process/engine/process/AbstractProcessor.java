package com.taki.process.engine.process;

/**
 * @ClassName AbstractProcessor
 * @Description 抽象流程，提供了流程包装功能
 * template method
 * 通过定义一个process方法，定义了各个子方法的运行流程h额顺序
 *各个子方法都提供了一个空实现，但具体的实现给你的子类覆盖实现
 *对于子类来说，自把需要执行的子方法覆盖，具体方法运行流程h额调用顺序，都交给抽象类父类
 * @Author Long
 * @Date 2023/4/10 22:41
 * @Version 1.0
 */

public abstract class AbstractProcessor implements Processor {

    private String name;


    @Override
    public void process(ProcessContext processContext) {
        beforeProcess(processContext);
        processInternal(processContext);
        afterProcess(processContext);

    }


    /*** 
     * @description:  流程前操作
     * @param context 上下文
     * @return  void
     * @author Long
     * @date: 2023/4/11 15:21
     */ 
    protected  void beforeProcess(ProcessContext context){
        
    }

    /*** 
     * @description:  流程后操作
     * @param context 上下文
     * @return  void
     * @author Long
     * @date: 2023/4/11 15:23
     */ 
    private void afterProcess(ProcessContext context){
        
    }

    /*** 
     * @description:  流程核心逻辑
     * @param context
     * @return  void
     * @author Long
     * @date: 2023/4/11 15:22
     */ 
    protected  abstract void processInternal(ProcessContext context);

    @Override
    public void caughtException(ProcessContext context, Throwable throwable) {

    }

    @Override
    public void setName(String name) {

    }

    @Override
    public String getName() {
        return null;
    }
}
