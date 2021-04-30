package com.alan344happyframework.config.filter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author AlanSun
 * @date 2020/9/3 10:51
 */
@Configuration
public class FilterRegisterConfig {

    /**
     * 配置 body 可重复读取过滤器
     *
     * @return {@link FilterRegistrationBean}
     */
    @ConditionalOnBean
    @ConditionalOnClass(name = "vip.tuoyang.base.config.ApiCheckProperties")
    @Bean
    public FilterRegistrationBean<BodyRepeatFilter> bodyRepeatFilter() {
        FilterRegistrationBean<BodyRepeatFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setName("bodyRepeatFilter");
        filterRegistrationBean.setFilter(new BodyRepeatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(2);
        return filterRegistrationBean;
    }
}
