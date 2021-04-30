package com.alan344happyframework.config.feign;

import feign.Feign;
import feign.Logger;
import feign.Retryer;
import feign.querymap.BeanQueryMapEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author AlanSun
 * @date 2020/8/27 12:31
 */
@Configuration
public class FeignGlobalConfig {
    /**
     * 日志级别
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        //这里记录所有，根据实际情况选择合适的日志level
        return Logger.Level.FULL;
    }

    /**
     * 替换解析 queryMap 的类，实现父类中变量的映射
     */
    @Bean
    @Scope("prototype")
    public Feign.Builder feignBuilder() {
        return Feign.builder()
                .queryMapEncoder(new BeanQueryMapEncoder())
                .retryer(Retryer.NEVER_RETRY);
    }

}
