package com.taki.wms.config;

import com.taki.wms.filter.MyFilter;
import com.taki.wms.filter.MyFilter1;
import com.taki.wms.filter.MyFilter2;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName FilterConfig
 * @Description TODO
 * @Author Long
 * @Date 2022/8/14 17:58
 * @Version 1.0
 */
@Configuration
public class FilterConfig {


    @Bean
    public FilterRegistrationBean registrationBean(){
       FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
       filterRegistrationBean.setFilter(myFilter());
       filterRegistrationBean.addServletNames("MyFilter");
       filterRegistrationBean.addUrlPatterns("/*");
       return filterRegistrationBean;
    }




    @Bean
    public FilterRegistrationBean registrationBean1(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(myFilter1());
        filterRegistrationBean.addServletNames("MyFilter1");
        filterRegistrationBean.setOrder(3);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean registrationBean2(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(myFilter2());
        filterRegistrationBean.addServletNames("MyFilter2");
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }


    @Bean
    public MyFilter myFilter(){

        return new MyFilter();
    }

    @Bean
    public MyFilter1 myFilter1(){

        return new MyFilter1();
    }

    @Bean
    public MyFilter2 myFilter2(){

        return new MyFilter2();
    }




}
