package com.taki.common.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @ClassName DruidConfig
 * @Description  Druid数据库连接池配置
 * Druid 具有以下特点：
 * 亚秒级 OLAP 查询，包括多维过滤、Ad-hoc 的属性分组、快速聚合数据等等。
 * 实时的数据消费，真正做到数据摄入实时、查询结果实时。
 * 高效的多租户能力，最高可以做到几千用户同时在线查询。
 * 扩展性强，支持 PB 级数据、千亿级事件快速处理，支持每秒数千查询并发。
 * 极高的高可用保障，支持滚动升级。
 *
 *
 * druid监控访问地址： http://ip:port/context-path/druid/index.html
 * eg：http://127.0.0.1:8080/demo/druid/login.html （这里的IP需要是上面配置文件中白名单指定的哦,多个可以使用逗号分割）
 *
 *
 * @Author Long
 * @Date 2021/11/24 15:54
 * @Version 1.0
 */
@Configuration
public class DruidConfig {

    /**
     * @description: servlet 注册 bean
     * @param:
     * @return:
     * @author Long
     * @date: 2021/11/24 16:08
     */
    @Bean
    public ServletRegistrationBean druidServlet(){
        // 进行druid 监控配置处理操作
        ServletRegistrationBean servletRegistrationBean =
                new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
        // 设置白名单
        servletRegistrationBean.addInitParameter("allow","127.0.0.1,10.1.1.1");
        // 设置黑名单
        servletRegistrationBean.addInitParameter("deny","192.168.33.15");
        // 设置登陆账号
        servletRegistrationBean.addInitParameter("loginUsername","admin");
        // 设置登陆密码
        servletRegistrationBean.addInitParameter("loginPassword","password");
        return  servletRegistrationBean;
    }

    /**
     * @description: 过滤器 注册bean
     * @param:
     * @return:
     * @author Long
     * @date: 2021/11/24 16:08
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        //所有请求进行监控
        filterRegistrationBean.addUrlPatterns("/*");

        filterRegistrationBean.addInitParameter("exclusions","*.js,*.gif,*jpg,*.css,/druid/*");
        return filterRegistrationBean;
    }

    /**
     * @description: 数据源
     * @param:
     * @return:
     * @author Long
     * @date: 2021/11/24 16:10
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource(){
        return new DruidDataSource();
    }
}
