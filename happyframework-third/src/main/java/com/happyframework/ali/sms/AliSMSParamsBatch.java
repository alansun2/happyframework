package com.happyframework.ali.sms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author alan
 * @date 18-8-10 下午2:22 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AliSMSParamsBatch {
    /**
     * 短信接收号码,支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,
     * 批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；
     * 发送国际/港澳台消息时，接收号码格式为00+国际区号+号码，如“0085200000000”
     */
    private List<String> phones;

    /**
     * 短信模板ID，发送国际/港澳台消息时，请使用国际/港澳台短信模版
     */
    private String templateCode;

    /**
     * 短信模板变量替换JSON串,友情提示:如果JSON中需要带换行符,请参照标准的JSON协议。
     */
    private List<Map<String, String>> templateParamMaps;

    /**
     * 短信签名
     */
    private List<String> signNames;

    public void setSignRepeat(String signName) {
        signNames = new ArrayList<>();
        for (int i = 0; i < phones.size(); i++) {
            signNames.add(signName);
        }
    }
}
