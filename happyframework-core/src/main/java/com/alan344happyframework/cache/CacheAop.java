package com.alan344happyframework.cache;

import com.alan344happyframework.util.StringUtils;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

/**
 * @author pengxg
 * @date 2021/2/19 8:57 上午
 */
@Slf4j
@Aspect
@Component
@ConditionalOnBean(value = CacheUtils.class)
@AllArgsConstructor
public class CacheAop {
    private CacheUtils cacheUtils;

    @Pointcut("@annotation(Cacheable)")
    public void cachePoint() {
    }

    @Around(value = "cachePoint() && @annotation(cacheable)", argNames = "pjp, cacheable")
    public Object around(ProceedingJoinPoint pjp, Cacheable cacheable) {
        Object[] args = pjp.getArgs();
        Assert.isTrue(StringUtils.isNotEmpty(cacheable.key()), "cacheable 注解的 key 参数值非法");
        final String k = compileKey(cacheable.key(), args, cacheable.spEl());

        Object v = cacheUtils.getCache(k);

        if (v == null) {
            try {
                v = pjp.proceed();
                cacheUtils.saveCache(v, k, cacheable.expireTime(), cacheable.expireTime(), cacheable.timeUnit());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        return v != null && !"-0".equals(v) ? v : null;
    }

    private String compileKey(String key, Object[] args, boolean spEl) {
        if (!spEl) {
            return key;
        }

        Expression expression = new SpelExpressionParser().parseExpression(key);
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("args", args);
        return expression.getValue(context, String.class);
    }
}
