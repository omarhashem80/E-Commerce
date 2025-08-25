package com.ecommerce.shop.saga;

import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@AllArgsConstructor
public class SagaAspect {
    private final SagaContext sagaContext;

    @Around("@annotation(com.ecommerce.shop.saga.annotations.SagaOrchestrated)")
    public Object aroundSaga(ProceedingJoinPoint pjp) throws Throwable {
        try {
            Object result = pjp.proceed();
            sagaContext.clear();
            return result;
        } catch (Exception ex) {
            sagaContext.rollback();
            sagaContext.clear();
            throw new RuntimeException("Saga rollback executed due to failure", ex);
        }
    }
}
