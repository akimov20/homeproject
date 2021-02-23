package com.example.hometask1.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
@ConditionalOnExpression(value = "${all-aspect.enable:true} and ${exception-logger-aspect.enable:true}")
public class ExceptionLoggerAspect {

    @AfterThrowing(pointcut = "execution(* com.example.hometask1.service.impl.*.*(..))", throwing = "ex")
    public void logException(JoinPoint pointcut, Exception ex) throws Throwable {
        String simpleClassName = pointcut.getTarget().getClass().getSimpleName();
        String methodName = pointcut.getSignature().getName();
        String message = String.format("Exception %s in class %s in method %s", ex, simpleClassName, methodName);
        log.error(message);
    }
}
