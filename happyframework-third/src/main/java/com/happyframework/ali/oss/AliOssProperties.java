package com.happyframework.ali.oss;

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
@ConditionalOnProperty(prefix = "ali.oss", name = "url")
@ConfigurationProperties(prefix = "ali.oss")
class AliOssProperties extends AliBase {

    private String url;

    private String bucketName;
    /**
     * Endpoint以杭州为例，其它Region请按实际情况填写。
     */
    private String endPoint;

    private String prefix;
}
