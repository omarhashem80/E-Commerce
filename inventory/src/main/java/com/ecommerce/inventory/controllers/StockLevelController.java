package com.ecommerce.inventory.controllers;

import com.ecommerce.inventory.dtos.QuantityRequest;
import com.ecommerce.inventory.dtos.StockLevelDTO;
import com.ecommerce.inventory.services.StockLevelService;
import com.ecommerce.inventory.payloads.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stock-levels")
@AllArgsConstructor
public class StockLevelController {

    private final StockLevelService stockLevelService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<StockLevelDTO>>> getAllStockLevels() {
        List<StockLevelDTO> stockLevels = stockLevelService.getAllStockLevels();
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "Stock levels fetched successfully", stockLevels)
        );
    }

    @GetMapping("/{stockLevelId}")
    public ResponseEntity<ApiResponse<StockLevelDTO>> getStockLevel(@PathVariable Long stockLevelId) {
        StockLevelDTO stockLevel = stockLevelService.getStockLevel(stockLevelId);
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "Stock level fetched successfully", stockLevel)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StockLevelDTO>> createStockLevel(@RequestBody StockLevelDTO stockLevelDTO) {
        StockLevelDTO created = stockLevelService.createStockLevel(stockLevelDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(HttpStatus.CREATED, "Stock level created successfully", created)
        );
    }

    @PatchMapping("/{stockLevelId}/quantity")
    public ResponseEntity<ApiResponse<StockLevelDTO>> editQuantity(
            @PathVariable Long stockLevelId,
            @RequestBody  QuantityRequest quantityRequest
    ) {
        StockLevelDTO updated = stockLevelService.editQuantity(stockLevelId, quantityRequest.getQuantity());
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "Stock level quantity updated successfully", updated)
        );
    }

    @PatchMapping("/transfer/{productId}")
    public ResponseEntity<ApiResponse<Map<String, StockLevelDTO>>> transfer(
            @RequestParam String source,
            @PathVariable Long productId,
            @RequestBody Map<String, Integer> body,
            @RequestParam String destination
    ) {
        Map<String, StockLevelDTO> result = stockLevelService.transferQuantity(
                source,
                productId,
                body.get("quantity"),
                destination
        );
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "Stock transferred successfully", result)
        );
    }

    @DeleteMapping("/{stockLevelId}")
    public ResponseEntity<ApiResponse<Void>> deleteStockLevel(@PathVariable Long stockLevelId) {
        stockLevelService.deleteStockLevel(stockLevelId);
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "Stock level deleted successfully", null)
        );
    }
}
