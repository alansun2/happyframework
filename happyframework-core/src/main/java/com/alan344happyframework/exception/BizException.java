package com.alan344happyframework.exception;


import com.alan344happyframework.constants.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author AlanSun
 * @date 2017年3月24日 下午2:32:30
 */
@Getter
@Setter
@NoArgsConstructor
public class BizException extends RuntimeException {

    private static final long serialVersionUID = -5328505822127772820L;

    private int errorCode;

    private String errorMsg;

    private Object data;

    public BizException(Exception e) {
        super(e);
    }

    public BizException(int errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
        this.errorMsg = msg;
    }

    public BizException(int errorCode, String msg, Object data) {
        super(msg);
        this.errorCode = errorCode;
        this.errorMsg = msg;
        this.data = data;
    }

    public BizException(int errorCode, String msg, Throwable cause) {
        super(msg, cause);
        this.errorCode = errorCode;
        this.errorMsg = msg;
    }

    public BizException(String msg) {
        super(msg);
        this.errorMsg = msg;
    }

    public BizException(ErrorCode errorCode) {
        this.errorCode = errorCode.getCode();
        this.errorMsg = errorCode.getMessage();
    }
}