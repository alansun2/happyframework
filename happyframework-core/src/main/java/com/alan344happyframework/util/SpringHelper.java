package com.alan344happyframework.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author yixin
 * @version 1.0.0
 * @company 嘉苏智能
 * @since 2021/4/13 11:55 下午
 */
@Component
public class SpringHelper implements ApplicationContextAware {

    private static ApplicationContext context = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringHelper.context = applicationContext;
    }

    /**
     * 返回spring 上下文
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return context;
    }

    /**
     * 获取对象
     *
     * @param beanName
     * @param <T>
     * @return
     */
    public static <T> T getBean(String beanName, Class<T> tClass) {
        if (context == null) {
            return null;
        }

        Object bean = context.getBean(beanName);
        return tClass.cast(bean);
    }

    /**
     * 获取对象
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> tClass) {
        if (context == null) {
            return null;
        }

        return context.getBean(tClass);
    }

}
