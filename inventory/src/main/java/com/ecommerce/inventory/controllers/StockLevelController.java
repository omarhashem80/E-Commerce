package com.ecommerce.inventory.controllers;

import com.ecommerce.inventory.dtos.QuantityRequest;
import com.ecommerce.inventory.dtos.StockLevelDTO;
import com.ecommerce.inventory.services.StockLevelService;
import com.ecommerce.inventory.payloads.ApiResponse;
import com.ecommerce.inventory.utils.ResponseBuilder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stock-levels")
@AllArgsConstructor
public class StockLevelController {

    private final StockLevelService stockLevelService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<StockLevelDTO>>> getAllStockLevels() {
        List<StockLevelDTO> stockLevels = stockLevelService.getAllStockLevels();
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Stock levels retrieved successfully",
                stockLevels
        );
    }

    @GetMapping("/{stockLevelId}")
    @PreAuthorize("hasRole('ADMIN') || @stockLevelSecurity.isOwner(authentication, #stockLevelId)")
    public ResponseEntity<ApiResponse<StockLevelDTO>> getStockLevel(@PathVariable Long stockLevelId) {
        StockLevelDTO stockLevel = stockLevelService.getStockLevel(stockLevelId);
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Stock level retrieved successfully",
                stockLevel
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<ApiResponse<StockLevelDTO>> createStockLevel(@RequestBody StockLevelDTO stockLevelDTO) {
        StockLevelDTO created = stockLevelService.createStockLevel(stockLevelDTO);
        return ResponseBuilder.build(
                HttpStatus.CREATED,
                "Stock level created successfully",
                created
        );
    }

    @PatchMapping("/{stockLevelId}/quantity")
    @PreAuthorize("@stockLevelSecurity.isOwner(authentication, #stockLevelId)")
    public ResponseEntity<ApiResponse<StockLevelDTO>> editQuantity(
            @PathVariable Long stockLevelId,
            @RequestBody QuantityRequest quantityRequest
    ) {
        StockLevelDTO updated = stockLevelService.editQuantity(stockLevelId, quantityRequest.getQuantity());
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Stock level quantity updated successfully",
                updated
        );
    }

    @PatchMapping("/transfer/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
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
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Stock transferred successfully",
                result
        );
    }

    @DeleteMapping("/{stockLevelId}")
    @PreAuthorize("@stockLevelSecurity.isOwner(authentication, #stockLevelId)")
    public ResponseEntity<ApiResponse<Void>> deleteStockLevel(@PathVariable Long stockLevelId) {
        stockLevelService.deleteStockLevel(stockLevelId);
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Stock level deleted successfully",
                null
        );
    }
}
