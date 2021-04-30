package com.alan344happyframework.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 取消响应包装(用于自定义包装rest接口响应对象)
 *
 * @author pengxg
 * @date 2021/3/3 6:50 下午
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface SkipRespWrapper {
}
