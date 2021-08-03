package com.i2dsp.maintenance.config.exception;

import com.i2dsp.maintenance.config.Enum.ResultEnum;

/**
 * description test contoller
 * date: 2021-06-28 11:45
 *
 * @author 林隆星
 */
public class GlobalException extends RuntimeException {

   private static final long serialVersionUID = 4453214753962022203L;
   private Integer code;
   private String msg;

   public GlobalException() {}

   public GlobalException(int code, String msg) {
       super(msg);
       this.code = code;
       this.msg = msg;
   }

   public GlobalException(String msg) {
       super(msg);
       this.code = ResultEnum.ERROR.getCode();
       this.msg = msg;
   }

    public GlobalException(Throwable cause) {
        super(cause.getMessage(), cause);
        this.msg = cause.getMessage();
    }

   public GlobalException(Throwable cause, String msg) {
       super(msg, cause);
       this.msg = msg;
   }

   public GlobalException(ResultEnum resultEnum, String msg) {
       super(msg);
       this.code = resultEnum.getCode();
       this.msg = msg;
   }

    public GlobalException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
    }



   public GlobalException(Integer code, String msg, Throwable cause) {
       super(msg, cause);
       this.code = code;
       this.msg = msg;
   }

   public Integer getCode() {
       return code;
   }

   public String getMsg() {
       return msg;
   }
}
