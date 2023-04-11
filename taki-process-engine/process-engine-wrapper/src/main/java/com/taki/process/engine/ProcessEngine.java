package com.taki.process.engine;

import com.taki.process.engine.model.ProcessContextFactory;
import com.taki.process.engine.process.ProcessContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ProcessEngie
 * @Description TODO
 * @Author Long
 * @Date 2023/4/11 21:16
 * @Version 1.0
 */
@Slf4j
public class ProcessEngine {

    private final ProcessContextFactory factory;

    public ProcessEngine(ProcessContextFactory factory) {
        this.factory = factory;
    }

    /*** 
     * @description:  获取一个流程上下文
     * @param name
     * @return  com.taki.process.engine.process.ProcessContext
     * @author Long
     * @date: 2023/4/11 21:25
     */ 
    public ProcessContext getContext(String name){

        return factory.getContext(name);


    }
}
