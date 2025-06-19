package com.example.muse.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class Timer {

    @Around("execution(* com.example.muse..*Controller.*(..))")
    public Object executionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long end = System.currentTimeMillis();

        log.info("{} 실행시간 : {} ms",
                joinPoint.getSignature().toShortString(),
                end - start);

        return proceed;
    }
}
