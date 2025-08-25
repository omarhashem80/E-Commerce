package com.ecommerce.inventory.services;

import com.ecommerce.inventory.dtos.StockLevelDTO;
import com.ecommerce.inventory.entities.Product;
import com.ecommerce.inventory.entities.StockLevel;
import com.ecommerce.inventory.entities.Warehouse;
import com.ecommerce.inventory.exceptions.*;
import com.ecommerce.inventory.repositories.ProductRepository;
import com.ecommerce.inventory.repositories.StockLevelRepository;
import com.ecommerce.inventory.repositories.WarehouseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Transactional
public class StockLevelServiceImpl implements StockLevelService {
    private final StockLevelRepository stockLevelRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;

    @Override
    public List<StockLevelDTO> getAllStockLevels() {
        return stockLevelRepository.findAll().stream()
                .filter(stockLevel -> Boolean.TRUE.equals(stockLevel.getActive()))
                .map(StockLevelDTO::fromEntity)
                .toList();
    }

    @Override
    public StockLevelDTO getStockLevel(Long stockLevelId) {
        return StockLevelDTO.fromEntity(retrieveStockLevel(stockLevelId));
    }

    private StockLevel retrieveStockLevel(Long stockLevelId) {
        StockLevel stockLevel = stockLevelRepository.findById(stockLevelId).orElse(null);
        if (stockLevel == null || stockLevel.getActive() == false) {
            throw new NotFoundException("Stock level with id " + stockLevelId + " not found");
        }
        return stockLevel;
    }

    @Override
    public StockLevelDTO createStockLevel(StockLevelDTO stockLevelDTO) {
        Long productId = stockLevelDTO.getProductDTO().getProductId();
        Product product = productRepository.findById(productId).orElse(null);

        if (product == null || product.getActive() == false) {
            throw new NotFoundException("Product with id: " + productId + " not found");
        }

        Long warehouseId = stockLevelDTO.getWarehouseDTO().getWarehouseId();
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElse(null);

        if (warehouse == null || warehouse.getActive() == false) {
            throw new NotFoundException("Warehouse with id " + warehouseId + " not found");
        }

        StockLevel checked = stockLevelRepository.getByProductProductIdAndWarehouseWarehouseId(productId, warehouse.getWarehouseId()).orElse(null);

        if (checked != null && checked.getActive()) {
            throw new AlreadyExistsException("Stock level already exists for product " + productId +
                    " in warehouse " + warehouse.getWarehouseId());
        }

        StockLevel stockLevel = stockLevelDTO.toEntity(product, warehouse);

        StockLevel saved = stockLevelRepository.save(stockLevel);
        return StockLevelDTO.fromEntity(saved);
    }

    @Override
    public StockLevelDTO editQuantity(Long stockLevelId, Integer quantity) {
        StockLevel stockLevel = retrieveStockLevel(stockLevelId);

        int newQuantity = stockLevel.getQuantityAvailable() + quantity;

        if (newQuantity < 0) {
            throw new InvalidOperationException("Quantity cannot be negative");
        }
        if (newQuantity == 0) {
            stockLevel.setActive(false);
        }
        stockLevel.setQuantityAvailable(newQuantity);

        StockLevel savedStockLevel = stockLevelRepository.save(stockLevel);

        return StockLevelDTO.fromEntity(savedStockLevel);
    }

    @Override
    public void deleteStockLevel(Long stockLevelId) {
        StockLevel stockLevel = retrieveStockLevel(stockLevelId);
        stockLevel.setActive(false);
        stockLevelRepository.save(stockLevel);
    }

    @Override
    public Map<String, StockLevelDTO> transferQuantity(String sourceAddress, Long productId, Integer quantity, String destinationAddress) {
        if (quantity == null || quantity <= 0) {
            throw new InvalidOperationException("Quantity must be greater than 0");
        }

        if (productId == null) {
            throw new InvalidOperationException("Product id is null");
        }

        if (sourceAddress.equals(destinationAddress)) {
            throw new InvalidOperationException("Source and destination addresses cannot be the same");
        }

        StockLevel sourceStockLevel = stockLevelRepository.findByProductProductIdAndWarehouseAddress(productId, sourceAddress).orElse(null);

        if (sourceStockLevel == null || sourceStockLevel.getActive() == false) {
            throw new NotFoundException("Source stock level for product " + productId +
                    " in warehouse " + sourceAddress + " not found");
        }

        if (quantity > sourceStockLevel.getQuantityAvailable()) {
            throw new InvalidOperationException("Not enough stock to transfer");
        }

        StockLevel destinationStockLevel = stockLevelRepository.findByProductProductIdAndWarehouseAddress(productId, destinationAddress).orElse(null);

        if (destinationStockLevel == null || destinationStockLevel.getActive() == false) {
            throw new NotFoundException("Destination stock level for product " + productId +
                    " in warehouse " + destinationAddress + " not found");
        }


        sourceStockLevel.setQuantityAvailable(sourceStockLevel.getQuantityAvailable() - quantity);
        if (sourceStockLevel.getQuantityAvailable() == 0)
            sourceStockLevel.setActive(false);

        stockLevelRepository.save(sourceStockLevel);

        destinationStockLevel.setQuantityAvailable(destinationStockLevel.getQuantityAvailable() + quantity);
        stockLevelRepository.save(destinationStockLevel);


        Map<String, StockLevelDTO> result = new HashMap<>();
        result.put("source", StockLevelDTO.fromEntity(sourceStockLevel));
        result.put("destination", StockLevelDTO.fromEntity(destinationStockLevel));
        return result;
    }

}
