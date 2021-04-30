package com.alan344happyframework.config.feign;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * feign请求统计耗时。 开启debug模式, 将会打印 vip.tuoyang 包下所有通过{@link org.springframework.cloud.openfeign.FeignClient}注解修饰的接口类发起的http调用耗时统计
 *
 * @author
 * @date 2021/3/15 4:42 下午
 */
@Slf4j
@Aspect
@Component
public class FeignTimeConsumingStatisticsAop {

    @Pointcut("@within(org.springframework.cloud.openfeign.FeignClient)")
    public void feignInterfacePointCut() {
    }

    @Around("feignInterfacePointCut() && execution(* vip.tuoyang..*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        Object obj;
        try {
            stopWatch.start();
            obj = pjp.proceed();
        } finally {
            stopWatch.stop();
            double cost = stopWatch.getTotalTimeSeconds();
            String className = pjp.getSignature().getDeclaringType().getName();
            String methodName = pjp.getSignature().getName();
            log.debug("基于feign的http请求耗时统计: 调用关联方及耗时, method={}.{}, cost:{}秒", className, methodName, cost);
        }
        return obj;
    }
}
