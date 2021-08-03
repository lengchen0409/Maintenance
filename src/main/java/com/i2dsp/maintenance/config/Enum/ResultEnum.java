package com.i2dsp.maintenance.config.Enum;


import org.springframework.util.StringUtils;

/**
 * description test contoller
 * date: 2021-06-28 11:45
 *
 * @author 林隆星
 */
public enum ResultEnum  {
    /**
     *自定义状态码
     */
    OK(20000, "业务请求成功"),
    ERROR(20400,"业务请求失败"),
    OTHER_ERROR(20500, "未知异常"),
    ARGYNENTS_ERROR(20401,"请求参数异常"),
    BATCHING_ERROR(20402,"批处理异常"),
    OPENFEIGN_ERROR(20403,"远程调用异常"),
    /**
     *httpq请求状态码
     */
    STATUS_CODE_400(400,"请求无效"),
    STATUS_CODE_403(403,"禁止访问"),
    STATUS_CODE_404(404,"请求的资源不存在"),
    STATUS_CODE_405(405,"资源被禁止"),
    STATUS_CODE_500(500,"内部服务器错误,请联系管理员");


    private final Integer code;
    private final String msg;

    ResultEnum (Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static String getMsgByCode(Integer code) {
        if (!StringUtils.isEmpty(code)) {
            ResultEnum [] var1 = values();
            int var2 = var1.length;
            for (ResultEnum  errorEnum : var1) {
                if (errorEnum.getCode().equals(code)) {
                    return errorEnum.msg;
                }
            }
        }
        return "未知异常-" + code;
    }


}
