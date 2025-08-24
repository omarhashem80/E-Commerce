package com.ecommerce.shop.services;

import com.ecommerce.shop.entities.Order;

public interface OrderOrchestrationService {
    Order createOrderWithPayments(Long userId);
}
