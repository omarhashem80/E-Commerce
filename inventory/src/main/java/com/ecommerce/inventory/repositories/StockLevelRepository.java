package com.ecommerce.inventory.repositories;

import com.ecommerce.inventory.entities.StockLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockLevelRepository  extends JpaRepository<StockLevel, Long> {

    Optional<StockLevel> findByProductProductIdAndWarehouseAddress(Long productId, String address);

    Optional<StockLevel> getByProductProductIdAndWarehouseWarehouseId(Long productId, Long warehouseId);
}
