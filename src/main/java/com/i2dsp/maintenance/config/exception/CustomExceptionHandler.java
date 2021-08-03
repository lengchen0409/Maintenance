package com.i2dsp.maintenance.config.exception;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.i2dsp.maintenance.config.Enum.ResultEnum;
import com.i2dsp.maintenance.utils.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * description 全局异常处理和请求参数与处理
 * date: 2021-06-28 09:25
 * @author 林隆星
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = Exception.class)
    public ResultVo serverError(Exception e) {
        //异常处理
        String msg = e.getMessage();
        if (e instanceof NullPointerException) {
            msg = "空指针异常";
        }else if (e instanceof HttpMessageNotReadableException) {
            msg = "请检查请求数据是否异常";
        } else if (e instanceof RuntimeException) {
            msg = "运行时发生异常";
        } else if (e instanceof MaxUploadSizeExceededException) {
            msg = "上传文件过大";
        }else if (e instanceof MissingServletRequestParameterException) {
            msg = "缺少请求参数";
        } else {
            msg = "发生未知异常,请联系管理员";
        }
        //异常信息打印
        logger.error("############ {} ############", msg, e);
        return new ResultVo <>(ResultEnum.OTHER_ERROR.getCode(),msg);
    }

    @ExceptionHandler(value = GlobalException.class)
    public ResultVo paramError(GlobalException e) {
        //自定义异常拦截
        logger.warn("############ {} ############",e.getMsg(), e);
        return new ResultVo <>(e.getCode(), e.getMsg());
    }

    /**
     * 处理方法参数校验失败的异常（ConstraintViolationException异常）
     * 设置响应状态码为20401
     * @param ex
     * @return
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResultVo  handleConstraintViolationException(ConstraintViolationException ex) throws JsonProcessingException {
        // 获取所有异常
        List<String> violations = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        //封装异常信息
        String msg = "";
        for (int i = 0; i < violations.size(); i++) {
            msg += violations.get(i) + ";";
        }
//        logger.warn("############ {} ############",msg, ex);
        logger.warn("############ {} ############",msg);
        return new ResultVo <>(ResultEnum.ARGYNENTS_ERROR.getCode(), msg);
    }

    /**
     * 批处理方法参数校验失败的异常（BindException异常）
     * 常规方法参数校验失败的异常（MethodArgumentNotValidException异常）
     * 设置响应状态码为20401
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {BindException.class,MethodArgumentNotValidException.class})
    public ResultVo  handleBindException(BindException ex) throws JsonProcessingException {
        // 获取所有异常
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        //封装异常信息
        String msg = "";
        for (int i = 0; i < fieldErrors.size(); i++) {
            msg += fieldErrors.get(i).getField() + " : " + fieldErrors.get(i).getDefaultMessage() + "   ";
        }
//        logger.warn("############ {} ############",msg, ex);
        logger.warn("############ {} ############",msg);
        return new ResultVo <>(ResultEnum.ARGYNENTS_ERROR.getCode(), msg);
    }

}
