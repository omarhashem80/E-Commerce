package com.ecommerce.shop.security;

import com.ecommerce.shop.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("orderSecurity")
@AllArgsConstructor
public class OrderSecurity {

    private final OrderService orderService;

    public boolean isOrderOwner(Authentication authentication, Long orderId) {
        Long authUserId = (Long) authentication.getDetails();
        Long orderUserId = orderService.getOrderById(orderId).getUserId();
        return authUserId.equals(orderUserId);
    }

    public boolean isOwner(Authentication authentication, Long userId) {
        Long authUserId = (Long) authentication.getDetails();
        return authUserId.equals(userId);
    }
}
