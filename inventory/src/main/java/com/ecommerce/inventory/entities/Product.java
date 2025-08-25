package com.ecommerce.inventory.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(nullable = false)
    private String name;

    private String description;

    private String category;

    @Column(nullable = false)
    private BigDecimal price;

    @JsonIgnore
    @Column(nullable = false)
    private Boolean active = true;

    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Long supplierId;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockLevel> stockLevels = new ArrayList<>();

    public void addStockLevel(StockLevel stockLevel) {
        stockLevels.add(stockLevel);
        stockLevel.setProduct(this);
    }

    public void removeStockLevel(StockLevel stockLevel) {
        stockLevels.remove(stockLevel);
        stockLevel.setProduct(null);
    }
}
