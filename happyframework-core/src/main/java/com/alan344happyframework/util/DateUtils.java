package com.alan344happyframework.util;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 日期处理工具类
 *
 * @author chenlong 2015-12-17
 */
public class DateUtils {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_SHORT_FORMAT = "yyyy-MM-dd HH:mm";

    public static final String DATE_SHORT_FORMAT_ = "yyyy.MM.dd HH:mm";

    public static final String TOMORROW_FORMAT = "MM月dd日";

    public static final String HOUR_MINUTES_FORMAT = "HHmm";

    public static final String DATE_SHORT = "yyyy-MM-dd";

    public static final String DATE_LONG_FORMAT = "yyyyMMddHHmmssSSS";

    public static final String SECOND_PATTERN = "MM-dd HH:mm";

    public static final String YYYYMMDDHHMM = "yyyyMMddHHmm";

    public static final String DATE_DAY_FORMAT = "yyyy-MM-dd";

    public static final String DATE_DAY_FORMAT2 = "yyyyMMdd";

    public static final String HOUR_MIN_SEC = "HH:mm:ss";

    public static final String HOUR_MIN = "HH:mm";

    public static final String SECOND = "SECOND";

    /**
     * 获取指定格式的当前时间
     *
     * @param format 指定格式
     */
    public static String getNow(final String format) {
        try {
            return formatDate(new Date(), format);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取当前的小时分钟数
     */
    public static int getTimeByHourMinute() {
        Date today = new Date();
        SimpleDateFormat f = new SimpleDateFormat(HOUR_MINUTES_FORMAT);
        return Integer.parseInt(f.format(today));
    }

    /**
     * 获取当前日期
     */
    public static String getCurrentDate() {
        Date today = new Date();
        SimpleDateFormat f = new SimpleDateFormat(DATE_FORMAT);
        return f.format(today);
    }

    /**
     * 根据时间戳转换时间类型
     */
    public static LocalDateTime getDateByTime(long time) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
    }

    /**
     * 将Date类型日期格式化为字符串
     *
     * @param date   Date类型日期
     * @param format 目标格式
     * @return String
     */
    public static String formatDate(final Date date, final String format) {
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        return fmt.format(date);
    }

    /**
     * 获取今日所剩时间
     */
    public static int getTodayLeft() {
        DateTime dateTime = DateTime.now();
        return Seconds.secondsBetween(dateTime, dateTime.millisOfDay().withMaximumValue()).getSeconds();
    }

    /**
     * 获取jodaTime时间差
     *
     * @return 相应timeUnit的时间差
     */
    public static Object getDiffTime(DateTime date1, DateTime date2, TimeUnit timeUnit) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        if (timeUnit.equals(TimeUnit.SECONDS)) {
            return Seconds.secondsBetween(date1, date2).getSeconds();
        }
        return null;
    }

    /**
     * 获取两个date时间差
     *
     * @return 相应timeUnit的时间差
     */
    public static Object getDiffTime(Date date1, Date date2, TimeUnit timeUnit) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        DateTime dateTime1 = new DateTime(date1);
        DateTime dateTime2 = new DateTime(date2);
        return getDiffTime(dateTime1, dateTime2, timeUnit);
    }

    /**
     * 将字符串格式化为Date类型
     *
     * @param date   日期字符串
     * @param format 日期的格式
     * @return Date
     */
    public static Date parseDate(final String date, final String format)
            throws Exception {
        if (date == null || "".equals(date.trim())) {
            return null;
        }

        SimpleDateFormat fmt = new SimpleDateFormat(format);
        return fmt.parse(date);
    }

    /**
     * 获取今天之前的日期
     * <p>
     * minus = 4， 表示返回4天之前的日期
     *
     * @param minus 多少天之前的日期
     */
    public static Date getDayBeforeNow(int minus) {
        DateTime result = DateTime.now().minusDays(minus);
        return result.toDate();
    }

    /**
     * 判断是否是同一天
     * 比较date2 - diff == date1
     */
    public static boolean isSameDay(Date date1, Date date2, int diff) {
        DateTime dateTime1 = new DateTime(date1);
        DateTime dateTime2 = new DateTime(date2);
        return dateTime1.toLocalDate().equals(dateTime2.minusDays(diff).toLocalDate());
    }

}
