package com.example.user_api.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExecutionTimeAspect {

    private final Logger logger = LoggerFactory.getLogger(ExecutionTimeAspect.class);
    private long startTime;

    @Before("execution(* com.example.user_api.servicies.UserService.*(..))")
    public void logMethodEntry(JoinPoint joinPoint) {
        logger.info("Запустился метод: " + joinPoint.getSignature().toShortString());
        startTime = System.currentTimeMillis();
    }

    @AfterReturning("execution(* com.example.user_api.servicies.UserService.*(..))")
    public void logMethodExit(JoinPoint joinPoint) {
        logger.info("Выполнился метод: " + joinPoint.getSignature().toShortString());
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        logger.info(
                "{} время выполнения {}ms",
                joinPoint.getSignature().toShortString(),
                executionTime
        );
    }

    @AfterThrowing(pointcut = "execution(* com.example.user_api.servicies.UserService.*(..))", throwing = "exception")
    public void afterThrowingMethod(JoinPoint joinPoint, Exception exception) {
        logger.error(
                "{} оценка времени выполнения не производилась, было выброшено исключение: {}",
                joinPoint.getSignature().toShortString(),
                exception.getMessage()
        );
    }
}
