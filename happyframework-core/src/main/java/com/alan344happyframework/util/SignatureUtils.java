package com.alan344happyframework.util;

import lombok.extern.slf4j.Slf4j;

import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author alan
 * @date 18-1-9 * 验证
 * <p>
 * md5签名
 */
@Slf4j
public class SignatureUtils {

    /**
     * @param map     待签名数据
     * @param signKey 自定义的签名key
     * @return 签名
     * @throws NoSuchAlgorithmException e
     */
    public static String getSign(Map<String, String> map, String signKey) throws NoSuchAlgorithmException {
        List<String> list = new ArrayList<>();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!Objects.equals(entry.getValue(), "") && !"sign".equals(entry.getKey())) {
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }

        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < size; ++i) {
            sb.append(arrayToSort[i]);
        }

        String result = sb.toString();
        result = result + "key=" + signKey;
        result = MD5Util.MD5(result).toUpperCase();
        return result;
    }
}
