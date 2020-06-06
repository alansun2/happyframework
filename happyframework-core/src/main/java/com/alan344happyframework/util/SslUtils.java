package com.alan344happyframework.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

/**
 * 20150503
 *
 * @author AlanSun
 * 验证微信证书
 */
@Slf4j
public class SslUtils {
    /**
     * 获取SSLConnectionSocketFactory
     * 采用设置信任自签名证书实现https
     *
     * @param keyStorepass
     * @return
     * @throws Exception
     */
    public static SSLConnectionSocketFactory getSSL(String keyStorePath, String keyStorepass) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream instream = new FileInputStream(new File(keyStorePath))) {
            keyStore.load(instream, keyStorepass.toCharArray());
        }

        // Trust own CA and all self-signed certs
        SSLContext sslContext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, keyStorepass.toCharArray())
                .build();
        // Allow TLSv1 protocol only
        return new SSLConnectionSocketFactory(
                sslContext,
                new String[]{"TLSv1"},
                null,
                new DefaultHostnameVerifier());
    }
}
