package com.happyframework.hx;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author alan
 * @createtime 18-6-26 * 环信聊天
 * 401 token 失效，重新获取token
 * 404 用户不存在
 */
@FeignClient(name = "hxChatClient", url = "http://a1.easemob.com")
public interface HxChatClient {
    /**
     * 获取token
     *
     * @param orgName 企业名
     * @param appName app名
     * @param body    client_credentials client_id client_secret
     * @return
     */
    @PostMapping(value = "/{org_name}/{app_name}/token", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Map<String, Object> getToken(@PathVariable("org_name") String orgName,
                                 @PathVariable("app_name") String appName,
                                 Map<String, String> body);

    /**
     * 注册单个用户
     *
     * @param orgName       企业名
     * @param appName       app名
     * @param Authorization token 授权注册模式下需要填写，格式为Bearer ${token}
     * @param chatUser      body
     * @return
     */
    @PostMapping(value = "/{org_name}/{app_name}/users")
    Map<String, Object> registerSingleUser(@PathVariable("org_name") String orgName,
                                           @PathVariable("app_name") String appName,
                                           @RequestHeader("Authorization") String Authorization,
                                           ChatUser chatUser);

    /**
     * 删除用户
     *
     * @param orgName
     * @param appName
     * @param username
     * @param Authorization
     * @return
     */
    @DeleteMapping(value = "/{org_name}/{app_name}/users/{username}")
    Map<String, Object> deleteSingleUser(@PathVariable("org_name") String orgName,
                                         @PathVariable("app_name") String appName,
                                         @PathVariable("username") String username,
                                         @RequestHeader("Authorization") String Authorization);

    /**
     * 更新昵称
     *
     * @param orgName
     * @param appName
     * @param username
     * @param Authorization
     * @param chatUser
     * @return
     */
    @PutMapping(value = "/{org_name}/{app_name}/users/{username}")
    Map<String, Object> updateNickname(@PathVariable("org_name") String orgName,
                                       @PathVariable("app_name") String appName,
                                       @PathVariable("username") String username,
                                       @RequestHeader("Authorization") String Authorization,
                                       ChatUser chatUser);

    @Getter
    @Setter
    @Builder
    class ChatUser {
        private String username;
        private String password;
        private String nickname;
    }

    @Getter
    @Setter
    @Component
    @ConfigurationProperties(prefix = "hx")
    class HxConfig {
        private String orgName;
        private String appName;
        private Map<String, String> body;
    }

}
