package com.example.hometask1.aspects;

import com.example.hometask1.aspects.annotation.CrudOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
@ConditionalOnExpression(value = "${all-aspect.enable:true} and ${crud-method-logger-aspect.enable:true}")
public class CrudMethodsLoggerAspect {
    @Around("@annotation(com.example.hometask1.aspects.annotation.CrudOperation)")
    public Object logCrudOperations(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed = joinPoint.proceed();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CrudOperation annotation = signature.getMethod().getAnnotation(CrudOperation.class);

        String annotationMessage = annotation.operation().getMessage();
        String simpleClassName = signature.getMethod().getDeclaringClass().getSimpleName();
        String methodName = signature.getMethod().getName();
        String message = String.format("User %s performs %s. Method %s was called in class %s",
                userName, annotationMessage, methodName, simpleClassName);
        log.debug(message);
        return proceed;
    }
}
