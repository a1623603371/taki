package com.taki.process.engine.model;

import com.taki.process.engine.instance.ProcessorInstanceCreator;
import com.taki.process.engine.instance.ReflectNodeInstanceCreator;
import com.taki.process.engine.node.ProcessorDefinition;
import com.taki.process.engine.process.ProcessContext;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName ProcessContextFactory
 * @Description TODO
 * @Author Long
 * @Date 2023/4/10 22:37
 * @Version 1.0
 */
@Slf4j
public class ProcessContextFactory {

    public static final ProcessorInstanceCreator DEFAULT_INSTANCE_CREATOR = new ReflectNodeInstanceCreator();

    private List<ProcessorModel> modelList;

    private final Map<String, ProcessorDefinition> processorDefinitionMap = new ConcurrentHashMap<>();

    private final ProcessorInstanceCreator instanceCreator;

    public ProcessContextFactory(List<ProcessorModel> processorModels, ProcessorInstanceCreator processorInstanceCreator) throws Exception {
        this.modelList = processorModels;
        this.instanceCreator = processorInstanceCreator;
        init();
    }
    
    /*** 
     * @description: 初始化
     * @param 
     * @return  void
     * @author Long
     * @date: 2023/4/11 20:37
     */ 
    private void init() throws Exception {

        modelList.forEach(model->{
            model.check();
        });

        for (ProcessorModel processorModel : modelList) {
            ProcessorDefinition processorDefinition = processorModel.build(instanceCreator);
            log.info("构造流程成功：{}",processorDefinition.toStr());
            processorDefinitionMap.put(processorDefinition.getName(),processorDefinition);
        }

    }
    /*** 
     * @description:
     * @param name
     * @return  com.taki.process.engine.process.ProcessContext
     * @author Long
     * @date: 2023/4/11 21:06
     */ 
    public ProcessContext getContext(String  name){
        ProcessorDefinition processorDefinition = processorDefinitionMap.get(name);
        if (processorDefinition == null){
            throw new IllegalArgumentException("流程不存在");
        }
        return new ProcessContext(processorDefinition); // 只有一次流程实例运行
        // 把ProcessorDefinition实际可执行的流程给了他，此时在运行过程中，这套流程是不会变化的，这是一套对象，是不会变的
        // 就是直接运行就可以了
        // 如果流程还没开始的话，完成了新流程定义的解析，此时在map里拿到的就是新的一套流程定义对象
        // 按照新流程定义去运行，一边运行流程，一边刷新流程定义是没问题的
        
    }

    /*** 
     * @description: 刷新
     * @param processorModels
     * @return  void
     * @author Long
     * @date: 2023/4/11 21:10
     */ 
  public void refresh(List<ProcessorModel> processorModels) throws Exception {

      synchronized (this){
            this.modelList = processorModels;
            init();
      }
  }

}
