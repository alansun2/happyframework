package com.happyframework.baidu.location;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author alan
 * @createtime 18-9-26 下午1:00 * 百度地图api
 */
@FeignClient(name = "BaiduLocationApiClient", url = "http://api.map.baidu.com", fallback = BaiduLocationApiFeighClient.BaiduLocationApiFallback.class, decode404 = true)
public interface BaiduLocationApiFeighClient {
    //http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&location=30.691223,121.039717&output=json&ak=6WIDsiR60GjWD0u9qmz9OeGWxkBQjXdf

    /**
     * 百度逆地理编码
     *
     * @param location 纬经度 格式 纬度，经度
     * @param ak       百度ak
     * @return json
     */
    @GetMapping(value = "/geocoder/v2/?output=json")
    String getGeocoder(@RequestParam("location") String location, @RequestParam("ak") String ak);

    @Component
    class BaiduLocationApiFallback implements BaiduLocationApiFeighClient {

        @Override
        public String getGeocoder(String location, String ak) {
            return null;
        }
    }
}
