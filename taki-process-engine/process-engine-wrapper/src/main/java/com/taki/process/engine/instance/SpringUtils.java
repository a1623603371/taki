package com.taki.process.engine.instance;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * @ClassName SpringUtils
 * @Description TODO
 * @Author Long
 * @Date 2023/4/11 17:32
 * @Version 1.0
 */
public class SpringUtils  implements BeanFactoryPostProcessor, ApplicationContextAware {

    /**
     * @PostCons
     */
    private static ConfigurableListableBeanFactory beanFactory;

    /**
     * spring 应用上下文
     */
    private  static ApplicationContext applicationContext;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        SpringUtils.beanFactory = configurableListableBeanFactory;


    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        SpringUtils.applicationContext = applicationContext;

    }

    /*** 
     * @description:  获取{@link ListableBeanFactory}，可能为{@link ConfigurableListableBeanFactory} 或 {@link ApplicationContextAware}
     * @param 
     * @return  org.springframework.beans.factory.ListableBeanFactory
     * @author Long
     * @date: 2023/4/11 19:00
     */ 
    public static ListableBeanFactory getBeanFactory(){

        return  null == beanFactory ? applicationContext : beanFactory;

    }

    /*** 
     * @description: 通过 class 获取 bean
     * @param clazz
     * @return  T
     * @author Long
     * @date: 2023/4/11 19:01
     */ 
    public static <T> T getBean(Class<?> clazz){

        return (T) beanFactory.getBean(clazz);
    }

    /***
     * @description: 根据bean名称 查询 bean
     * @param name
     * @return  T
     * @author Long
     * @date: 2023/4/11 19:02
     */
    public static <T> T getBean(String name){
        return (T) beanFactory.getBean(name);
    }

    /*** 
     * @description:  通过name，以及clazz 返回指定bean
     * @param name
clazz
     * @return  T
     * @author Long
     * @date: 2023/4/11 19:03
     */ 
    public static <T> T getBean(String name,Class<T> clazz){
            return beanFactory.getBean(name,clazz);
    }

    /*** 
     * @description:  从spring 容器中获取相关降级bean
     * @param fallbackClass 降级 类对象
     * @param paramValues 参数值
     * @return  T
     * @author Long
     * @date: 2023/4/11 19:04
     */ 
    public static Object getBean(Class<?> fallbackClass,Object[] paramValues){

        return  getBeanFactory().getBean(fallbackClass,paramValues);
    }

    /*** 
     * @description: 获取指定类型对应所有bean 包括子类
     * @param type
     * @return  java.util.Map<java.lang.String,T>
     * @author Long
     * @date: 2023/4/11 19:07
     */ 
    public static <T>Map<String,T> getBeansOfType(Class<T> type){

        return getBeanFactory().getBeansOfType(type);
    }

    /***
     * @description: 获取配置文件配置乡的值
     * @param type
     * @return  java.lang.String[]
     * @author Long
     * @date: 2023/4/11 19:08
     */
    public static String[] getBeanNamesForType(Class<?> type){

        return getBeanFactory().getBeanNamesForType(type);
    }

    /*** 
     * @description:  获取配置文件配置值项
     * @param 
     * @return  java.lang.String
     * @author Long
     * @date: 2023/4/11 20:23
     */ 
    public static String getProperty(String key){
        if (null ==  applicationContext){
            return null;
        }

        return applicationContext.getEnvironment().getProperty(key);
    }

}
