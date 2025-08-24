package com.ecommerce.inventory.services;

import com.ecommerce.inventory.dtos.WarehouseDTO;
import com.ecommerce.inventory.entities.Warehouse;

import java.util.List;

public interface WarehouseService {

    List<Warehouse> getWarehouses();

    Warehouse getWarehouse(Long warehouseId);

    Warehouse getWarehouseByAddress(String address);

    Warehouse addWarehouse(Warehouse warehouse);

    Warehouse updateWarehouse(Long warehouseId, WarehouseDTO warehouseDTO);

    void deleteWarehouse(Long warehouseId);
}
