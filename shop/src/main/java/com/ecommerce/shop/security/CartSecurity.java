package com.ecommerce.shop.security;

import com.ecommerce.shop.services.CartService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("cartSecurity")
@AllArgsConstructor
public class CartSecurity {

    private final CartService cartService;

    public boolean isOwner(Authentication authentication, Long userId) {
        Long authUserId = (Long) authentication.getDetails();
        return userId.equals(authUserId);
    }
}
