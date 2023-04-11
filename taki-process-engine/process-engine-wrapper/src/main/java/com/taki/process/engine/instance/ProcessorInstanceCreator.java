package com.taki.process.engine.instance;

import com.taki.process.engine.process.Processor;

/**
 * @ClassName ProcessorInstanceCreator
 * @Description 流程节点实例器
 * @Author Long
 * @Date 2023/4/10 22:39
 * @Version 1.0
 */
public interface ProcessorInstanceCreator {

    /***
     * @description: 创建节点
     * @param className 类名称
     * @param name 节点 id
     * @return  com.taki.process.engine.process.Processor
     * @author Long
     * @date: 2023/4/11 17:21
     */
    Processor newInstance(String className,String name) throws Exception;

}
