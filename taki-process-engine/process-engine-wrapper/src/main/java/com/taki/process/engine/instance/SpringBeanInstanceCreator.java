package com.taki.process.engine.instance;

import com.taki.process.engine.model.ProcessContextFactory;
import com.taki.process.engine.process.Processor;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;

/**
 * @ClassName SpringBeanInstanceCreator
 * @Description TODO
 * @Author Long
 * @Date 2023/4/11 17:31
 * @Version 1.0
 */
@Component
public class SpringBeanInstanceCreator implements ProcessorInstanceCreator{
    @Override
    public Processor newInstance(String className, String name) throws Exception {
        Object bean;

        try {
            Class<?> clazz = Class.forName(className);
            bean =  SpringUtils.getBean(clazz);

        }catch (BeansException e){
            return ProcessContextFactory.DEFAULT_INSTANCE_CREATOR.newInstance(className,name);
        }

        if ((bean instanceof   Processor)){
            throw new IllegalArgumentException("类" +  className +"不是Processor实例");
        }

        Processor processor = (Processor) bean; // 接口类型的强转，所有的流程节点都是实现了Processor接口的
        // 拿出的每个流程点的bean实例，都可以强转为Processor接口类型，设置进去流程节点对应的name就可以
        processor.setName(name);

        return processor;
    }
}
