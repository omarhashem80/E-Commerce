package com.ecommerce.shop.entities;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Product {
    private Long productId;

    private String name;

    private String description;

    private String category;

    private BigDecimal price;

    private LocalDateTime updatedAt;

    private Long supplierId;

}
