package com.ecommerce.shop.saga;

import com.ecommerce.shop.saga.actions.CompensationAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;

@Component
public class SagaContext {
    private final Logger logger = LoggerFactory.getLogger(SagaContext.class);
    private final ThreadLocal<Deque<CompensationAction>> actions =
            ThreadLocal.withInitial(ArrayDeque::new);

    public void addCompensation(CompensationAction action) {
        actions.get().push(action);
    }

    public void rollback() {
        Deque<CompensationAction> stack = actions.get();
        while (!stack.isEmpty()) {
            CompensationAction action = stack.pop();
            try {
                action.compensate();
            } catch (Exception e) {
                logger.error("Compensation action {} failed: {}",
                        action.getClass().getSimpleName(), e.getMessage());
            }
        }
    }

    public void clear() {
        actions.remove();
    }
}
