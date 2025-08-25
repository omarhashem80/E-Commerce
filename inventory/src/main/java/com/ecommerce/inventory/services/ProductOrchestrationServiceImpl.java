package com.ecommerce.inventory.services;

import com.ecommerce.inventory.entities.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class ProductOrchestrationServiceImpl implements ProductOrchestrationService {
    private final ProductService productService;

    @Override
    public Product markSold(Long productId, Integer quantity) {
        Product product = productService.sellQuantity(productId, quantity);
        return product;
    }


}
