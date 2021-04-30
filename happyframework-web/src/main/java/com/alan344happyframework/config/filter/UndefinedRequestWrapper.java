package com.alan344happyframework.config.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Enumeration;
import java.util.Objects;

/**
 * https://blog.csdn.net/asd1098626303/article/details/82149129
 */
@Slf4j
public class UndefinedRequestWrapper extends HttpServletRequestWrapper {
    UndefinedRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return super.getParameterNames();
    }

    /**
     * 将undefined入参置为null
     */
    @Nullable
    @Override
    public String[] getParameterValues(@Nullable String name) {
        String[] parameterValues = super.getParameterValues(name);
        if (Objects.nonNull(parameterValues) && isInValidParam(parameterValues[0])) {
            parameterValues[0] = null;
        }
        return parameterValues;
    }

    /**
     * 非法字符:undefined;空字符串
     */
    public static boolean isInValidParam(@Nullable String param) {
        return Objects.isNull(param)
                || "undefined".equalsIgnoreCase(param);
    }
}
