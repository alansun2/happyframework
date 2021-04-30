package com.alan344happyframework.bean.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import vip.tuoyang.base.constants.ErrorCode;
import vip.tuoyang.base.exception.BizException;

import java.time.LocalDateTime;

/**
 * @author AlanSun
 * @date 2020/7/16 16:56
 * <p>
 * 公共返回数据包装层
 **/
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CommonResponse<T> {
    /**
     * 通用的异常 code
     */
    public static final int COMMON_ERROR = 1;
    public static final int COMMON_SUCCESS = 0;
    /**
     * 状态码，0正常，其他是异常
     */
    private int code = 0;
    /**
     * 错误信息
     */
    private String message;
    /**
     * 数据
     */
    private T data;
    /**
     * 系统时间
     */
    private LocalDateTime systemTime = LocalDateTime.now();

    public CommonResponse(T data) {
        this.data = data;
    }

    public CommonResponse(String message) {
        this.code = COMMON_ERROR;
        this.message = message;
    }

    public CommonResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public CommonResponse(BizException ex) {
        this.code = ex.getErrorCode();
        this.message = ex.getErrorMsg();
    }

    public CommonResponse(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public CommonResponse(ErrorCode errorCode, T data) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.data = data;
    }
}
