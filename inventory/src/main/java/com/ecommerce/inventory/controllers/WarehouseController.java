package com.ecommerce.inventory.controllers;

import com.ecommerce.inventory.dtos.WarehouseDTO;
import com.ecommerce.inventory.entities.Warehouse;
import com.ecommerce.inventory.services.WarehouseService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouses")
@AllArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @GetMapping
    public List<Warehouse> getWarehouses() {
        return warehouseService.getWarehouses();
    }

    @GetMapping("/{warehouseId}")
    public Warehouse getWarehouse(@PathVariable Long warehouseId) {
        return warehouseService.getWarehouse(warehouseId);
    }

    @PostMapping
    public Warehouse addWarehouse(@RequestBody Warehouse warehouse) {
        return warehouseService.addWarehouse(warehouse);
    }

    @PatchMapping("/{warehouseId}")
    public Warehouse updateWarehouse(@PathVariable Long warehouseId, @RequestBody WarehouseDTO warehouseDTO) {
        return warehouseService.updateWarehouse(warehouseId, warehouseDTO);
    }

    @DeleteMapping("/{warehouseId}")
    public void deleteWarehouse(@PathVariable Long warehouseId) {
        warehouseService.deleteWarehouse(warehouseId);
    }
}
