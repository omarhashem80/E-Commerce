package com.ecommerce.inventory.services;


import com.ecommerce.inventory.dtos.WarehouseDTO;
import com.ecommerce.inventory.entities.Warehouse;
import com.ecommerce.inventory.repositories.WarehouseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WarehouseService {
    private WarehouseRepository warehouseRepository;

    public List<Warehouse> getWarehouses() {
        return warehouseRepository.findAll().stream().filter(warehouse -> warehouse.getActive() == Boolean.TRUE).toList();
    }

    public Warehouse getWarehouse(Long warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId).orElse(null);

        if (warehouse == null || warehouse.getActive() == false) {
            throw new RuntimeException("Warehouse with id " + warehouseId + " not found");
        }

        return warehouse;
    }

    public Warehouse getWarehouseByAddress(String address) {
        Warehouse warehouse = warehouseRepository.findByAddress(address);
        if (warehouse == null || warehouse.getActive() == false) {
            throw new RuntimeException("Warehouse with address " + address + " already exists");
        }
        return warehouse;
    }

    public Warehouse addWarehouse(Warehouse warehouse) {
        Warehouse checked = warehouseRepository.findByAddress(warehouse.getAddress());
        if (checked != null && checked.getActive() == true) {
            throw new RuntimeException("Warehouse with address " + warehouse.getAddress() + " already exists");
        }
        return warehouseRepository.save(warehouse);
    }

    public Warehouse updateWarehouse(Long warehouseId, WarehouseDTO warehouseDTO) {
        Warehouse warehouseToUpdate = getWarehouse(warehouseId);

        if (warehouseDTO.getAddress() != null) {
            Warehouse checked = warehouseRepository.findByAddress(warehouseDTO.getAddress());
            if (checked != null || checked.getActive() == true) {
                throw new RuntimeException("Warehouse with address " + warehouseDTO.getAddress() + " already exists");
            }
            warehouseToUpdate.setAddress(warehouseDTO.getAddress());
        }

        if (warehouseDTO.getName() != null) {
            warehouseToUpdate.setName(warehouseDTO.getName());
        }
        return warehouseRepository.save(warehouseToUpdate);
    }

    public void deleteWarehouse(Long warehouseId) {
        Warehouse warehouseToDelete = getWarehouse(warehouseId);
        warehouseToDelete.setActive(false);
        warehouseRepository.save(warehouseToDelete);
    }
}