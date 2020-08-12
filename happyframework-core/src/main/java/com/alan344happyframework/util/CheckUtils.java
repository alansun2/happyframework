package com.alan344happyframework.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author AlanSun
 * @date 2020/8/12 16:34
 * <p>
 * 校验类
 **/
public class CheckUtils {
    public static final String PHONE_FORMAT = "^1[0-9]{10}$";

    public static final Pattern PHONE_START_PATTERN = Pattern.compile(CheckUtils.PHONE_START);

    public static final String PHONE_START = "(?<=\\d{3})\\d(?=\\d{4})";

    /**
     * 匹配中文
     */
    public static final String ZH_PATTERN = "([\\u4e00-\\u9fa5]+)";

    /**
     * 匹配数字
     */
    public static final String NUMBER_PATTERN = "-?[1-9]\\d*";

    /**
     * 字母
     */
    public static final String LETTER_PATTERN = "^[A-Za-z]+";


    /**
     * 验证手机号格式是否正确
     *
     * @param phone 手机号
     * @return true indicate the phone number is true
     */
    public static boolean checkPhoneNumber(String phone) {
        Pattern pattern = Pattern.compile(PHONE_FORMAT);
        Matcher matcher = pattern.matcher(phone);
        return !matcher.matches();
    }

    /**
     * 手机号替换成星号
     *
     * @param phone 手机号
     * @return the phone number after replace
     */
    public static String replacePhoneStart(String phone) {
        return PHONE_START_PATTERN.matcher(phone).replaceAll("*");
    }
}
