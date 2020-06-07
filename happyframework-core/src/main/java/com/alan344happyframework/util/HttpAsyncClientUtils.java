package com.alan344happyframework.util;

import com.alan344happyframework.util.bean.HttpParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHeader;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * @author AlanSun
 * @date 2019-07-02
 */
@Slf4j
public class HttpAsyncClientUtils {
    /**
     * 请求器的配置
     */
    private static RequestConfig requestConfig;

    /**
     * 异步请求
     */
    private static CloseableHttpAsyncClient httpAsyncClient;

    /**
     * 连接池
     */
    private static PoolingNHttpClientConnectionManager poolConnManager;

    /**
     * 绕过验证
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }

    public static CloseableHttpAsyncClient getHttpAsyncClient() {
        if (httpAsyncClient == null) {
            try {
                log.info("初始化HttpClientTest~~~开始");
                //绕过证书验证，处理https请求
                SSLContext sslcontext = createIgnoreVerifySSL();

                // 设置协议http和https对应的处理socket链接工厂的对象
                Registry<SchemeIOSessionStrategy> sessionStrategyRegistry = RegistryBuilder.<SchemeIOSessionStrategy>create()
                        .register("http", NoopIOSessionStrategy.INSTANCE)
                        .register("https", new SSLIOSessionStrategy(sslcontext))
                        .build();
                //配置io线程
                IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                        .setIoThreadCount(Runtime.getRuntime().availableProcessors())
                        .build();
                //设置连接池大小
                ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);

                // 初始化连接管理器
                poolConnManager = new PoolingNHttpClientConnectionManager(ioReactor, null, sessionStrategyRegistry);

                // 将最大连接数增加到200，实际项目最好从配置文件中读取这个值
                poolConnManager.setMaxTotal(1000);

                // 设置最大路由
                poolConnManager.setDefaultMaxPerRoute(2);

                // 根据默认超时限制初始化requestConfig
                int socketTimeout = 10000;
                int connectTimeout = 10000;
                int connectionRequestTimeout = 10000;
                requestConfig = RequestConfig.custom()
                        .setConnectionRequestTimeout(connectionRequestTimeout)
                        .setSocketTimeout(socketTimeout)
                        .setConnectTimeout(connectTimeout)
                        .build();

                // 初始化httpClient
                httpAsyncClient = getConnection();

                log.info("初始化HttpAsyncClientTest~~~结束");
            } catch (NoSuchAlgorithmException | KeyManagementException | IOReactorException e) {
                log.info("init httpAsyncClient error", e);
            }
        }
        return httpAsyncClient;
    }

    /**
     * 获取异步请求客户端
     *
     * @return 连接
     */
    public static CloseableHttpAsyncClient getConnection() {
        CloseableHttpAsyncClient httpClient = HttpAsyncClients.custom()
                // 设置连接池管理
                .setConnectionManager(poolConnManager)
                // 设置请求配置
                .setDefaultRequestConfig(requestConfig)
                .build();

        if (poolConnManager != null && poolConnManager.getTotalStats() != null) {
            log.info("now client pool " + poolConnManager.getTotalStats().toString());
        }

        return httpClient;
    }

    /**
     * post
     *
     * @param httpParams http参数
     */
    public static void doAsyncPost(HttpParams httpParams) {
        HttpPost post = new HttpPost(httpParams.getUrl());
        if (httpParams.getHeaders() != null) {
            post.setHeaders(httpParams.getHeaders());
        }
        if (httpParams.getStrEntity() != null) {
            StringEntity se = new StringEntity(httpParams.getStrEntity(), StandardCharsets.UTF_8);
            post.setEntity(se);
        }
        try {
            final CloseableHttpAsyncClient finalHttpAsyncClient = getHttpAsyncClient();
            finalHttpAsyncClient.start();
            execute(finalHttpAsyncClient, post);
        } catch (Exception e) {
            log.error("doAsyncPost error", e);
        }
        // Send the post request and get the response
    }

    /**
     * post
     *
     * @param httpParams http参数
     */
    public static void doAsyncPut(HttpParams httpParams) {
        HttpPut put = new HttpPut(httpParams.getUrl());
        if (httpParams.getHeaders() != null) {
            put.setHeaders(httpParams.getHeaders());
        }
        if (httpParams.getStrEntity() != null) {
            StringEntity se = new StringEntity(httpParams.getStrEntity(), StandardCharsets.UTF_8);
            put.setEntity(se);
        }
        try {
            final CloseableHttpAsyncClient finalHttpAsyncClient = getHttpAsyncClient();
            finalHttpAsyncClient.start();
            execute(finalHttpAsyncClient, put);
        } catch (Exception e) {
            log.error("doAsyncPut error", e);
        }
        // Send the put request and get the response
    }

    /**
     * 执行GET请求
     *
     * @param httpParams httpParams
     */
    public static void doAsyncGet(HttpParams httpParams) {
        // 创建http GET请求
        HttpGet httpGet = new HttpGet(httpParams.getUrl());
        BasicHeader[] headers = httpParams.getHeaders();
        if (headers != null && headers.length > 0) {
            httpGet.setHeaders(headers);
        }
        try {
            final CloseableHttpAsyncClient finalHttpAsyncClient = getHttpAsyncClient();
            httpAsyncClient.start();
            // 执行请求
            execute(finalHttpAsyncClient, httpGet);
        } catch (Exception e) {
            log.error("doAsyncGet error", e);
        }
    }

    /**
     * 执行
     *
     * @param finalHttpAsyncClient 异步客户端
     * @param httpRequestBase      请求
     */
    public static void execute(final CloseableHttpAsyncClient finalHttpAsyncClient, HttpRequestBase httpRequestBase) {
        final AsyncHandler handler = new AsyncHandler();
        // Send the post request and get the response
        finalHttpAsyncClient.execute(httpRequestBase, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse httpResponse) {
                String body = "";
                //这里使用EntityUtils.toString()方式时会大概率报错，原因：未接受完毕，链接已关
                try {
                    HttpEntity entity = httpResponse.getEntity();
                    if (entity != null) {
                        try (InputStream instream = entity.getContent()) {
                            final StringBuilder sb = new StringBuilder();
                            final char[] tmp = new char[1024];
                            final Reader reader = new InputStreamReader(instream, StandardCharsets.UTF_8);
                            int l;
                            while ((l = reader.read(tmp)) != -1) {
                                sb.append(tmp, 0, l);
                            }
                            body = sb.toString();
                        } finally {
                            EntityUtils.consume(entity);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handler.completed(body);
//                close(finalHttpAsyncClient);
            }

            @Override
            public void failed(Exception e) {
                handler.failed(e);
//                close(finalHttpAsyncClient);
            }

            @Override
            public void cancelled() {
                handler.cancelled();
//                close(finalHttpAsyncClient);
            }
        });
    }

    /**
     * 关闭client对象
     *
     * @param client
     */
    private static void close(CloseableHttpAsyncClient client) {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] str) {
        BasicHeader[] headers = new BasicHeader[1];
        BasicHeader header = new BasicHeader("User-Agent", "Mozilla/5.0");
        headers[0] = header;
        HttpParams params = HttpParams.builder().url("https://www.baidu.com").headers(headers).build();
        doAsyncGet(params);
    }
}

@Slf4j
class AsyncHandler {

    public Object failed(Exception e) {
        log.error(Thread.currentThread().getName() + "--失败了--" + e.getClass().getName() + "--" + e.getMessage());
        return null;
    }

    public Object completed(String respBody) {
        log.warn(Thread.currentThread().getName() + "--获取内容：" + respBody);
        return null;
    }

    public Object cancelled() {
        log.warn(Thread.currentThread().getName() + "--取消了");
        return null;
    }
}

