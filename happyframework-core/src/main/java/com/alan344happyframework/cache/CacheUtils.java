package com.alan344happyframework.cache;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author AlanSun
 * @date 2020/6/24 16:24
 */
@ConditionalOnClass(value = RedisTemplate.class)
@Component
public class CacheUtils {
    @Autowired(required = false)
    protected RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取缓存
     * <p>
     * 当值不存在时，缓存时间为 30 - 120 秒
     *
     * @param func 当没有缓存时执行的方法
     * @param key  cacheKey
     * @return {@link R}
     */
    public <R> R getCache(Supplier<R> func, String key, long startTime, long endTime, TimeUnit timeUnit) {
        Object t = getCache(key);
        if (t == null) {
            t = func.get();
            this.saveCache(t, key, startTime, endTime, timeUnit);
        }

        return t != null && !"-0".equals(t) ? (R) t : null;
    }

    /**
     * 获取缓存
     * <p>
     * 当值不存在时，缓存时间为 30 - 120 秒
     *
     * @param func 当没有缓存时执行的方法
     * @param key  cacheKey
     * @return {@link R}
     */
    public <R> R getCache(Supplier<R> func, boolean refresh, String key, long startTime, long endTime, TimeUnit timeUnit) {
        Object t;
        if (refresh) {
            t = func.get();
            this.saveCache(t, key, startTime, endTime, timeUnit);
        } else {
            t = getCache(key);
            if (t == null) {
                t = func.get();
                this.saveCache(t, key, startTime, endTime, timeUnit);
            }
        }

        return t != null && !(t instanceof String) ? (R) t : null;
    }

    protected void saveCache(Object t, String key, long startTime, long endTime, TimeUnit timeUnit) {
        if (t != null) {
            redisTemplate.opsForValue().set(key, t, RandomUtils.nextLong(startTime, endTime), timeUnit);
        } else {
            redisTemplate.opsForValue().set(key, "-0", RandomUtils.nextInt(30, 120), TimeUnit.SECONDS);
        }
    }

    protected Object getCache(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取缓存
     *
     * @param func 当没有缓存时执行的方法
     * @param key  cacheKey
     * @return true: 存在；false：不存在
     */
    public boolean getCacheFlag(Supplier<Long> func, String key, long startTime, long endTime, TimeUnit timeUnit) {
        Object t = getCache(key);
        if (t == null) {
            t = func.get();
            redisTemplate.opsForValue().set(key, (long) t >= 1 ? 1 : 0, RandomUtils.nextLong(startTime, endTime), timeUnit);
        }

        return (int) t >= 1;
    }

    /**
     * 删除缓存
     *
     * @param key key
     */
    public boolean delete(String key) {
        final Boolean delete = redisTemplate.delete(key);
        return delete != null && delete;
    }
}
