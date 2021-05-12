package com.happyframework.ali.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author AlanSun
 * @date 2021/5/11 17:02
 */
@EnableConfigurationProperties({AliSmsProperties.class})
@Configuration(proxyBeanMethods = false)
public class AliSmsConfig {

    @Autowired
    private AliSmsProperties aliSmsProperties;

    @Bean
    public AliSmsClient aliSmsClient() throws Exception {
        final AliSmsClient aliSmsClient = new AliSmsClient();
        aliSmsClient.setAliSmsProperties(aliSmsProperties);
        aliSmsClient.createClient();
        return aliSmsClient;
    }
}
