// This file is auto-generated, don't edit it. Thanks.
package com.happyframework.ali.sms;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendBatchSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendBatchSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendBatchSmsResponseBody;
import com.aliyun.teaopenapi.models.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author AlanSun
 * @date 2021/5/11 16:17
 **/
@Slf4j
@Component
public class AliSmsClient {

    private AliSmsProperties aliSmsProperties;

    @Autowired
    public void setAliSmsProperties(AliSmsProperties aliSmsProperties) {
        this.aliSmsProperties = aliSmsProperties;
    }

    private Client client;

    /**
     * 使用AK&SK初始化账号Client
     */
    @PostConstruct
    public void createClient() throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(aliSmsProperties.getAccessKeyId())
                // 您的AccessKey Secret
                .setAccessKeySecret(aliSmsProperties.getAccessKeySecret());
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        client = new Client(config);
    }

    public boolean send(AliSMSParams params) {
        SendBatchSmsRequest sendSmsRequest = new SendBatchSmsRequest()
                .setPhoneNumberJson("[" + params.getPhoneStr() + "]")
                .setSignNameJson("[" + params.getSignName() + "]")
                .setTemplateCode(params.getTemplateCode())
                .setTemplateParamJson(JSONObject.toJSONString(params.getTemplateParam()));

        // 复制代码运行请自行打印 API 的返回值
        try {
            final SendBatchSmsResponse sendBatchSmsResponse = client.sendBatchSms(sendSmsRequest);
            final SendBatchSmsResponseBody body = sendBatchSmsResponse.getBody();
            if (!"OK".equals(body.getCode())) {
                log.error("短信发送失败，错误码: [{}]，错误信息: [{}], 信息: [{}],", body.getCode(), body.getMessage(), params.toString());
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("短信发送失败", e);
            return false;
        }
    }
}