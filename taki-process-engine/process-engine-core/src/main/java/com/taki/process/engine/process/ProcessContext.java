package com.taki.process.engine.process;

import com.taki.process.engine.node.ProcessorDefinition;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @ClassName ProcessContext
 * @Description 流程上下文
 * @Author Long
 * @Date 2023/4/10 22:46
 * @Version 1.0
 */
@Slf4j
public class ProcessContext {
    private final Map<String,Object> params = new HashMap<>();


    private final ProcessorDefinition processorDefinition;

    /**
     *每个线程都有自己的ProcessorsContext ，一个ProcessorContext 只有一个线程驱动一次流程实例运行的时候才会访问
     *
     */
    private final Stack<RollbackProcessor> rollbackProcessors = new Stack<>();

    public ProcessContext(ProcessorDefinition processorDefinition) {
        this.processorDefinition = processorDefinition;
    }
}
