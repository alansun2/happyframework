package com.happyframework.baidu.location;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author AlanSun
 * @date 2019/1/22 18:01
 **/
@Getter
@Setter
@Component
@ConditionalOnProperty(prefix = "baidu.map", name = "ak")
@ConfigurationProperties(prefix = "baidu.map")
class BaiduMapProperties {
    private String ak;
}
