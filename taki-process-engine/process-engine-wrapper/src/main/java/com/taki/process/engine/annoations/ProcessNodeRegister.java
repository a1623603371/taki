package com.taki.process.engine.annoations;

import com.taki.process.engine.config.ClassPathXmlProcessParser;
import com.taki.process.engine.model.ProcessContextFactory;
import com.taki.process.engine.model.ProcessorModel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ProcessNodeRegister
 * @Description TODO
 * @Author Long
 * @Date 2023/4/11 21:27
 * @Version 1.0
 */
@Slf4j
public class ProcessNodeRegister implements ImportBeanDefinitionRegistrar {


    @SneakyThrows
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        try {
            String configFile = (String) annotationMetadata.getAnnotationAttributes(EnableProcessEngine.class.getName()).get("value");
            ClassPathXmlProcessParser classPathXmlProcessParser = new ClassPathXmlProcessParser(configFile);
            List<ProcessorModel> processorModelList = classPathXmlProcessParser.parse();
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(ProcessContextFactory.class);
            beanDefinitionBuilder.addConstructorArgValue(new ArrayList<>(processorModelList));
            beanDefinitionBuilder.addConstructorArgReference("springBeanInstanceCreator"); // 依赖和引用spring容器里的其他bean，springBeanInstanceCreator
            registry.registerBeanDefinition(ProcessContextFactory.class.getName(),beanDefinitionBuilder.getBeanDefinition());
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
