package com.alan344happyframework.config;

import com.alan344happyframework.exception.BizException;
import com.alan344happyframework.response.AbstractResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * @author AlanSun
 * @date 2016年8月9日
 * 异常统一处理类
 */
@Slf4j
@RestControllerAdvice
public class CommonControllerAdvice implements ResponseBodyAdvice<Object> {
    @Autowired
    private ObjectMapper objectMapper;
    private static String[] SWAGGER_URLS = new String[]{
            "/configuration/ui", "/swagger-resources", "/v2/api-docs", "/configuration/security"
    };

    /**
     * 400 - Bad Request
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Object handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.error("缺少请求参数", ex);
        return new AbstractResponse<Object>("required_parameter_is_not_present") {
        };
    }

    /**
     * 400 - Bad Request
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Object handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("参数解析失败", ex);
        return new AbstractResponse<Object>("could_not_read_json") {
        };
    }

    /**
     * 捕获BizException
     */
    @ExceptionHandler(BizException.class)
    public Object handleBizExec(HttpServletRequest request, BizException ex) {
        return new AbstractResponse<Object>(ex.getErrorCode(), ex.getMessage()) {
        };
    }

    /**
     * 捕获MethodArgumentNotValidException
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException ex) {
        return new AbstractResponse<Object>(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage()) {
        };
    }

    /**
     * 参数类型不匹配
     */
    @ExceptionHandler(TypeMismatchException.class)
    public AbstractResponse handleTypeMismatchException(TypeMismatchException ex) {
        return new AbstractResponse<Object>("参数类型不匹配") {
        };
    }

    /**
     * 不支持的请求方法请求
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public AbstractResponse<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return new AbstractResponse<String>(0, ex.getMessage()) {
        };
    }

    /**
     * 参数错误
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public AbstractResponse<String> handleHttpRequestMethodNotSupportedException(IllegalArgumentException ex) {
        return new AbstractResponse<String>(0, ex.getMessage()) {
        };
    }

    /**
     * 捕获BindException
     */
    @ExceptionHandler(BindException.class)
    public Object bindExceptionException(HttpServletRequest request, BindException ex) {
        return new AbstractResponse<Object>(ex.getAllErrors().get(0).getDefaultMessage()) {
        };
    }

    /**
     * 捕获UndeclaredThrowableException
     */
    @ExceptionHandler(UndeclaredThrowableException.class)
    public Object handleUndeclaredThrowableException(HttpServletRequest request, UndeclaredThrowableException ex) {
        log.error("error", ex);
        Throwable cause = ex.getUndeclaredThrowable().getCause();
        if (cause instanceof BizException) {
            BizException exception = (BizException) cause;
            return new AbstractResponse<Object>(0, exception.getMessage()) {
            };
        }
        return new AbstractResponse<Object>(0, ex.getMessage()) {
        };
    }

    /**
     * 捕获Exception
     */
    @ExceptionHandler(RuntimeException.class)
    public Object handleException(HttpServletRequest request, RuntimeException ex) {
        log.error("error", ex);
        return new AbstractResponse<Object>(500, "系统异常") {
        };
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(@Nullable Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (contains(serverHttpRequest.getURI().getPath(), SWAGGER_URLS) || o instanceof AbstractResponse) {
            return o;
        } else {
            AbstractResponse<Object> response = new AbstractResponse<Object>() {
            };
            response.setResultCode(200);
            response.setData(o);
            if (o instanceof String) {
                try {
                    return objectMapper.writeValueAsString(response);
                } catch (JsonProcessingException e) {
                    log.error("jackson解析出错", e);
                }
            }
            return response;
        }
    }

    /**
     * 判断是否包含数组中的字符串
     */
    private static boolean contains(String source, String[] urls) {
        for (String url : urls) {
            if (source.contains(url)) {
                return true;
            }
        }

        return false;
    }
}