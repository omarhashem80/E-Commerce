package com.ecommerce.inventory.repositories;

import com.ecommerce.inventory.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByProductId(Long productId);
}
