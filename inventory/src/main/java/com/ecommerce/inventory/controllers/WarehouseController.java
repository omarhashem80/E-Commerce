package com.ecommerce.inventory.controllers;

import com.ecommerce.inventory.dtos.WarehouseDTO;
import com.ecommerce.inventory.entities.Warehouse;
import com.ecommerce.inventory.payloads.ResponseTemplate;
import com.ecommerce.inventory.services.WarehouseService;
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

@Tag(
        name = "CRUD REST APIs for Warehouses in Inventory",
        description = "CRUD REST APIs in Inventory to CREATE, UPDATE, FETCH AND DELETE warehouse details"
)
@RestController
@RequestMapping("/warehouses")
@AllArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @Operation(
            summary = "Fetch All Warehouses REST API",
            description = "REST API to fetch all warehouses from inventory"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Warehouses retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @GetMapping
    public ResponseEntity<ResponseTemplate<List<Warehouse>>> getWarehouses() {
        List<Warehouse> warehouses = warehouseService.getWarehouses();
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Warehouses retrieved successfully",
                warehouses
        );
    }

    @Operation(
            summary = "Fetch Warehouse by ID REST API",
            description = "REST API to fetch a warehouse by its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Warehouse retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Warehouse not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @GetMapping("/{warehouseId}")
    public ResponseEntity<ResponseTemplate<Warehouse>> getWarehouse(@PathVariable Long warehouseId) {
        Warehouse warehouse = warehouseService.getWarehouse(warehouseId);
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Warehouse retrieved successfully",
                warehouse
        );
    }

    @Operation(
            summary = "Create Warehouse REST API",
            description = "REST API to create a new warehouse (Admin only)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Warehouse created successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Only admins can create warehouses"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseTemplate<Warehouse>> addWarehouse(@RequestBody Warehouse warehouse) {
        Warehouse savedWarehouse = warehouseService.addWarehouse(warehouse);
        return ResponseBuilder.build(
                HttpStatus.CREATED,
                "Warehouse created successfully",
                savedWarehouse
        );
    }

    @Operation(
            summary = "Update Warehouse REST API",
            description = "REST API to update an existing warehouse by ID (Admin only)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Warehouse updated successfully"),
            @ApiResponse(responseCode = "404", description = "Warehouse not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Only admins can update warehouses"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PatchMapping("/{warehouseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseTemplate<Warehouse>> updateWarehouse(
            @PathVariable Long warehouseId,
            @RequestBody WarehouseDTO warehouseDTO) {

        Warehouse updatedWarehouse = warehouseService.updateWarehouse(warehouseId, warehouseDTO);
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Warehouse updated successfully",
                updatedWarehouse
        );
    }

    @Operation(
            summary = "Delete Warehouse REST API",
            description = "REST API to delete a warehouse by ID (Admin only)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Warehouse deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Warehouse not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Only admins can delete warehouses"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @DeleteMapping("/{warehouseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseTemplate<Void>> deleteWarehouse(@PathVariable Long warehouseId) {
        warehouseService.deleteWarehouse(warehouseId);
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Warehouse deleted successfully",
                null
        );
    }
}
