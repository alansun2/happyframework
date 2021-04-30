package com.alan344happyframework.cache;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 配合 {@link CacheAop} 实现注解声明缓存数据
 *
 * @author pengxg
 * @date 2021/2/19 8:46 上午
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.METHOD)
public @interface Cacheable {
    /**
     * redis key, 当声明spEl为true时，将视为spEl表达式。表达式接受一个默认参数args，为当前方法的入参数组。
     * example:
     * <pre>
     *      &#064;Cacheable(key = "'USER:' + #args[0].getUsername()", spEl = true)
     *      public User getUser(UserReq req) {
     *          // your logic
     *      }
     * </pre>
     * 其中#args参数为getUser入参数组
     *
     * @return
     */
    String key();

    /**
     * 默认: 24 * 60 * 60, 结合 timeUnit, 即默认为一个天。
     *
     * @return
     */
    long expireTime() default 24 * 60 * 60;

    /**
     * unit of expireTime
     *
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 默认false, 为true时，对key进行spEl解析， 此时key表达式支持变量args, 为当前环绕方法的入参数组。
     *
     * @return
     */
    boolean spEl() default false;
}




