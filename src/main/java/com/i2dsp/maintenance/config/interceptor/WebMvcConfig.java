package com.i2dsp.maintenance.config.interceptor;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * description 自定义MVC配置
 * date: 2021-06-16 10:27
 *
 * @author 林隆星
 */
@Component
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 自定义拦截
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加过滤器
        registry.addInterceptor(new CoustomInterceptor())
                //拦截路径
                .addPathPatterns("/**")
                .excludePathPatterns("")
                //order的数值越小，优先级越高
                .order(1);
    }

    /**
     * 跨域拦截
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //请求方法
                .allowedMethods("*")
                //请求头
                .allowedHeaders("*")
                //有效期
                .maxAge(1800)
                //支持跨的域
                .allowedOrigins("*")
                //允许跨域发送cookie
                .allowCredentials(true)
                //maxAge(3600)表明在3600秒内，不需要再发送预检验请求，可以缓存该结果
                .exposedHeaders(HttpHeaders.SET_COOKIE).maxAge(3600L);
        WebMvcConfigurer.super.addCorsMappings(registry);
    }


}
