package com.happyframework.elasticsearch;

import com.alan344happyframework.serializer.GsonLocalDateTimeDeSerializer;
import com.alan344happyframework.serializer.GsonLocalDateTimeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.apache.http.HttpHost;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.elasticsearch.jest.HttpClientConfigBuilderCustomizer;
import org.springframework.boot.autoconfigure.elasticsearch.jest.JestProperties;
import org.springframework.boot.autoconfigure.elasticsearch.jest.JestProperties.Proxy;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Jest.
 *
 * @author Stephane Nicoll
 * @since 1.4.0
 */
@Configuration
@ConditionalOnBean(JestProperties.class)
@EnableConfigurationProperties(JestProperties.class)
@ConditionalOnClass(value = JestClient.class)
public class JestConfig {

    private final JestProperties properties;


    private final ObjectProvider<HttpClientConfigBuilderCustomizer> builderCustomizers;

    public JestConfig(JestProperties properties, ObjectProvider<Gson> gson, ObjectProvider<HttpClientConfigBuilderCustomizer> builderCustomizers) {
        this.properties = properties;
        this.builderCustomizers = builderCustomizers;
    }

    @Bean(destroyMethod = "shutdownClient")
    @ConditionalOnMissingBean
    public JestClient jestClient() {
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(createHttpClientConfig());
        return factory.getObject();
    }

    private HttpClientConfig createHttpClientConfig() {
        HttpClientConfig.Builder builder = new HttpClientConfig.Builder(
                this.properties.getUris());

        //添加自定义Gson序列化
        builder.gson(new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeSerializer())
                .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeDeSerializer())
                .create());

        PropertyMapper map = PropertyMapper.get();
        map.from(this.properties::getUsername).whenHasText().to((username) -> builder
                .defaultCredentials(username, this.properties.getPassword()));
        Proxy proxy = this.properties.getProxy();
        map.from(proxy::getHost).whenHasText().to((host) -> {
            Assert.notNull(proxy.getPort(), "Proxy port must not be null");
            builder.proxy(new HttpHost(host, proxy.getPort()));
        });
        map.from(this.properties::isMultiThreaded).to(builder::multiThreaded);
        map.from(this.properties::getConnectionTimeout).whenNonNull()
                .asInt(Duration::toMillis).to(builder::connTimeout);
        map.from(this.properties::getReadTimeout).whenNonNull().asInt(Duration::toMillis)
                .to(builder::readTimeout);
        customize(builder);
        return builder.build();
    }

    private void customize(HttpClientConfig.Builder builder) {
        this.builderCustomizers.orderedStream()
                .forEach((customizer) -> customizer.customize(builder));
    }

}
