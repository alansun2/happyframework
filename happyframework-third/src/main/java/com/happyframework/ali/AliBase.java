package com.happyframework.ali;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ：AlanSun
 * @date ：2018/8/13 0:29
 */
@Getter
@Setter
public class AliBase {
    /**
     * 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。
     * 强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
     */
    private String accessKeyId;

    private String accessKeySecret;
}
