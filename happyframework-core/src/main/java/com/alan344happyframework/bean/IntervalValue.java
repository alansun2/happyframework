package com.alan344happyframework.bean;

/**
 * @author AlanSun
 * @date 2021/1/14 16:21
 */
public class IntervalValue<T extends Comparable<T>> {
    private T value;

    public IntervalValue(T value) {
        this.value = value;
    }

    /**
     * 是否在区间内
     *
     * @param start 包含
     * @param end   不包含
     * @return true 在区间内
     */
    public boolean isIn(T start, T end) {
        return value.compareTo(start) >= 0 && value.compareTo(end) > 0;
    }
}
