package com.alan344happyframework.util;

import ch.hsr.geohash.GeoHash;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author AlanSun
 */
public class MapUtils {
    private static final int numberOfCharacters = 5;

    public static String getGeoHashStr(double lon, double lat) {
        return GeoHash.geoHashStringWithCharacterPrecision(lat, lon, numberOfCharacters);
    }

    /**
     * 获取周围的地图区块
     *
     * @param geoHashStr geoHashStr
     * @return 周围的地图区块
     */
    public static String[] getAroundGeoHash(String geoHashStr) {
        GeoHash geoHash = GeoHash.fromGeohashString(geoHashStr);
        GeoHash[] gList = geoHash.getAdjacent();
        String[] sList = new String[gList.length + 1];
        for (int i = 0; i < gList.length; i++) {
            sList[i] = gList[i].toBase32();
        }
        sList[8] = geoHashStr;
        return sList;
    }

    /**
     * 获取周围的地图区块
     *
     * @param geoHashStr geoHashStr
     * @return 周围的地图区块
     */
    public static List<String> getAroundGeoHashReturnList(String geoHashStr) {
        GeoHash geoHash = GeoHash.fromGeohashString(geoHashStr);
        GeoHash[] gList = geoHash.getAdjacent();
        List<String> sList = new ArrayList<>();
        for (GeoHash aGList : gList) {
            sList.add(aGList.toBase32());
        }
        sList.add(geoHashStr);
        return sList;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 计算两个经纬度之间的距离
     * 非直线距离
     * <p>
     *
     * @param lat1 第一点纬度
     * @param lng1 第一点经度
     * @param lat2 第二点纬度
     * @param lng2 第二点经度
     * @return 返回距离 单位：米
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        double earthRadius = 6378137;
        s = s * earthRadius;
        s = Math.round(s * 1000);
        return s;
    }

    /**
     * 使用reflect(反射)进行转换
     *
     * @param map
     * @param beanClass
     * @return
     * @throws Exception
     */
    public static Object map2Bean(Map<String, Object> map, Class<?> beanClass) throws Exception {

        return null;
    }
}
