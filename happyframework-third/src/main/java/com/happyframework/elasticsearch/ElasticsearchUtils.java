package com.happyframework.elasticsearch;

import io.searchbox.client.JestClient;
import io.searchbox.core.Count;
import io.searchbox.core.CountResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author ：AlanSun
 * @date ：2019/3/3 22:12
 * @description ：${description}
 * @since : $version$
 */
@Slf4j
@Component
@ConditionalOnClass(JestClient.class)
public class ElasticsearchUtils {

    @Resource
    private JestClient jestClient;

    public double getCount(String indexName) {
        Count count = new Count.Builder()
                .addIndex(indexName)
                .setParameter("ignore_unavailable", true)
                .build();
        try {
            CountResult countResult = jestClient.execute(count);
            if (countResult.getResponseCode() == HttpStatus.SC_OK) {
                return countResult.getCount();
            }
        } catch (IOException e) {
            log.error("获取数量错误,indexName：{}", indexName);
            log.error("", e);
        }
        return 0;
    }
}
