package com.ecommerce.inventory.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "stock_levels",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "warehouse_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private Long stockId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "warehouse_id", nullable = false)
//    @JsonIgnore
//    @ToString.Exclude
    private Warehouse warehouse;

    @Column(nullable = false)
    private Integer quantityAvailable;

    private Integer quantitySold = 0;

    @JsonIgnore
    @Column(nullable = false)
    private Boolean active = true;

    private LocalDateTime updatedAt = LocalDateTime.now();
}
