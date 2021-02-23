package com.example.hometask1.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
@ConditionalOnExpression(value = "${all-aspect.enable:true} and ${time-controller-logger-aspect.enable:true}")
public class TimeControllerLoggerAspect {
    @Around("execution(* com.example.hometask1.controller.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        String simpleClassName = signature.getMethod().getDeclaringClass().getSimpleName();
        String message = String.format("Method %s in class %s is executed in %d ms", methodName, simpleClassName, executionTime);
        log.debug(message);
        return proceed;
    }
}
