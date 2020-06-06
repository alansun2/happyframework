package com.alan344happyframework.util;


import java.math.BigDecimal;

/**
 * 算法工具类
 *
 * @author chenlong 2015-12-17
 */
public class MathUtils {

    private static final BigDecimal BIGDECIMAL000 = new BigDecimal(String.valueOf("0.000000001"));

    /**
     * 找回丢失的精度
     * 对res加上十亿分之一
     *
     * @param res 需要找回的数据
     */
    public static BigDecimal getAccuracy(BigDecimal res) {
        if (res == null) {
            res = BigDecimal.ZERO;
        }
        return res.add(BIGDECIMAL000);
    }

    /**
     * 如果Integer为null则初始化为0，否则返回原来的数
     *
     * @param integer int
     */
    public static Integer initIntegerWhenNull(Integer integer) {
        if (integer == null) {
            integer = 0;
        }
        return integer;
    }

    /**
     * 如果decimal为null则初始化为0，否则返回原来的数
     *
     * @param decimal decimal
     */
    public static BigDecimal initBigDecimalWhenNull(BigDecimal decimal) {
        if (decimal == null) {
            return BigDecimal.ZERO;
        }
        return decimal;
    }
}
