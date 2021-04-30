package com.alan344happyframework.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author AlanSun
 * @date 2020/6/22 13:46
 **/
@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static DefaultRedisScript<Long> lockRedisScript;
    private static DefaultRedisScript<Long> releaseLockRedisScript;

    /**
     * 获取自增并设置过期时间的redis脚本
     *
     * @return {@link RedisScript}
     */
    private static RedisScript<Long> getLongRedisScriptWithExpire() {
        if (lockRedisScript == null) {
            // 使用lua脚本 自增并设置过期时间7天
            String script = "local result = redis.call('setnx', KEYS[1], ARGV[1]);" +
                    "if (result ~= 1) then" +
                    "   return 0;" +
                    "end;" +
                    "" +
                    "if (tonumber(ARGV[2]) > 0) then" +
                    "   redis.call('expire', KEYS[1], ARGV[2]);" +
                    "end;" +
                    "return result;";

            // 这里使用Long类型，查看源码可知脚本返回值类型只支持Long, Boolean, List, or deserialized value type.
            lockRedisScript = new DefaultRedisScript<>();
            lockRedisScript.setScriptText(script);
            lockRedisScript.setResultType(Long.class);
        }
        return lockRedisScript;
    }

    /**
     * 获取自增并设置过期时间的redis脚本
     *
     * @return {@link RedisScript}
     */
    private static RedisScript<Long> getReleaseLockScript() {
        if (releaseLockRedisScript == null) {
            // 使用lua脚本 自增并设置过期时间7天
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";

            // 这里使用Long类型，查看源码可知脚本返回值类型只支持Long, Boolean, List, or deserialized value type.
            releaseLockRedisScript = new DefaultRedisScript<>();
            releaseLockRedisScript.setScriptText(script);
            releaseLockRedisScript.setResultType(Long.class);
        }
        return releaseLockRedisScript;
    }

    /**
     * 锁住方法，防止用户再seconds内点击多次
     *
     * @param key     key
     * @param signature  自定义标识, 与releaseLock 输入的 signature 必须一致。参考:https://redis.io/topics/distlock
     * @param seconds key的过期时间，<= 0表示不过期
     * @return true：锁已被别的线程获取；false：获取锁成功
     */
    public boolean lockMethod(String key, String signature, int seconds) {
        Long incr = redisTemplate.execute(getLongRedisScriptWithExpire(), Collections.singletonList(key), signature, seconds);
        return incr != null && 1 != incr;
    }

    /**
     * 释放锁
     *
     * @param key key
     * @param signature 自定义标识, 与lockMethod 输入的 signature 必须一致。参考:https://redis.io/topics/distlock
     */
    public void releaseLock(String key, String signature) {
        Assert.notNull(key, "key 不能为 null");
        Assert.notNull(signature, "signature 不能为 null");

        redisTemplate.execute(getReleaseLockScript(), Collections.singletonList(key), signature);
    }

    /**
     * 获取指定范围的过期时间
     *
     * @param min 最小值
     * @param max 最大值
     * @return 时间
     */
    public static long getExpireTime(long min, long max) {
        return min + (int) (Math.random() * (max - min + 1));
    }

    private static DefaultRedisScript<Long> incrementWithExpireTimeScript;

    /**
     * 获取自增并设置过期时间的redis脚本
     *
     * @return {@link RedisScript}
     */
    private static RedisScript<Long> getIncrementScript() {
        if (incrementWithExpireTimeScript == null) {
            // 使用lua脚本 自增并设置过期时间
            String script = "local result = redis.call('incr', KEYS[1]);" +
                    "if (result == 1) then" +
                    "   redis.call('expire', KEYS[1], ARGV[1]);" +
                    "end;" +
                    "return result;";

            // 这里使用Long类型，查看源码可知脚本返回值类型只支持Long, Boolean, List, or deserialized value type.
            incrementWithExpireTimeScript = new DefaultRedisScript<>();
            incrementWithExpireTimeScript.setScriptText(script);
            incrementWithExpireTimeScript.setResultType(Long.class);
        }
        return incrementWithExpireTimeScript;
    }

    /**
     * 带过期时间自增
     *
     * @param key        key
     * @param expireTime 过期时间
     * @param timeUnit   时间单位
     * @return 自增返回值
     */
    public long incrWithExpireTime(String key, Integer expireTime, TimeUnit timeUnit) {
        final long expireTimeWithSec = TimeUnit.SECONDS.convert(expireTime, timeUnit);
        return redisTemplate.execute(getIncrementScript(), Collections.singletonList(key), expireTimeWithSec);
    }
}
