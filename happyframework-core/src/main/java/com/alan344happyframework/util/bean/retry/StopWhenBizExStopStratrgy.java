package com.alan344happyframework.util.bean.retry;

import com.github.rholder.retry.Attempt;
import com.github.rholder.retry.StopStrategy;

/**
 * @author alan
 * @date 18-10-24 下午6:24 *
 */
public class StopWhenBizExStopStratrgy implements StopStrategy {
    @Override
    public boolean shouldStop(Attempt failedAttempt) {
        return failedAttempt.hasException();
    }
}
