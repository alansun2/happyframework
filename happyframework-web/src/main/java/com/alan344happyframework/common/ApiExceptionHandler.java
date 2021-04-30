package com.alan344happyframework.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vip.tuoyang.base.bean.response.CommonResponse;
import vip.tuoyang.base.constants.BaseErrorCode;
import vip.tuoyang.base.exception.BizException;
import vip.tuoyang.base.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.UndeclaredThrowableException;


/**
 * 全局异常处理
 *
 * @author demon
 */
@Order
@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    private static final String MESSAGE = "请求地址:{}";
    private static final String ERROR = "error";

    /**
     * 捕获 BindException，用于 validation，如 @NotNull
     */
    @ExceptionHandler(BindException.class)
    public CommonResponse<Object> handleBindException(BindException ex) {
        String defaultMessage = ex.getAllErrors().get(0).getDefaultMessage();
        final String bizException = "BizException";
        if (StringUtils.isNotEmpty(defaultMessage) && defaultMessage.contains(bizException)) {
            defaultMessage = defaultMessage.substring(defaultMessage.indexOf(bizException));
        }
        log.error(MESSAGE, defaultMessage);
        return new CommonResponse<>(defaultMessage);
    }

    /**
     * 参数类型不匹配
     */
    @ExceptionHandler(TypeMismatchException.class)
    public CommonResponse<Object> handleTypeMismatchException(TypeMismatchException ex, HttpServletRequest request) {
        log.error(ERROR, ex);
        log.error(MESSAGE, request.getRequestURI());
        return new CommonResponse<>(BaseErrorCode.BAD_REQUEST);
    }

    /**
     * 参数不符合要求
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse<Object> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.error(ERROR, ex);
        log.error(MESSAGE, request.getRequestURI());
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder errorMessage = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMessage.append(fieldError.getDefaultMessage()).append("<br>");
        }
        errorMessage.delete(errorMessage.lastIndexOf("<br>"), errorMessage.length());
        return new CommonResponse<>(BaseErrorCode.BAD_REQUEST.getCode(), errorMessage.toString());
    }

    /**
     * 请求的格式错误，一般是 json 格式错误
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public CommonResponse<Object> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.error(ERROR, ex);
        log.error(MESSAGE, request.getRequestURI());
        return new CommonResponse<>(BaseErrorCode.BAD_REQUEST);
    }

    /**
     * 请求参数校验异常:当 ex.getMessage()等同于ErrorMsgConstants.PARAM_ERROR时说明是参数校验异常,否则是其他异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public CommonResponse<Object> handleIllegalArgumentsException(IllegalArgumentException ex, HttpServletRequest request) {
        log.error(ERROR, ex);
        if (BaseErrorCode.INTERNAL_PARAM_VALID_ERROR.getMessage().equals(ex.getMessage())) {
            log.error(MESSAGE, request.getRequestURI());
            return new CommonResponse<>(BaseErrorCode.BAD_REQUEST);
        } else {
            return new CommonResponse<>(BaseErrorCode.BAD_REQUEST.getCode(), ex.getMessage());
        }
    }

    /**
     * 捕获 UndeclaredThrowableException
     */
    @ExceptionHandler(UndeclaredThrowableException.class)
    public CommonResponse<Object> handleUndeclaredThrowableException(UndeclaredThrowableException ex) {
        log.error(ERROR, ex);
        Throwable cause = ex.getUndeclaredThrowable();
        if (cause instanceof BizException) {
            BizException exception = (BizException) cause;
            return new CommonResponse<>(exception.getErrorCode(), exception.getMessage());
        }
        return new CommonResponse<>(ex.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(BizException.class)
    public CommonResponse<Object> bizExceptionError(BizException ex) {
        return new CommonResponse<>(ex);
    }

    /**
     * 捕获Exception
     */
    @ExceptionHandler(Exception.class)
    public CommonResponse<Object> handleException(Exception ex, HttpServletRequest request) {
        log.error(ERROR, ex);
        log.error(MESSAGE, request.getRequestURI());
        return new CommonResponse<>(BaseErrorCode.INTERNAL_SERVER_ERROR);
    }
}
