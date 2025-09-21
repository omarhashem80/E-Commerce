package com.ecommerce.inventory.controllers;

import com.ecommerce.inventory.dtos.QuantityRequest;
import com.ecommerce.inventory.dtos.StockLevelDTO;
import com.ecommerce.inventory.services.StockLevelService;
import com.ecommerce.inventory.payloads.ResponseTemplate;
import com.ecommerce.inventory.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(
        name = "CRUD REST APIs for Stock Levels in Inventory",
        description = "REST APIs to CREATE, UPDATE, FETCH, TRANSFER and DELETE stock level details"
)
@RestController
@RequestMapping("/stock-levels")
@AllArgsConstructor
public class StockLevelController {

    private final StockLevelService stockLevelService;

    @Operation(
            summary = "Fetch All Stock Levels REST API",
            description = "REST API to fetch all stock levels (Admin only)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock levels retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Only admins can access this"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseTemplate<List<StockLevelDTO>>> getAllStockLevels() {
        List<StockLevelDTO> stockLevels = stockLevelService.getAllStockLevels();
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Stock levels retrieved successfully",
                stockLevels
        );
    }

    @Operation(
            summary = "Fetch Stock Level by ID REST API",
            description = "REST API to fetch a stock level by its ID (Admin or Owner only)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock level retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Stock level not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Only admins or owner can access this"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @GetMapping("/{stockLevelId}")
    @PreAuthorize("hasRole('ADMIN') || @stockLevelSecurity.isOwner(authentication, #stockLevelId)")
    public ResponseEntity<ResponseTemplate<StockLevelDTO>> getStockLevel(@PathVariable Long stockLevelId) {
        StockLevelDTO stockLevel = stockLevelService.getStockLevel(stockLevelId);
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Stock level retrieved successfully",
                stockLevel
        );
    }

    @Operation(
            summary = "Create Stock Level REST API",
            description = "REST API to create a new stock level (Supplier only)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Stock level created successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Only suppliers can create stock levels"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PostMapping
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<ResponseTemplate<StockLevelDTO>> createStockLevel(@RequestBody StockLevelDTO stockLevelDTO) {
        StockLevelDTO created = stockLevelService.createStockLevel(stockLevelDTO);
        return ResponseBuilder.build(
                HttpStatus.CREATED,
                "Stock level created successfully",
                created
        );
    }

    @Operation(
            summary = "Update Stock Quantity REST API",
            description = "REST API to update stock quantity for a stock level (Owner only)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock level quantity updated successfully"),
            @ApiResponse(responseCode = "404", description = "Stock level not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Only owner can update stock level"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PatchMapping("/{stockLevelId}/quantity")
    @PreAuthorize("@stockLevelSecurity.isOwner(authentication, #stockLevelId)")
    public ResponseEntity<ResponseTemplate<StockLevelDTO>> editQuantity(
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

    @Operation(
            summary = "Transfer Stock REST API",
            description = "REST API to transfer stock from one warehouse to another (Admin only)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock transferred successfully"),
            @ApiResponse(responseCode = "404", description = "Stock or warehouses not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Only admins can transfer stock"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PatchMapping("/transfer/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseTemplate<Map<String, StockLevelDTO>>> transfer(
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

    @Operation(
            summary = "Delete Stock Level REST API",
            description = "REST API to delete a stock level by ID (Owner only)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock level deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Stock level not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Only owner can delete stock level"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @DeleteMapping("/{stockLevelId}")
    @PreAuthorize("@stockLevelSecurity.isOwner(authentication, #stockLevelId)")
    public ResponseEntity<ResponseTemplate<Void>> deleteStockLevel(@PathVariable Long stockLevelId) {
        stockLevelService.deleteStockLevel(stockLevelId);
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Stock level deleted successfully",
                null
        );
    }
}
