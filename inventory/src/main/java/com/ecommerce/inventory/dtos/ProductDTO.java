package com.ecommerce.inventory.dtos;


import com.ecommerce.inventory.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ProductDTO {
    private Long productId;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private LocalDateTime updatedAt;
    private Long supplierId;

    public static ProductDTO fromEntity(Product product) {
        if (product == null) return null;
        return new ProductDTO(
                product.getProductId(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getPrice(),
                product.getUpdatedAt(),
                product.getSupplierId()
        );
    }

    public Product toEntity() {
        Product product = new Product();
        product.setProductId(this.productId);
        product.setName(this.name);
        product.setDescription(this.description);
        product.setCategory(this.category);
        product.setPrice(this.price);
        product.setUpdatedAt(this.updatedAt != null ? this.updatedAt : LocalDateTime.now());
        product.setSupplierId(this.supplierId);
        product.setActive(true);
        return product;
    }
}

