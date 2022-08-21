package com.taki.wms.Interceptor;

import com.taki.wms.annotation.LoginRequired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @ClassName MyLoginInterceptor
 * @Description TODO
 * @Author Long
 * @Date 2022/8/21 15:02
 * @Version 1.0
 */
public class MyLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("MyLoginInterceptor3.preHandle");
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)){
            return true;
        }

        //方法注解级别拦截
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 判断接口是否需要登录，也可用 method.isAnnotationPresent(LoginRequired.class);
        LoginRequired  methodAnnotation = method.getAnnotation(LoginRequired.class);
        // 有@LoginRequired 注解需要认证
        if (methodAnnotation != null){

            System.out.println("权限判断等");
            return  true;
        }


        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("MyLoginInterceptor3.postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("MyLoginInterceptor3.afterCompletion");
    }
}
