package com.alan344happyframework.constants;

import java.math.BigDecimal;

public class BaseConstants {
    public static final String FAIL = "FAIL";

    public static final String SUCCESS = "SUCCESS";

    public static final String SYSTEM = "SYSTEM";

    /**
     * BigDecimal :2
     */
    public static final BigDecimal BIGDECIMAL_2 = new BigDecimal(String.valueOf(2));
    /**
     * BigDecimal :0.1
     */
    public static final BigDecimal BIGDECIMAL_01 = new BigDecimal(String.valueOf(0.1));
    /**
     * 第二件半价相当于打75折
     */
    public static final BigDecimal BIGDECIMAL_075 = new BigDecimal(String.valueOf(0.75));
    /**
     * bigdecimal 5
     */
    public static final BigDecimal BIGDECIMAL_5 = new BigDecimal("5");
    /**
     * BigDecimal :0.9
     */
    public static final BigDecimal BIGDECIMAL_09 = new BigDecimal(String.valueOf("0.9"));
    /**
     * BigDecimal :100
     */
    public static final BigDecimal BIGDECIMAL_100 = new BigDecimal(String.valueOf("100"));
    /**
     * BigDecimal :0.01
     */
    public static final BigDecimal BIGDECIMAL_001 = new BigDecimal(String.valueOf("0.01"));

    /**
     * 保留一位小数
     */
    public static final int SCALE_0 = 0;
    /**
     * 保留一位小数
     */
    public static final int SCALE_1 = 1;
    /**
     * 保留两位小数
     */
    public static final int SCALE_2 = 2;
    /**
     * 保留四位小数
     */
    public static final int SCALE_4 = 4;

    /*安卓 or ios*/
    /**
     * 来源 安卓
     */
    public static final int SOURCE_ANDROID = 1;
    /**
     * 来源 ios
     */
    public static final int SOURCE_IOS = 2;
    /**
     * 来源 小程序
     */
    public static final int SOURCE_XCX = 3;
}