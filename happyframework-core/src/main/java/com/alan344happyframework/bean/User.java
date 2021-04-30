package com.alan344happyframework.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * @author AlanSun
 * @date 2020/11/20 9:20
 */
@Getter
@Setter
public class User<ID> {
    /**
     * 用户 id
     */
    private ID id;
    private String name;
    private String mobile;
    private String account;
    private Integer gender;
    private String avatar;
    private String email;
}
