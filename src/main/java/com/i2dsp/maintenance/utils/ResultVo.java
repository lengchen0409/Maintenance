package com.i2dsp.maintenance.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.i2dsp.maintenance.config.Enum.ResultEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * description 请求响应类
 * date: 2021-06-30 13:35
 *
 * @author 林隆星
 */
@Data
public class ResultVo<T> implements Serializable {

    private static final long serialVersionUID = -3999803560577989187L;

    private Integer code;

    private String message;

    @JsonInclude(JsonInclude.Include.ALWAYS)
    private T data;

    public ResultVo() {
        super();
    }

    public ResultVo(Integer code) {
        super();
        this.message = ResultEnum.getMsgByCode(code);
        this.code = code;
    }
    public ResultVo(ResultEnum resultEnum) {
        super();
        this.message = resultEnum.getMsg();
        this.code = resultEnum.getCode();
    }

    public ResultVo(ResultEnum resultEnum, String msg) {
        super();
        this.message = msg;
        this.code = resultEnum.getCode();
    }

    public ResultVo(ResultEnum resultEnum, T data) {
        super();
        this.message = resultEnum.getMsg();
        this.code = resultEnum.getCode();
        this.data = data;
    }

    public ResultVo(T data) {
        super();
        this.code = ResultEnum.OK.getCode();
        this.data = data;
        this.message = ResultEnum.getMsgByCode(code);
    }

    public ResultVo(Integer code, String message) {
        super();
        this.code = code;
        this.message = message;
        this.data = null;
    }

    public ResultVo(Integer code, Throwable throwable) {
        super();
        this.code = code;
        this.message = throwable.getMessage();
    }

    public ResultVo(Integer code, String message, T data) {
        super();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ResultVo<?> other = (ResultVo<?>) obj;
        if (data == null) {
            if (other.data != null) {
                return false;
            }
        } else if (!data.equals(other.data)) {
            return false;
        }
        if (message == null) {
            if (other.message != null) {
                return false;
            }
        } else if (!message.equals(other.message)) {
            return false;
        }
        if (code == null) {
            if (other.code != null) {
                return false;
            }
        } else if (!code.equals(other.code)) {
            return false;
        }
        return true;
    }
}
