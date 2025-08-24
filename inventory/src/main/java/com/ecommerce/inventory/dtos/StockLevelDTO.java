package com.ecommerce.inventory.dtos;

import com.ecommerce.inventory.entities.Product;
import com.ecommerce.inventory.entities.StockLevel;
import com.ecommerce.inventory.entities.Warehouse;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockLevelDTO {
    private Long stockId;
    private Integer quantityAvailable;
    private Integer quantitySold;
    private WarehouseDTO warehouse;
    private ProductDTO product;

    public static StockLevelDTO fromEntity(StockLevel stockLevel) {
        if (stockLevel == null) return null;
        return new StockLevelDTO(
                stockLevel.getStockId(),
                stockLevel.getQuantityAvailable(),
                stockLevel.getQuantitySold(),
                WarehouseDTO.fromEntity(stockLevel.getWarehouse()),
                ProductDTO.fromEntity(stockLevel.getProduct())
        );
    }

    public StockLevel toEntity(Product product, Warehouse warehouse) {
        StockLevel stockLevel = new StockLevel();
        stockLevel.setStockId(this.stockId);
        stockLevel.setQuantityAvailable(this.quantityAvailable);
        stockLevel.setQuantitySold(this.quantitySold);
        stockLevel.setProduct(product);
        stockLevel.setWarehouse(warehouse);
        stockLevel.setActive(true);
        return stockLevel;
    }
}

