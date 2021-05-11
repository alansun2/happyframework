package com.happyframework.ali.sms;

import com.happyframework.ali.AliBase;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author alan
 * @date 18-8-10 下午3:01 *
 */
@Getter
@Setter
@Component
@ConditionalOnProperty(prefix = "ali.sms", name = "signName")
@ConfigurationProperties(prefix = "ali.sms")
class AliSmsProperties extends AliBase {
    private String signName;
}
