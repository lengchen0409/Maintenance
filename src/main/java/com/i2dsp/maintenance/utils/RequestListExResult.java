package com.i2dsp.maintenance.utils;

import lombok.Data;

import java.util.List;

/**
 * description 批处理请求异常响应类
 * date: 2021-06-30 13:35
 *
 * @author 林隆星
 */
@Data
public class RequestListExResult<T> {
    /**
     * 批处理总条数
     */
    private Integer total;

    /**
     * 成功处理条数
     */
    private Integer success;

    /**
     *处理异常条数
     */
    private Integer error;

    /**
     * 异常请求返回
     */
    private List<T> errorRequestList;

    public RequestListExResult(){
        this.total = 0;
        this.success = 0;
        this.error = 0;
    }

    public void addError(){
        this.error++;
    }
    public void addSuccess(){
        this.success++;
    }
}
