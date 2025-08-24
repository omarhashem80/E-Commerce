package com.ecommerce.inventory.controllers;

import com.ecommerce.inventory.dtos.QuantityRequest;
import com.ecommerce.inventory.dtos.StockLevelDTO;
import com.ecommerce.inventory.services.StockLevelService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stock-levels")
@AllArgsConstructor
public class StockLevelController {
    private final StockLevelService stockLevelService;

    @GetMapping
    public List<StockLevelDTO> getAllStockLevels() {
        return stockLevelService.getAllStockLevels();
    }

    @GetMapping("{stockLevelId}")
    public StockLevelDTO getStockLevel(@PathVariable Long stockLevelId) {
        return stockLevelService.getStockLevel(stockLevelId);
    }

    @PostMapping
    public StockLevelDTO createStockLevel(@RequestBody StockLevelDTO stockLevelDTO) {
        return stockLevelService.createStockLevel(stockLevelDTO);
    }

    @PatchMapping("/{stockLevelId}/quantity")
    public StockLevelDTO editQuantity(@PathVariable Long stockLevelId, @RequestBody QuantityRequest quantityRequest) {
        return stockLevelService.editQuantity(stockLevelId, quantityRequest.getQuantity());
    }

    @PatchMapping("/transfer/{productId}")
    public Map<String, StockLevelDTO> transfer(@RequestParam  String source,@PathVariable Long productId , @RequestBody Map<String, Integer> body, @RequestParam String destination) {
        System.out.println("transfer from " + source + " to " + destination);
        return stockLevelService.transferQuantity(source, productId, body.get("quantity"), destination);
    }

    @DeleteMapping("/{stockLevelId}")
    public void deleteStockLevel(@PathVariable Long stockLevelId) {
        stockLevelService.deleteStockLevel(stockLevelId);
    }

}
