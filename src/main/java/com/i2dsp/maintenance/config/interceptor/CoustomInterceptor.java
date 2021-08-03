package com.i2dsp.maintenance.config.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 应用场景
 * 1、日志记录，可以记录请求信息的日志，以便进行信息监控、信息统计等。
 * 2、权限检查：如登陆检测，进入处理器检测是否登陆，如果没有直接返回到登陆页面。
 * 3、性能监控：典型的是慢日志。
 * description 自定义拦截器
 * date: 2021-06-16 10:19
 *
 * @author 林隆星
 */
@Slf4j
public class CoustomInterceptor  implements HandlerInterceptor {
//    public class CoustomInterceptor  extends HandlerInterceptorAdapter {

    private NamedThreadLocal<Long> threadLocal = new NamedThreadLocal<>("CoustomInterceptor");
    /**
     * preHandle->Controllers
     * 在业务处理器处理请求之前被调用
     * 在Controller前执行，可以进行请求拦截校验
     * 预处理回调方法，实现处理器的预处理（如检查登陆），第三个参数为响应的处理器，自定义Controller
     *返回值：true表示继续流程（如调用下一个拦截器或处理器）；false表示流程中断（如登录检查失败），不会继续调用其他的拦截器或处理器，此时我们需要通过response来产生响应；
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        //当前请求开始时间
        threadLocal.set(System.currentTimeMillis());
        log.info("*************** " +request.getMethod() +"请求url:" + request.getHeader("Host") + request.getRequestURI() + "  开始执行 ***************");
        return true;
    }

    /**
     * 在业务处理器处理请求完成之后，生成视图之前执行，可以对ModelAndView进行操作
     * 执行顺序在Controller之后，前提是preHandle返回值为TRUE
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     */
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {

    }

    /**
     * 执行顺序在postHandle之后，前提是preHandle的返回值为TRUE
     * 在DispatcherServlet完全处理完请求之后被调用，可用于清理资源
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long endTime = System.currentTimeMillis();
        long startTime = threadLocal.get();
        long elapsed = endTime - startTime;
        log.info("*************** " +request.getMethod() +"请求url:" + request.getHeader("Host") + request.getRequestURI() + "  执行耗时：" + elapsed +"ms ***************");
    }
}
