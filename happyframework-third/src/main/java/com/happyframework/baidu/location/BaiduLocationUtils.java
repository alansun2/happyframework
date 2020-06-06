package com.happyframework.baidu.location;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author AlanSun
 * @date 2019/1/23 13:08
 **/
@Slf4j
@ConditionalOnBean(value = BaiduMapProperties.class)
@Component
public class BaiduLocationUtils {

    @Resource
    private BaiduLocationApiFeighClient baiduLocationApiFeighClient;

    @Resource
    private BaiduMapProperties baiduMapProperties;

    /**
     * 获取城市
     *
     * @param lat 纬度
     * @param lng 经度
     * @return 城市
     */
    String getCity(String lat, String lng) {
        JSONObject baiduAddressComponent = getBaiduAddressComponent(lat, lng);
        if (baiduAddressComponent == null) {
            return null;
        }
        return baiduAddressComponent.getString("city");
    }

    /**
     * 获取县/区/县级市
     *
     * @param lat 纬度
     * @param lng 经度
     * @return o
     */
    String getDistrict(String lat, String lng) {
        JSONObject baiduAddressComponent = getBaiduAddressComponent(lat, lng);
        if (baiduAddressComponent == null) {
            return null;
        }
        return baiduAddressComponent.getString("district");
    }

    private JSONObject getBaiduAddressComponent(String lat, String lng) {
        String jsonStr = "";
        try {
            jsonStr = baiduLocationApiFeighClient.getGeocoder(lat + "," + lng, baiduMapProperties.getAk());
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            JSONObject result = jsonObject.getJSONObject("result");
            return result.getJSONObject("addressComponent");
        } catch (Exception e) {
            log.error("百度地图已挂, 返回信息{}", jsonStr);
            return null;
        }
    }
}
