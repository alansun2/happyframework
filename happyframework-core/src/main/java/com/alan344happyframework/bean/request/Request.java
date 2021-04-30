package com.alan344happyframework.bean.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author AlanSun
 * @date 2020/8/12 17:37
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    /**
     * app版本号
     */
    private String version;
    /**
     * 来源 0：android；1：ios；3：小程序
     */
    private Byte clientType;
    /**
     * 业务类型
     */
    private Byte serviceTyp;
}
