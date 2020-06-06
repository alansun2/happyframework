package com.alan344happyframework.util;

import com.alan344happyframework.util.bean.HttpParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author AlanSun
 * @date 2019-07-01
 */
@Slf4j
public class HttpClientUtils {
    /**
     * 池化管理
     */
    private static Map<String, HttpClient> httpClientMap = new HashMap<>();

    private static ResponseHandler<String> responseHandler = new BasicResponseHandler();

    /**
     * 把证书放入map，keyStorepass为key
     */
    private static Map<String, SSLConnectionSocketFactory> sslsfMap = new HashMap<>();

    private static SSLConnectionSocketFactory getSslsf(String keyStorePath, String keyStorepass) throws Exception {
        if (!sslsfMap.containsKey(keyStorepass)) {
            SSLConnectionSocketFactory sslsf = SslUtils.getSSL(keyStorePath, keyStorepass);
            sslsfMap.put(keyStorepass, sslsf);
            return sslsf;
        } else {
            return sslsfMap.get(keyStorepass);
        }
    }

    /**
     * 初始化http配置
     *
     * @param keyStorePath 证书地址
     * @param keyStorepass 证书密码
     */
    private static PoolingHttpClientConnectionManager getConnectionManager(String keyStorePath, String keyStorepass) {
        // 初始化连接管理器
        PoolingHttpClientConnectionManager connectionManager = null;
        try {
            if (StringUtils.isNotEmpty(keyStorepass)) {
                // 配置同时支持 HTTP 和 HTPPS
                Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().register(
                        "http", PlainConnectionSocketFactory.getSocketFactory()).register(
                        "https", getSslsf(keyStorePath, keyStorepass)).build();
                connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
                // 将最大连接数增加到200，实际项目最好从配置文件中读取这个值
                connectionManager.setMaxTotal(1000);

                // 设置最大路由
                connectionManager.setDefaultMaxPerRoute(2);
            }
        } catch (Exception e) {
            log.error("httpClient init error", e);
        }

        return connectionManager;
    }

    /**
     * 获取基础配置
     *
     * @return {@link RequestConfig}
     */
    private static RequestConfig getRequestConfig() {
        // 根据默认超时限制初始化requestConfig
        int socketTimeout = 10000;
        int connectTimeout = 10000;
        int connectionRequestTimeout = 10000;
        return RequestConfig.custom()
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectTimeout)
                .build();
    }

    /**
     * 获取连接
     *
     * @param keyStorePath 证书地址
     * @param keyStorepass 证书密码
     * @return {@link CloseableHttpClient}
     */
    private static HttpClient getConnection(String keyStorePath, String keyStorepass) {
        if (httpClientMap.containsKey(keyStorePath)) {
            return httpClientMap.get(keyStorePath);
        } else {
            PoolingHttpClientConnectionManager connectionManager = getConnectionManager(keyStorePath, keyStorepass);
            if (connectionManager != null && connectionManager.getTotalStats() != null) {
                log.info("now client pool {}", connectionManager.getTotalStats().toString());
            }
            HttpClient httpClient = HttpClients.custom()
                    // 设置连接池管理
                    .setConnectionManager(connectionManager)
                    // 设置请求配置
                    .setDefaultRequestConfig(getRequestConfig())
                    //关闭重试
                    .disableAutomaticRetries()
                    // 设置重试次数
//                .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
                    .build();
            httpClientMap.put(keyStorePath, httpClient);
            return httpClient;
        }
    }

    /**
     * post
     *
     * @param httpParams http参数
     */
    public static HttpResponse doPost(HttpParams httpParams) throws IOException {
        HttpPost post = new HttpPost(httpParams.getUrl());
        if (httpParams.getHeaders() != null) {
            post.setHeaders(httpParams.getHeaders());
        }
        if (httpParams.getStrEntity() != null) {
            StringEntity se = new StringEntity(httpParams.getStrEntity(), StandardCharsets.UTF_8);
            post.setEntity(se);
        }
        // Send the post request and get the response

        return getConnection(null, null).execute(post);
    }

    /**
     * post
     *
     * @param httpParams http参数
     */
    public static HttpResponse doPostWithSsl(String keyStorePath, String keyStorepass, HttpParams httpParams) throws IOException {
        HttpPost post = new HttpPost(httpParams.getUrl());
        if (httpParams.getHeaders() != null) {
            post.setHeaders(httpParams.getHeaders());
        }
        if (httpParams.getStrEntity() != null) {
            StringEntity se = new StringEntity(httpParams.getStrEntity(), StandardCharsets.UTF_8);
            post.setEntity(se);
        }
        // Send the post request and get the response

        return getConnection(keyStorePath, keyStorepass).execute(post);
    }

    /**
     * post
     *
     * @param httpParams http参数
     */
    public static <T> T doPostWithResponseHandler(HttpParams httpParams, ResponseHandler<T> responseHandler) throws IOException, HttpException {
        if (responseHandler == null) {
            throw new HttpException("responseHandler 不能为空");
        }
        HttpPost post = new HttpPost(httpParams.getUrl());
        if (httpParams.getHeaders() != null) {
            post.setHeaders(httpParams.getHeaders());
        }
        if (httpParams.getStrEntity() != null) {
            StringEntity se = new StringEntity(httpParams.getStrEntity(), StandardCharsets.UTF_8);
            post.setEntity(se);
        }
        // Send the post request and get the response

        return getConnection(null, null).execute(post, responseHandler);
    }

    /**
     * post
     *
     * @param httpParams http参数
     */
    public static <T> T doPostWithSslAndResponseHandler(String keyStorePath, String keyStorepass, HttpParams httpParams, ResponseHandler<T> responseHandler) throws IOException, HttpException {
        if (responseHandler == null) {
            throw new HttpException("responseHandler 不能为空");
        }

        if (StringUtils.isEmpty(keyStorePath) || StringUtils.isEmpty(keyStorepass)) {
            throw new HttpException("keyStorePath or keyStorepass can not be null");
        }

        HttpPost post = new HttpPost(httpParams.getUrl());
        if (httpParams.getHeaders() != null) {
            post.setHeaders(httpParams.getHeaders());
        }
        if (httpParams.getStrEntity() != null) {
            StringEntity se = new StringEntity(httpParams.getStrEntity(), StandardCharsets.UTF_8);
            post.setEntity(se);
        }
        // Send the post request and get the response

        return getConnection(keyStorePath, keyStorepass).execute(post, responseHandler);
    }

    /**
     * 执行GET请求
     *
     * @param keyStorePath 证书地址
     * @param keyStorepass 证书密码
     * @param httpParams   参数
     * @return response
     * @throws IOException e
     */
    public static String doGet(String keyStorePath, String keyStorepass, HttpParams httpParams) throws IOException {
        // 创建http GET请求
        HttpGet httpGet = new HttpGet(httpParams.getUrl());
        if (httpParams.getHeaders() == null) {
            httpGet.setHeaders(httpParams.getHeaders());
        }
        return getConnection(keyStorePath, keyStorepass).execute(httpGet, responseHandler);
    }

    public static void main(String[] str) throws IOException, NoSuchAlgorithmException, KeyManagementException {
/*        HttpParams params = new HttpParams();
        params.setUrl("https://www.baidu.com");
        BasicHeader[] headers = new BasicHeader[1];
        BasicHeader header = new BasicHeader("User-Agent", "Mozilla/5.0");
        headers[0] = header;
        params.setHeaders(headers);
        String closeableHttpResponse = doPost(null, null, false, params);
        params.setHeaders(headers);
        CloseableHttpResponse closeableHttpResponse = doPost(params);
        int i = 0;*/
        HttpParams httpParams = HttpParams.builder().url("https://www.baidu.com").build();
        try {
            for (int i = 0; i < 10; i++) {
                String s = doGet(null, null, httpParams);
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}