package com.example.hometask1.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
@ConditionalOnExpression(value = "${all-aspect.enable:true} and ${method-without-args-logger-aspect.enable:true}")
public class MethodWithoutArgsLoggerAspect {

    @After("execution(* com.example.hometask1.service.impl.*.*())")
    public void logMethodWithoutArgs(JoinPoint pointcut) {
        MethodSignature signature = (MethodSignature) pointcut.getSignature();
        String methodName = signature.getMethod().getName();
        String message = String.format("Method %s is called and it has no arguments", methodName);
        log.debug(message);
    }
}
