// This file is auto-generated, don't edit it. Thanks.
package com.happyframework.ali.sms;

import com.alan344happyframework.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.*;
import com.aliyun.teaopenapi.models.Config;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author AlanSun
 * @date 2021/5/11 16:17
 **/
@Slf4j
public class AliSmsClient {

    private AliSmsProperties aliSmsProperties;

    public void setAliSmsProperties(AliSmsProperties aliSmsProperties) {
        this.aliSmsProperties = aliSmsProperties;
    }

    private Client client;

    /**
     * 使用AK&SK初始化账号Client
     */
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
        if (StringUtils.isEmpty(params.getSignName())) {
            params.setSignName(aliSmsProperties.getSignName());
        }
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(params.getPhoneStr())
                .setSignName(params.getSignName())
                .setTemplateCode(params.getTemplateCode())
                .setTemplateParam(params.getTemplateParamJson());

        // 复制代码运行请自行打印 API 的返回值
        try {
            final SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);
            final SendSmsResponseBody body = sendSmsResponse.getBody();
            if (!"OK".equals(body.getCode())) {
                log.error("短信发送失败，code: [{}]，message: [{}], bizId: [{}], requestId: [{}], 信息: [{}],", body.getCode(), body.getMessage(), body.getBizId(), body.getRequestId(), params.toString());
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("短信发送失败", e);
            return false;
        }
    }

    public boolean sendBatch(AliSMSParamsBatch params) {
        final List<String> signNames = params.getSignNames();
        if (signNames == null) {
            params.setSignRepeat(aliSmsProperties.getSignName());
        }
        SendBatchSmsRequest sendSmsRequest = new SendBatchSmsRequest()
                .setPhoneNumberJson(JSONObject.toJSONString(params.getPhones()))
                .setSignNameJson(JSONObject.toJSONString(params.getSignNames()))
                .setTemplateCode(params.getTemplateCode())
                .setTemplateParamJson(params.getTemplateParamMaps() == null ? null : JSONObject.toJSONString(params.getTemplateParamMaps()));

        // 复制代码运行请自行打印 API 的返回值
        try {
            final SendBatchSmsResponse sendBatchSmsResponse = client.sendBatchSms(sendSmsRequest);
            final SendBatchSmsResponseBody body = sendBatchSmsResponse.getBody();
            if (!"OK".equals(body.getCode())) {
                log.error("短信发送失败，code: [{}]，message: [{}], bizId: [{}], requestId: [{}], 信息: [{}],", body.getCode(), body.getMessage(), body.getBizId(), body.getRequestId(), params.toString());
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("短信发送失败", e);
            return false;
        }
    }
}