package com.test.technical.logging.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LogIOAndExecutionTimeAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogIOAndExecutionTimeAspect.class);

    @Around("@annotation(com.test.technical.logging.annotation.LogIOAndExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        logSignatureAndArgs(joinPoint);
        Object result = proceedAndLogExecutionTime(joinPoint);
        logExitSignatureAndResult(joinPoint, result);

        return result;
    }

    private void logSignatureAndArgs(ProceedingJoinPoint joinPoint) {
        String args = Arrays.toString(joinPoint.getArgs());
        LOGGER.info("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),args);
    }

    private Object proceedAndLogExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;

        LOGGER.info("{} executed in {} ms", joinPoint.getSignature(), executionTime);
        return result;
    }

    private void logExitSignatureAndResult(ProceedingJoinPoint joinPoint, Object result) {
        LOGGER.info("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), result);
    }
}
