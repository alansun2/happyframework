package com.alan344happyframework.properties;

import com.alan344happyframework.common.CommonResControllerAdvice;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author AlanSun
 * @date 2020/8/20 11:10
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "system")
public class SystemProperties {
    /**
     * 是否进行 token 校验
     */
    private boolean tokenCheck = true;

    /**
     * 是否开启通用的返回结果封装，详情见：{@link CommonResControllerAdvice}
     */
    private boolean enableCommonRes = true;

    /**
     * token 过期时间，单位;秒
     */
    private Long tokenExpire;

    /**
     * 拦截器 exclude
     */
    private String[] webInterceptorExclude;

    /**
     * 时区
     */
    private Integer timeZone = 8;

    /**
     * json mapping 在 HttpMessageConverter 中位置
     */
    private Integer jsonMappingIndex = 0;

    public String[] getWebInterceptorExclude() {
        final List<String> strings;
        if (webInterceptorExclude == null) {
            strings = new ArrayList<>();
        } else {
            strings = Arrays.stream(webInterceptorExclude).collect(Collectors.toList());
        }
        // 登录放行
        strings.add("/login/single-login");
        strings.add("/login/password");

        // swagger放行
        strings.add("/doc.html");
        strings.add("/swagger-ui.html/**");
        strings.add("/swagger-resources/**");
        strings.add("/v2/**");
        strings.add("/webjars/**");
        strings.add("/swagger**");
        strings.add("/index.html");
        strings.add("/template/**");
        return strings.toArray(new String[0]);
    }
}
