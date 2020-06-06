package com.alan344happyframework.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    /**
     * 用户id
     */
    protected Integer userId;
    /**
     * app版本号
     */
    private String version;
    /**
     * 来源 1：android；2：ios；3：小程序
     */
    private Byte os;
    /**
     * 1:用户端；2：商家端
     */
    private Byte appType;
}
