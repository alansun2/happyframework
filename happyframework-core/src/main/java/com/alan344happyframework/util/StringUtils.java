package com.alan344happyframework.util;

import com.alan344happyframework.constants.SeparatorConstants;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * @author AlanSun
 */
public class StringUtils {

    /**
     * 字符串是否为空
     *
     * @param string
     * @return
     */
    public static boolean isEmpty(String... string) {
        for (String str : string) {
            if (str == null || "".equals(str.trim()) || "null".equals(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str.trim()) || "null".equals(str);
    }

    /**
     * 字符串是否不为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !(str == null || "".equals(str.trim()) || "null".equals(str));
    }

    /**
     * 取的UUID生成的随机数
     *
     * @return
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, uuid.indexOf("-"));
    }

    /**
     * 获取文件类型
     *
     * @param fileName 文件名
     * @return 返回文件类型
     */
    public static String getFileType(String fileName) {
        return fileName.substring(fileName.lastIndexOf(SeparatorConstants.DOT));
    }

    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }

    public static boolean hasLength(String str) {
        return hasLength((CharSequence) str);
    }

    public static String trimAllWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }
        int len = str.length();
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (!Character.isWhitespace(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 比较两个字符串的长度。1 ： str1 > str2 ; 0: str1 = str2; -1 : str1 < str2
     *
     * @param str1 1
     * @param str2 2
     * @return 1 ： str1 > str2 ; 0: str1 = str2; -1 : str1 < str2
     */
    public static int compareStringLength(String str1, String str2) {
        int c = str1.compareTo(str2);
        return Integer.compare(c, 0);
    }

    /**
     * 去除字符串中的全部空格
     *
     * @param str 可能有空格的字符串
     * @return 去除空格的字符串
     */
    public static String replaceBlank(String str) {
        return str.replaceAll(SeparatorConstants.SPACE, SeparatorConstants.EMPTY);
    }

    /**
     * 匹配字符串
     *
     * @param str
     * @param regex
     * @return
     */
    public static boolean matchPattern(String str, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(str);
        return matcher.find();
    }

    /**
     * @param str
     * @param regex 正则表达式
     * @return
     */
    public static List<String> getRegexMatchStr(String regex, String str) {
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(str);
        List<String> regexStr = new ArrayList<>();
        while (matcher.find()) {
            regexStr.add(matcher.group(0));
        }
        return regexStr;
    }

    /**
     * 判断是否包含数组中的字符串
     *
     * @param source
     * @param urls
     * @return
     */
    public static boolean contains(String source, String[] urls) {
        for (String url : urls) {
            if (source.contains(url)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 如果为空则获取默认值
     *
     * @param str        str
     * @param defaultVal 默认值
     * @return str
     */
    public static String getDefaultIfNull(String str, String defaultVal) {
        return isEmpty(str) ? defaultVal : str;
    }

    /**
     * 按指定的字节数截取字符串（一个中文字符占3个字节，一个英文字符或数字占1个字节）
     *
     * @param sourceString 源字符串
     * @param cutBytes     要截取的字节数
     * @return 返回截取后String
     */
    public static String subStringSpecifyBytes(String sourceString, int cutBytes) {
        if (StringUtils.isEmpty(sourceString)) {
            return "";
        }

        int totalBytes = 0, strTotalBytes = sourceString.getBytes().length;
        if (strTotalBytes <= cutBytes) {
            return sourceString;
        }

        int lastIndex = 0, strLength = sourceString.length();
        int last = strLength - 1;
        for (int i = last; i >= 0; i--) {
            String s = Integer.toBinaryString(sourceString.charAt(i));
            if (s.length() > 8) {
                totalBytes += 3;
            } else {
                totalBytes += 1;
            }

            if (strTotalBytes - totalBytes <= cutBytes) {
                lastIndex = i;
                break;
            }
        }

        return sourceString.substring(0, lastIndex);
    }

    /**
     * 将 String 转为 数字列表
     */
    public static List<Integer> formatString2IntList(String content, String separator) {
        if (StringUtils.isNotEmpty(content)) {
            final String[] split = content.split(separator);
            if (split.length > 0) {
                return Arrays.stream(split).map(Integer::parseInt).collect(Collectors.toList());
            }
        }

        return Collections.emptyList();
    }

    /**
     * 将 String 转为 String列表
     */
    public static List<String> formatString2StringList(String content, String separator) {
        if (StringUtils.isNotEmpty(content)) {
            final String[] split = content.split(separator);
            if (split.length > 0) {
                return Arrays.stream(split).collect(Collectors.toList());
            }
        }

        return Collections.emptyList();
    }
}
