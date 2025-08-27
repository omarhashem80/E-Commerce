package com.ecommerce.inventory.security;

import com.ecommerce.inventory.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("productSecurity")
@RequiredArgsConstructor
public class ProductSecurity {

    private final ProductService productService;

    public boolean isOwner(Authentication authentication, Long productId) {

        Long supplierId = productService.getProductSupplierId(productId);
        Long authUserId = (Long) authentication.getDetails();
        return supplierId.equals(authUserId);
    }
}
