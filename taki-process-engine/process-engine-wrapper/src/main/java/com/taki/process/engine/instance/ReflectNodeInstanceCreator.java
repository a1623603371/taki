package com.taki.process.engine.instance;

import com.taki.process.engine.process.Processor;

/**
 * @ClassName ReflectNodeInstanceCreator
 * @Description 基于反射创建实例
 * ProcessorInstanceCreator，流程里每个节点是如何构建出来的
 *  * 框架是可以自己直接运行的，脱离spring容器，如果是这样子的话，流程定义里的各个节点一般都是通过反射自己构建的
 *  * 如果说是基于spring环境来运行的话，流程定义里的节点，都是基于spring bean实现注入的，作为流程中的节点
 * @Author Long
 * @Date 2023/4/11 17:24
 * @Version 1.0
 */
public class ReflectNodeInstanceCreator implements ProcessorInstanceCreator{
    @Override
    public Processor newInstance(String className, String name) throws Exception {

   Class<?> clazz = Class.forName(className);
    Object o = clazz.newInstance();
    if (!(o instanceof Processor)){
       throw new IllegalArgumentException("类" + className +"不是Processor实例");
    }
    Processor processor = (Processor) o;

    processor.setName(name);

        return processor;
    }
}
