package com.happyframework.ali.sms;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author alan
 * @date 18-8-10 下午1:17 * 阿里短信服务工具类
 */
@Slf4j
@Component
public class AliSMSUtils {
    /**
     * 短信API产品名称（短信产品名固定，无需修改）
     */
    private static final String PRODUCT = "Dysmsapi";
    /**
     * 短信API产品域名（接口地址固定，无需修改）
     */
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";

    private AliSMSProperties aliSMSProperties;

    @Autowired
    public void setAliSMSUtils(AliSMSProperties aliSMSProperties) {
        this.aliSMSProperties = aliSMSProperties;
    }

    static {
        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
    }

    private static IAcsClient acsClient;

    /**
     * 初始化ascClient,暂时不支持多region（请勿修改）
     *
     * @return {@link IAcsClient}
     * @throws ClientException e
     */
    private IAcsClient getAcsClient() throws ClientException {
        if (null == acsClient) {
            //初始化ascClient,暂时不支持多region（请勿修改）
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliSMSProperties.getAccessKeyId(), aliSMSProperties.getAccessKeySecret());
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PRODUCT, DOMAIN);
            acsClient = new DefaultAcsClient(profile);
        }
        return acsClient;
    }

    /**
     * 发送
     *
     * @param params params
     * @throws ClientException e
     */
    public boolean send(AliSMSParams params) throws ClientException {
        params.valid();
        String phoneStr = params.getPhoneStr();
        String templateCode = params.getTemplateCode();
        String templateParamJson = params.getTemplateParamJson();
        String signName = params.getSignName();
        //初始化ascClient,暂时不支持多region（请勿修改）
        IAcsClient acsClient = getAcsClient();

        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为00+国际区号+号码，如“0085200000000”
        request.setPhoneNumbers(phoneStr);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
        request.setTemplateCode(templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        request.setTemplateParam(templateParamJson);
        //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
//        request.setOutId("yourOutId");
        //请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if (sendSmsResponse.getCode() == null) {
            //请求不成功
            log.error("短信发送失败，信息{}", params.toString());
            return false;
        }

        if (!"OK".equals(sendSmsResponse.getCode())) {
            log.error("短信发送失败，错误码{}，信息{}", sendSmsResponse.getCode(), params.toString());
            return false;
        }

        return true;
    }
}
