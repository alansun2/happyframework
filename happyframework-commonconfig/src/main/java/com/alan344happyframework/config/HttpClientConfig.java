package com.alan344happyframework.config;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author alan
 * @date 18-6-7 * Apache httpclient 配置
 */
@Configuration
public class HttpClientConfig {

    @Bean
    public HttpClient httpClient(HttpClientBuilder httpClientBuilder) {
        return httpClientBuilder.build();
    }

    @Bean(destroyMethod = "close")
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(400);
        connectionManager.setDefaultMaxPerRoute(200);
        return connectionManager;
    }

    @Bean
    public HttpClientBuilder httpClientBuilder(HttpClientConnectionManager connectionManager) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(10000)
                .setSocketTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .build();
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(connectionManager);
        httpClientBuilder.setDefaultRequestConfig(requestConfig);
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
        return httpClientBuilder;
    }
}
