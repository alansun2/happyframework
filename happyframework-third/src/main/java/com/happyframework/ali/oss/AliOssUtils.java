package com.happyframework.ali.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ：AlanSun
 * @date ：2018/8/12 15:15
 * @description ：阿里oss
 */

@Slf4j
@Component
public class AliOssUtils {

    private AliOssProperties aliOssProperties;

    @Autowired
    public void setAliOssProperties(AliOssProperties aliOssProperties) {
        this.aliOssProperties = aliOssProperties;
    }

    /**
     * oss客户端
     */
    private static OSS ossClient;

    /**
     * 获取 ossClient
     *
     * @return ossClient
     */
    private OSS getOssClient() {
        if (ossClient == null) {
            // 创建OSSClient实例。
//            CredentialsProvider credsProvider = new DefaultCredentialProvider(accessKeyId, accessKeySecret);
//            ClientConfiguration config = new ClientBuilderConfiguration();
//            ossClient = new OSSClient(endpoint, credsProvider, config);
            OSSClientBuilder ossClientBuilder = new OSSClientBuilder();
            ossClient = ossClientBuilder.build(aliOssProperties.getEndPoint(), aliOssProperties.getAccessKeyId(), aliOssProperties.getAccessKeySecret());
            return ossClient;
        }

        return ossClient;
    }

    /**
     * 简单上传 流
     */
    public void putObject(AliOssParamsWithInputStream aliOssParams) throws Exception {
        OSS ossClient = getOssClient();
        try {
            ossClient.putObject(aliOssParams.getBucketName(), aliOssParams.getObjectName(), aliOssParams.getInputStream());
        } catch (Exception e) {
            log.error("oss error", e);
            throw new Exception("oss error");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 简单上传 文件
     */
    public void putFile(AliOssParamsWithFile aliOssParams) throws Exception {
        OSS ossClient = getOssClient();
        try {
            ossClient.putObject(aliOssParams.getBucketName(), aliOssParams.getObjectName(), aliOssParams.getFile());
        } catch (Exception e) {
            log.error("oss error", e);
            throw new Exception("oss error");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 检查文件是否存在
     *
     * @param aliOssParams {@link BaseAliOssParams}
     * @return boolean
     * @throws Exception e
     */
    public boolean checkIsExist(BaseAliOssParams aliOssParams) throws Exception {
        OSS ossClient = getOssClient();
        boolean isExist;
        try {
            isExist = ossClient.doesObjectExist(aliOssParams.getBucketName(), aliOssParams.getObjectName());
        } catch (Exception e) {
            log.error("oss error", e);
            throw new Exception("oss error");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return isExist;
    }

    /**
     * 删除object
     *
     * @param aliOssParams {@link BaseAliOssParams}
     * @throws Exception e
     */
    public void deleteObject(BaseAliOssParams aliOssParams) throws Exception {
        OSS ossClient = getOssClient();
        try {
            ossClient.deleteObject(aliOssParams.getBucketName(), aliOssParams.getObjectName());
        } catch (Exception e) {
            log.error("oss error", e);
            throw new Exception("oss error");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        AliOssUtils aliOssUtils = new AliOssUtils();
        AliOssProperties aliOssProperties = new AliOssProperties();
        aliOssProperties.setBucketName("***");
        aliOssProperties.setAccessKeyId("****");
        aliOssProperties.setAccessKeySecret("*****");
        aliOssUtils.setAliOssProperties(aliOssProperties);
        AliOssParamsWithFile aliOssParams = new AliOssParamsWithFile();
        aliOssParams.setObjectName("test/image/tt");
        aliOssParams.setBucketName("alan344happyframework-static");
        aliOssUtils.putFile(aliOssParams);
    }
}
