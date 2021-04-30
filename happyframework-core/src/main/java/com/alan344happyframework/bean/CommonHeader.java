package com.alan344happyframework.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author AlanSun
 * @date 2020/11/20 14:01
 * <p>
 * 通用请求头
 */
@Getter
@Setter
public class CommonHeader {
    /**
     * app版本号
     */
    private String version;
    /**
     * 来源 0：android；1：ios；3：小程序
     */
    private Integer clientType;
    /**
     * 业务类型
     */
    private Integer serviceTyp;
    /**
     * 园区码
     */
    private Integer parkCode;
}
