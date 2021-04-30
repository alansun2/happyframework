package com.alan344happyframework.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author AlanSun
 * @date 2020/12/28 13:29
 */
@Getter
@Setter
@ToString
public class CommonPushParam implements Serializable {
    private static final long serialVersionUID = -5469258096527401671L;
    /**
     * service 类型
     */
    private String serviceType;
    /**
     * data
     */
    private Object data;
}
