package com.happyframework.tencent.im;

import com.alan344happyframework.exception.BizException;
import com.alan344happyframework.util.StringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author alan
 * @createtime 18-8-15 下午4:36 *
 */
@ConditionalOnBean(value = IMProperties.class, name = "redisTemplate")
@Component
public class IMUtils {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private TencentIMClient tencentIMClient;

    @Autowired
    private IMProperties imProperties;

    /**
     * 腾讯im的admind的urlsig
     */
    private static final String TENCENT_IM_ADMIN_URLSIG = "tencent_im_admin_urlsig:";

    private static String admUrlsig;

    private Map<String, Object> getImBaseRequest() {
        Map<String, Object> baseRequest = new HashMap<>(5);
        baseRequest.put("sdkappid", imProperties.getAppId());
        baseRequest.put("identifier", imProperties.getAdminIdentifier());
        baseRequest.put("random", RandomUtils.nextInt(10000000, 99999999));
        baseRequest.put("contenttype", "json");
        baseRequest.put("usersig", this.getAdmUrlSig());
        return baseRequest;
    }

    private String getAdmUrlSig() {
        if (StringUtils.isEmpty(admUrlsig)) {
            admUrlsig = redisTemplate.opsForValue().get(TENCENT_IM_ADMIN_URLSIG);
            if (StringUtils.isEmpty(admUrlsig)) {
                admUrlsig = IMSignUtils.createUsersig(imProperties.getAppId(), imProperties.getAdminIdentifier(), imProperties.getAccountType(), imProperties.getPrivateKey());
                if (StringUtils.isEmpty(admUrlsig)) {
                    throw new BizException("创建失败");
                }
                redisTemplate.opsForValue().set(TENCENT_IM_ADMIN_URLSIG, admUrlsig, 150, TimeUnit.DAYS);
            }
        }

        return admUrlsig;
    }

    public Map<String, Object> importAccount(Map<String, String> requestBody) {
        return tencentIMClient.importAccount(this.getImBaseRequest(), requestBody);
    }

    public Map<String, Object> createGroup(Map<String, Object> requestBody) {
        return tencentIMClient.createGroup(this.getImBaseRequest(), requestBody);
    }

    public Map<String, Object> destroyGroup(Map<String, Object> requestBody) {
        return tencentIMClient.destroyGroup(this.getImBaseRequest(), requestBody);
    }

    public Map<String, Object> modifyGroupMemberInfo(Map<String, Object> requestBody) {
        return tencentIMClient.modifyGroupMemberInfo(this.getImBaseRequest(), requestBody);
    }

    public Map<String, Object> addGroupMember(Map<String, Object> requestBody) {
        return tencentIMClient.addGroupMember(this.getImBaseRequest(), requestBody);
    }

    public Map<String, Object> deleteGroupMember(Map<String, Object> requestBody) {
        return tencentIMClient.deleteGroupMember(this.getImBaseRequest(), requestBody);
    }

    public Map<String, Object> sendGroupSystemNotification(Map<String, Object> requestBody) {
        return tencentIMClient.sendGroupSystemNotification(this.getImBaseRequest(), requestBody);
    }

    public Map<String, Object> sendGroupMsg(Map<String, Object> requestBody) {
        return tencentIMClient.sendGroupMsg(this.getImBaseRequest(), requestBody);
    }

    public Map<String, Object> changeGroupOwner(Map<String, String> requestBody) {
        return tencentIMClient.changeGroupOwner(this.getImBaseRequest(), requestBody);
    }

    public Map<String, Object> friendImport(Map<String, Object> requestBody) {
        return tencentIMClient.friendImport(this.getImBaseRequest(), requestBody);
    }

    public Map<String, Object> friendAdd(Map<String, Object> requestBody) {
        return tencentIMClient.friendAdd(this.getImBaseRequest(), requestBody);
    }

    public Map<String, Object> friendUpdate(Map<String, Object> requestBody) {
        return tencentIMClient.friendUpdate(this.getImBaseRequest(), requestBody);
    }

    public Map<String, Object> friendDelete(Map<String, Object> requestBody) {
        return tencentIMClient.friendDelete(this.getImBaseRequest(), requestBody);
    }

    public Map<String, Object> friendGetAll(Map<String, Object> requestBody) {
        return tencentIMClient.friendGetAll(this.getImBaseRequest(), requestBody);
    }

    public Map<String, Object> portraitGet(Map<String, Object> requestBody) {
        return tencentIMClient.portraitGet(this.getImBaseRequest(), requestBody);
    }

    public Map<String, Object> querystate(Map<String, Object> requestBody) {
        return tencentIMClient.querystate(this.getImBaseRequest(), requestBody);
    }

    public Map<String, Object> sendmsg(Map<String, Object> requestBody) {
        return tencentIMClient.sendmsg(this.getImBaseRequest(), requestBody);
    }
}
