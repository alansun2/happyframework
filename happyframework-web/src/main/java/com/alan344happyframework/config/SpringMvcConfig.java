package com.alan344happyframework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * @author AlanSun
 * @date 2020/7/20 21:36
 * <p>
 * web 配置
 **/
@Configuration
public class SpringMvcConfig {

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }
}
