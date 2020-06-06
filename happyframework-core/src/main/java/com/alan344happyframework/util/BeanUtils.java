package com.alan344happyframework.util;

import com.alan344happyframework.util.annotation.NoNeedConvert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * bean工具类.
 *
 * @author demon
 * @createtime 2016/5/11 9:40
 */
public class BeanUtils {

    /**
     * 将java bean转到map，map的key为给定的attrs
     *
     * @param obj   bean实例
     * @param clazz bean的class
     * @param attrs 需要转换的属性
     * @return 实体的属性map，key为属性名，value为属性值
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> beanToMap(Object obj, Class clazz, String... attrs) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Map<String, Object> resultMap = new HashMap<>(attrs.length);
        for (String attr : attrs) {
            Method method = clazz.getMethod(getMethodGetName(attr));
            resultMap.put(attr, method.invoke(obj));
        }
        return resultMap;
    }

    /**
     * bean转为map
     *
     * @param obj   类
     * @param clazz clazz
     * @return map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> beanToMap(Object obj, Class clazz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Object> resultMap = new HashMap<>(fields.length);
        for (Field field : fields) {
            if ("getSerialVersionUID".equalsIgnoreCase(getMethodGetName(field.getName())) || field.isAnnotationPresent(NoNeedConvert.class)) {
                continue;
            }
            Method method = clazz.getMethod(getMethodGetName(field.getName()));
            resultMap.put(field.getName(), method.invoke(obj));
        }
        return resultMap;
    }

    /**
     * 组装get方法名
     *
     * @param attrName 属性名
     */
    public static String getMethodGetName(String attrName) {
        return "get" + attrName.substring(0, 1).toUpperCase() +
                attrName.substring(1);
    }

    /**
     * 组装set方法名
     *
     * @param attrName 属性名
     */
    public static String getMethodSetName(String attrName) {
        return "set" + attrName.substring(0, 1).toUpperCase() +
                attrName.substring(1);
    }

    /**
     * 复制属性值
     *
     * @param source source
     * @param target target
     */
    public static void copyProperties(Object source, Object target) {
        Field[] fields = source.getClass().getDeclaredFields();
        for (Field field : fields) {
            if ("getSerialVersionUID".equalsIgnoreCase(getMethodGetName(field.getName()))) {
                continue;
            }
            try {
                Method getMethod = source.getClass().getMethod(getMethodGetName(field.getName()));
                Method setMethod = target.getClass().getMethod(getMethodSetName(field.getName()), field.getType());
                setMethod.invoke(target, getMethod.invoke(source));
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    /**
     * 调用指定属性的set方法
     *
     * @param clazz
     * @param field
     * @param obj
     * @param args
     */
    @SuppressWarnings("unchecked")
    public static void invokeSet(Class clazz, Field field, Object obj, Object... args) {
        Method method;
        try {
            method = clazz.getMethod(getMethodSetName(field.getName()), field.getType());
            method.invoke(obj, args);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用get方法
     *
     * @param clazz
     * @param source
     * @param fieldName
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Object invokeGet(Class clazz, Object source, String fieldName) {
        try {
            Method getMethod = clazz.getMethod(getMethodGetName(fieldName));
            return getMethod.invoke(source);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Object数组转bean
     *
     * @param objArr
     * @param t
     * @param <T>
     * @return
     */
    public static <T> T objArrToBean(Object[] objArr, Class<T> t) {
        T result = null;
        try {
            result = t.newInstance();
            Field[] fields = t.getDeclaredFields();
            int i = 0;
            for (Field field : fields) {
                if (!field.isAnnotationPresent(NoNeedConvert.class)) {
                    invokeSet(t, field, result, objArr[i]);
                    i++;
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * list<Object[]>转成list<T>
     *
     * @param objList
     * @param t
     * @param <T>
     * @return
     */
    public static <T> List<T> listObjToListBean(List<Object[]> objList, Class<T> t) {
        List<T> resultList = new ArrayList<>();
        for (Object[] objArr : objList) {
            T bean = objArrToBean(objArr, t);
            resultList.add(bean);
        }
        return resultList;
    }

    /**
     * 判断map中的value是否都为空
     *
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> boolean AllNullValues(Map<K, V> map) {
        return Collections.frequency(map.values(), null) == map.size();
    }

    /**
     * 判断两个相同类型的对象的值是否不存在不同的值
     *
     * @param older 类1
     * @param newer 类2
     * @param <T>   类型
     * @return true ： 两个对象完全相同， false : 存在不同的值
     */
    public static <T> boolean checkPropertyOfBean(T older, T newer) {
        Class<?> clazz = older.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                Method getMethod = pd.getReadMethod();
                Object o1 = getMethod.invoke(older);
                Object o2 = getMethod.invoke(newer);
                //避免空指针异常
                Object s1 = o1 == null ? "" : o1;
                //避免空指针异常
                Object s2 = o2 == null ? "" : o2;
                if (!s1.equals(s2)) {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
