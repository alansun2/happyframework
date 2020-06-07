package com.alan344happyframework.util;

import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author alan
 * @date 18-8-15 下午4:54 *
 */
public class RetryUtils {

    /**
     * 获取string 重试器
     *
     * @param attempt 尝试次数
     * @return {@link Retryer}
     */
    public static Retryer<String> getStrRetry(int attempt) {
        return RetryerBuilder.<String>newBuilder()
                .retryIfException()
                .retryIfResult(StringUtils::isEmpty)
                .withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(attempt))
                .build();
    }

    /**
     * 获取boolean 重试器
     *
     * @param attempt 尝试次数
     * @return {@link Retryer}
     */
    public static Retryer<Boolean> getBooleanRetry(int attempt) {
        return RetryerBuilder.<Boolean>newBuilder()
//                .retryIfException()
                .retryIfResult(aBoolean -> Objects.equals(aBoolean, false))
//                .withStopStrategy(new StopWhenBizExStopStratrgy())
                .withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(attempt))
                .build();
    }
}
