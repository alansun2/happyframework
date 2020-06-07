package com.happyframework.tencent.im;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author alan
 * @date 18-8-15 下午12:38 *
 */
@Getter
@Setter
@Component
@ConditionalOnProperty(prefix = "tencent.im", name = "appId")
@ConfigurationProperties(prefix = "tencent.im")
class IMProperties {

    private String appId;

    /**
     * 用户名，调用 REST API 时一般为 App 管理员帐号。
     */
    private String adminIdentifier;

    /**
     * 生成usersig时需要
     */
    private int accountType;

    private String privateKey;

    private String publicKey;
}
