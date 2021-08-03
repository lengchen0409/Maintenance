package com.i2dsp.maintenance.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 连接池配置类
 * @author : 梁海聪
 * @since : 2021/07/13
 */
@Configuration
public class DruidConfig {

    //将所有前缀为spring.datasource下的配置项都加载到DataSource中
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean(name = "dataSource" ,initMethod = "init" ,destroyMethod = "close")
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }

    //配置Druid的监控
    //配置一个管理后台的servlet
    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");

        Map<String, String> initParams = new HashMap<>();
        //账号
        initParams.put("loginUsername", "admin");
        //密码
        initParams.put("loginPassword", "123456");

        //后台允许所有访问
        initParams.put("allow", "");

        bean.setInitParameters(initParams);
        return bean;
    }

    //配置一个监控的filter     webStatFilter: 用于配置web和Druid数据源之间的管理关联监控统计
    public FilterRegistrationBean webStatFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());

        Map<String, String> initParams = new HashMap<>();
        //exclusions: 设置哪些请求进行过滤排除掉,从而不进行统计
        initParams.put("exclusions", "*.js,*.css,/druid/*,/jdbc/*");
        bean.setInitParameters(initParams);

        // “/*” 表示过滤所有请求
        bean.setUrlPatterns(Arrays.asList("/*"));
        return bean;
    }

}
