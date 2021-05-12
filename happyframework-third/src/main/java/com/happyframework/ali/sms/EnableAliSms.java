package com.happyframework.ali.sms;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author AlanSun
 * @date 2021/5/11 17:13
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AliSmsConfig.class)
public @interface EnableAliSms {
}
