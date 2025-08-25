package com.ecommerce.inventory.services;

import com.ecommerce.inventory.entities.Product;

public interface ProductOrchestrationService {
    Product markSold(Long productId, Integer quantity);
}
