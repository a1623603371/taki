package com.taki.common.bean;


import org.springframework.context.ApplicationContext;

/**
 * @ClassName SpringApplication
 * @Description  spring IOC 容器 上下文
 * @Author Long
 * @Date 2021/12/13 14:36
 * @Version 1.0
 */
public class SpringApplicationContext{

    /**
     *spring 容器
     */
    private ApplicationContext applicationContext;

    /**
     * @description: 构造方法
     * @param: applicationContext spring 容器
     * @return:
     * @author Long
     * @date: 2021/12/13 14:38
     */
    public SpringApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    /**
     * @description:
     * @param: clazz
     * @return: T
     * @author Long
     * @date: 2021/12/13 14:39
     */
  public <T> T getBean(Class<? extends T> clazz) {
    return applicationContext.getBean(clazz);
  }
}
