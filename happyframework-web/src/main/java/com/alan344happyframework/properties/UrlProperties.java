package com.alan344happyframework.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author AlanSun
 * @date 2020/7/17 15:40
 **/
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "url")
public class UrlProperties {
}
