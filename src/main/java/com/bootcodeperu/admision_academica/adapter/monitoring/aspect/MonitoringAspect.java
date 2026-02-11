package com.bootcodeperu.admision_academica.adapter.monitoring.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class MonitoringAspect {

    // Captura todos los métodos en la capa de UseCase
    @Around("execution(* com.bootcodeperu.admision_academica.application.usercase.*.*(..))")
    public Object profileUseCaseMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();

        long start = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;

            // Log de éxito (opcional para debug)
            log.info("UseCase: {}.{} ejecutado en {}ms", className, methodName, executionTime);
            return result;

        } catch (Throwable e) {
            long executionTime = System.currentTimeMillis() - start;
            // LOG DE ERROR PROFESIONAL: Incluye contexto, tiempo y excepción
            log.error("ERROR en UseCase: {}.{} después de {}ms. Mensaje: {}",
                    className, methodName, executionTime, e.getMessage());
            throw e;
        }
    }
}