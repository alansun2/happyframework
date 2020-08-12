package com.alan344happyframework.exception;

/**
 * @author AlanSun
 * @date 2020/8/12 17:06
 */
public class InnerException extends RuntimeException {
    public InnerException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
