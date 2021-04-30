package com.alan344happyframework.common;

import com.alan344happyframework.bean.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * @author AlanSun
 * @date 2020/7/16 17:02
 * <p>
 * 通用返回处理类
 **/
@ConditionalOnProperty(prefix = "system", name = "enable-common-res", havingValue = "true", matchIfMissing = true)
@Slf4j
@ControllerAdvice
public class CommonResControllerAdvice implements ResponseBodyAdvice<Object> {

    private static final HashSet<String> IGNORE_METHOD = new HashSet<>();
    private static final HashSet<String> IGNORE_PATH = new HashSet<>();

    static {
        IGNORE_METHOD.add("openapiJson");
        IGNORE_PATH.add("/swagger-resources/**");
        IGNORE_PATH.add("/v2/api-docs");
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        if (methodParameter.getMethod() == null || IGNORE_METHOD.contains(methodParameter.getMethod().getName()) || skipSupport(methodParameter)) {
            return false;
        }
        return aClass == MappingJackson2HttpMessageConverter.class;
    }

    private boolean skipSupport(MethodParameter methodParameter) {
        return Arrays.stream(methodParameter.getDeclaringClass().getAnnotations()).anyMatch(annotation -> annotation instanceof SkipRespWrapper)
                || Arrays.stream(methodParameter.getMethod().getAnnotations()).anyMatch(annotation -> annotation instanceof SkipRespWrapper);
    }

    @Override
    public Object beforeBodyWrite(@Nullable Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (o instanceof CommonResponse || skipWrapper(serverHttpRequest)) {
            return o;
        } else {
            CommonResponse<Object> response = new CommonResponse<>();
            if (o == null && List.class.isAssignableFrom(methodParameter.getParameterType())) {
                // 当返回的 list 为 null 时返回空数组
                response.setData(Collections.emptyList());
            } else {
                response.setData(o);
            }

            return response;
        }
    }

    private boolean skipWrapper(ServerHttpRequest serverHttpRequest) {
        AntPathMatcher matcher = new AntPathMatcher();
        return IGNORE_PATH.stream().anyMatch(p -> matcher.match(p, ((ServletServerHttpRequest) serverHttpRequest).getServletRequest().getRequestURI()));
    }
}
