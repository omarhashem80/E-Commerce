package com.ecommerce.inventory.services;

import com.ecommerce.inventory.dtos.StockLevelDTO;

import java.util.List;
import java.util.Map;

public interface StockLevelService {
    List<StockLevelDTO> getAllStockLevels();

    StockLevelDTO getStockLevel(Long stockLevelId);

    StockLevelDTO createStockLevel(StockLevelDTO stockLevelDTO);

    StockLevelDTO editQuantity(Long stockLevelId, Integer quantity);

    void deleteStockLevel(Long stockLevelId);

    Map<String, StockLevelDTO> transferQuantity(String sourceAddress, Long productId, Integer quantity, String destinationAddress);

    Long getSupplierIdByStockLevel(Long stockLevelId);
}
