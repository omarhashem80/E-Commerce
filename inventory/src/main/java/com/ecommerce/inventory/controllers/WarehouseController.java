package com.ecommerce.inventory.controllers;

import com.ecommerce.inventory.dtos.WarehouseDTO;
import com.ecommerce.inventory.entities.Warehouse;
import com.ecommerce.inventory.payloads.ApiResponse;
import com.ecommerce.inventory.services.WarehouseService;
import com.ecommerce.inventory.utils.ResponseBuilder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouses")
@AllArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Warehouse>>> getWarehouses() {
        List<Warehouse> warehouses = warehouseService.getWarehouses();
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Warehouses retrieved successfully",
                warehouses
        );
    }

    @GetMapping("/{warehouseId}")
    public ResponseEntity<ApiResponse<Warehouse>> getWarehouse(@PathVariable Long warehouseId) {
        Warehouse warehouse = warehouseService.getWarehouse(warehouseId);
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Warehouse retrieved successfully",
                warehouse
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Warehouse>> addWarehouse(@RequestBody Warehouse warehouse) {
        Warehouse savedWarehouse = warehouseService.addWarehouse(warehouse);
        return ResponseBuilder.build(
                HttpStatus.CREATED,
                "Warehouse created successfully",
                savedWarehouse
        );
    }

    @PatchMapping("/{warehouseId}")
    public ResponseEntity<ApiResponse<Warehouse>> updateWarehouse(
            @PathVariable Long warehouseId,
            @RequestBody WarehouseDTO warehouseDTO) {

        Warehouse updatedWarehouse = warehouseService.updateWarehouse(warehouseId, warehouseDTO);
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Warehouse updated successfully",
                updatedWarehouse
        );
    }

    @DeleteMapping("/{warehouseId}")
    public ResponseEntity<ApiResponse<Void>> deleteWarehouse(@PathVariable Long warehouseId) {
        warehouseService.deleteWarehouse(warehouseId);
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Warehouse deleted successfully",
                null
        );
    }
}
