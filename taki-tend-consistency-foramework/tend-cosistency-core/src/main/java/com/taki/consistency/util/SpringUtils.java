package com.taki.consistency.util;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.*;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Map;

/**
 * @ClassName SpringUtils
 * @Description TODO
 * @Author Long
 * @Date 2022/9/3 16:00
 * @Version 1.0
 */
@Component
public class SpringUtils implements BeanFactoryPostProcessor, ApplicationContextAware {


    /**
     * @PostConstruct  注解标记的类中，由于ApplicationContext还未加载，导致空指针
     * 因此实现 BeanFactoryPostProcessor注入ConfigurableListableBeanFactory实现bean的操作
     *
     *
     */
    private static ConfigurableListableBeanFactory beanFactory;

    /**
     * Spring 应用上下文环境
     */
    private static  ApplicationContext applicationContext;





    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        SpringUtils.beanFactory = configurableListableBeanFactory;


    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.applicationContext = applicationContext;
    }

    /*** 
     * @description: 获取{@link ApplicationContext}
     * @param 
     * @return  {@link ApplicationContext}
     * @author Long
     * @date: 2022/9/3 16:41
     */ 
    public static ApplicationContext getApplicationContext () {return  applicationContext;}

    /*** 
     * @description:  获取{@link ListableBeanFactory },可能为{$link  ConfigurableListableBeanFactory} 或 {@link ApplicationContextAware}
     * @param
     * @return  {@link ListableBeanFactory }
     * @author Long
     * @date: 2022/9/3 16:44
     */ 
    public static ListableBeanFactory getBeanFactory() {
        return null == beanFactory ? applicationContext : beanFactory;
    }
    
    /*** 
     * @description:   获取{@link ConfigurableListableBeanFactory }
     * @param 
     * @return  {@link ConfigurableListableBeanFactory }
     * @author Long
     * @date: 2022/9/3 16:47
     */ 
    public static ConfigurableListableBeanFactory getConfigurableBeanFactory(){
        final ConfigurableListableBeanFactory factory;
        if (null !=  beanFactory){
            factory = beanFactory;
        }else if (applicationContext instanceof ConfigurableApplicationContext){
            factory = ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
        }else{
            throw new UtilException("No ConfigurableListableBeanFactory form context !");
        }

        return factory;
    }
    
    /*** 
     * @description:  通过name 获取 Bean
     * @param <T> Bean 类型
     * @param name Bean 名称
     * @return  T
     * @author Long
     * @date: 2022/9/3 16:50
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name){ return (T) getBeanFactory().getBean(name);}
    
    
    /*** 
     * @description: 根据class 获取Bean
     * @param clazz bean类
     * @return  T
     * @author Long
     * @date: 2022/9/3 16:52
     */
    public static <T> T getBean(Class<T> clazz) {
        return  getBeanFactory().getBean(clazz);}

    /***
     * @description: 通过name，以及Clazz返回指定的Bean
     * @param clazz Bean类型
     * @param name bean 名称
     * @return  T
     * @author Long
     * @date: 2022/9/3 16:53
     */
    public static <T> T getBean(String name,Class<T> clazz) { return  getBeanFactory().getBean(name,clazz);}

    /*** 
     * @description: 从spring容器中获取相关降级的bean
     * @param fallbackClass 降级类Class类对象
     * @param paramValues 参数值
     * @return  相关降级的bean
     * @author Long
     * @date: 2022/9/3 16:55
     */ 
    public static  Object getBean(Class<?> fallbackClass,Object[] paramValues){
            return getBeanFactory().getBean(fallbackClass,paramValues);
    }

    /*** 
     * @description:  通过类型参考返回带泛型参数的bean
     * @param reference 类型参考，用于持有转换后的泛型类型
     * @return  带泛型参的bean
     * @author Long
     * @date: 2022/9/3 16:57
     */ 
    public static <T> T getBean(TypeReference<T> reference){
        final ParameterizedType parameterizedType = (ParameterizedType) reference.getType();
        final Class<T> rawType = (Class<T>) parameterizedType.getRawType();
        final Class<?>[] genericTypes = Arrays.stream(parameterizedType.getActualTypeArguments()).map(type -> (Class<?>) type).toArray(Class[]::new);
        final String[] beanNames = getBeanFactory().getBeanNamesForType(ResolvableType.forClassWithGenerics(rawType,genericTypes));
        return getBean(beanNames[0],rawType);

    }

    /*** 
     * @description: 获取指定类型对应的所有的Bean，包括子类
     * @param type 类，接口，null表示获取所有bean
     * @return  java.util.Map<java.lang.String,T>
     * @author Long
     * @date: 2022/9/3 17:08
     */ 
    public static <T> Map<String,T> getBeansOfType(Class<T> type) {

        return getBeanFactory().getBeansOfType(type);

    }
    
    /*** 
     * @description:  获取指定类型对应的bean名称 包括子类
     * @param type 类，接口，null表示获取所有bean名称
     * @return  java.lang.String[]
     * @author Long
     * @date: 2022/9/3 17:10
     */ 
    public static  String[] getBeanNameForType(Class<?> type) {return getBeanFactory().getBeanNamesForType(type);}


    /*** 
     * @description:  获取配置文件配置的值
     * @param key 配置项key
     * @return 属性值
     * @author Long
     * @date: 2022/9/3 17:11
     */ 
    public static   String getProperty(String  key){
        if (null  ==  applicationContext){
            return null;
        }
        return applicationContext.getEnvironment().getProperty(key);
    }

    /*** 
     * @description:  获取应用程序名称
     * @param 
     * @return 应用程序名称
     * @author Long
     * @date: 2022/9/3 17:12
     */ 
    public  static  String getApplicationName(){

        return getProperty("spring.application.name");
    }

    /*** 
     * @description:  获取当前的环境配置，无配置返回null
     * @param
     * @return  当前环境配置
     * @author Long
     * @date: 2022/9/3 17:13
     */ 
    public static   String[] getActiveProfiles(){
        if (null ==  applicationContext){
            return null;
        }
        return applicationContext.getEnvironment().getActiveProfiles();
        
    }
    
    /*** 
     * @description:  获取当前的环境配置，当有多个环境配置时，只获取第一个
     * @param
     * @return  当前的环境配置
     * @author Long
     * @date: 2022/9/3 17:16
     */ 
    public static String getActiveProfile() {
        final String[] activeProfiles = getActiveProfiles();

        return ArrayUtil.isNotEmpty(activeProfiles) ? activeProfiles[0] : null;
    }

    /*** 
     * @description:  动态向  Spring 注册Bean
     * 由{@link org.springframework.beans.factory.BeanFactory} 实现，通过工具开发API
     * @param beanName 名称
     * @param bean bean
     * @return  void
     * @author Long
     * @date: 2022/9/3 17:18
     */ 
    public static <T> void registerBean(String beanName,T bean){
        final ConfigurableListableBeanFactory factory = getConfigurableBeanFactory();
        factory.autowireBean(bean);
        factory.registerSingleton(beanName,bean);
    }

    /*** 
     * @description:  注销bean 将Spring中的Bean注销，请求谨慎使用
     * @param beanName bean名称
     * @return  void
     * @author Long
     * @date: 2022/9/3 17:21
     */ 
    public static void unregisterBean(String beanName){
        final   ConfigurableListableBeanFactory factory = getConfigurableBeanFactory();

        if (factory instanceof DefaultSingletonBeanRegistry) {

            DefaultSingletonBeanRegistry registry = (DefaultSingletonBeanRegistry) factory;
            registry.destroySingleton(beanName);
        }else {
        throw new UtilException("Can  not unregister bean,the factory is not a  DefaultSingletonBeanRegistry !");
        }

    }

    /*** 
     * @description:  发布事件
     * @param event the event to publish
     * @return  void
     * @author Long
     * @date: 2022/9/3 17:25
     */ 
    public static void publishEvent(ApplicationContext  event){

        if(ObjectUtil.isNotEmpty(applicationContext)){
            applicationContext.publishEvent(event);
        }
    }
    
}
