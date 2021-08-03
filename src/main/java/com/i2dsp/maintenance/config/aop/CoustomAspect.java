package com.i2dsp.maintenance.config.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.i2dsp.maintenance.utils.ResultVo;
import io.minio.credentials.AssumeRoleBaseProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * description Controller 层 aop增强类
 * date: 2021-06-28 10:56
 *
 * @author 林隆星
 */

@Aspect
@Component
@Slf4j
public class CoustomAspect {

    /**
     * poincut:切入点
     */
    @Pointcut("execution(* com.i2dsp.maintenance.controller.*.*(..))")
    public void pointcut() {
    }

    /**
     * 切入点之前执行
     */
    @Before(value = "pointcut()")
    public void before(JoinPoint jp) {
    }

//    /**
//     * 切入点之后执行
//     */
//    @After(value = "pointcut()")
//    public void after(JoinPoint jp) {
//
//    }
//
//    /**
//     * 切入点之前和之后执行
//     */
//    @AfterReturning(value = "pointcut()", returning = "result")
//    public void afterReturning(JoinPoint jp, Object result) {
//
//    }
//
//    /**
//     * 切入点之前执行
//     */
//    @AfterThrowing(value = "pointcut()", throwing = "ex")
//    public void afterThrowing(JoinPoint jp, Exception ex) {
//
//    }

    /**
     * 相当于MethodInterceptor,可以统计controller接口方法的执行时间
     *
     * @param pjp
     * @throws Throwable
     */
    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        String method = request.getMethod();//请求方式为GET/POST
        String methodName = pjp.getSignature().getName();//请求controller层里面的方法名
        String controller = pjp.getTarget().getClass().getName();//请求的是哪个controller
        String uri = request.getRequestURI();//请求接口名
        String paraString = JSON.toJSONString(request.getParameterMap());//请求参数 只能接收@RequestParam注解的参数值
        String params = getParams(paraString);
        //如果@RequestParam的请求参数全部为空就进去
        if ("{}".equals(paraString)) {
            //如果用的是@RequestBody注解且用的是在如:Student对象上面
            //如果用的是@RequestBody注解
            List<Object> paramObjects = Arrays.stream(pjp.getArgs()).collect(Collectors.toList());
            params = paramObjects.toString();
        }
//        log.debug("********************请求开始*********************");
        log.debug("URI         : {}", uri);
        log.debug("Controller  : {}", controller + getTargetStack(controller));
        log.debug("method      : {}", method);
        log.debug("MethodName  : {}", methodName);
        log.debug("params      : {}", params);
        log.debug("------------------------------------------------------------------------------------");

        // result的值就是被拦截方法的返回值
        Object result = pjp.proceed();//获得拦截方法的返回值
//        log.info("请求结束，controller的返回值是 " + JSON.toJSONString(result));

        return result;//这里如果将拦截方法的返回值改为result=null,前端接收到的就是空的,就会变的没有返回值
    }

    //用StringBuffer定位调用的controller层 定位第一行
    private static String getTargetStack(String tag) {
        StringBuffer sb = new StringBuffer();
        int number = tag.lastIndexOf(".");//找到controller名称中最后一个.的索引
        String controller = tag.substring(number + 1);//截取controller的名字
        sb.append(".(" + controller).append(".java:1)");//定位在controller类里面的第一行
        return sb.toString();
    }

    //获取当前时间
    private String getDate() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    //处理注解为@RequestParam 将请求参数变为name=张三这种格式
    private String getParams(String params) {
        JSONObject object = JSONObject.parseObject(params);
        Set<String> keys = object.keySet();
        String myValue = "";
        String value = "";
        for (String key : keys) {
            value = object.getString(key);//取出来的值为["张三"]这样 不能用replace替换,怕请求参数里面可能会有json数据比如[{},{}]这种类型的数据
            int firstNum = value.indexOf("\"");//第一个引号的索引
            int lastNum = value.lastIndexOf("\"");//最后一个引号的索引
            value = value.substring(firstNum + 1, lastNum);
            myValue += key + " = " + value + "  ";
        }
        return myValue;
    }
}
