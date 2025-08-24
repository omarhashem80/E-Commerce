package com.ecommerce.inventory.services;


import com.ecommerce.inventory.dtos.WarehouseDTO;
import com.ecommerce.inventory.entities.Warehouse;
import com.ecommerce.inventory.exceptions.AlreadyExistsException;
import com.ecommerce.inventory.exceptions.WarehouseNotFoundException;
import com.ecommerce.inventory.repositories.WarehouseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WarehouseServiceImpl implements WarehouseService{
    private final WarehouseRepository warehouseRepository;

    @Override
    public List<Warehouse> getWarehouses() {
        return warehouseRepository.findAll().stream().filter(warehouse -> warehouse.getActive() == Boolean.TRUE).toList();
    }

    @Override
    public Warehouse getWarehouse(Long warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId).orElse(null);

        if (warehouse == null || warehouse.getActive() == false) {
            throw new WarehouseNotFoundException(warehouseId);
        }

        return warehouse;
    }

    @Override
    public Warehouse getWarehouseByAddress(String address) {
        Warehouse warehouse = warehouseRepository.findByAddress(address);
        if (warehouse == null || warehouse.getActive() == false) {
            throw new WarehouseNotFoundException(address);
        }
        return warehouse;
    }

    @Override
    public Warehouse addWarehouse(Warehouse warehouse) {
        Warehouse checked = warehouseRepository.findByAddress(warehouse.getAddress());
        if (checked != null && checked.getActive() == true) {
            throw new AlreadyExistsException("Warehouse with address " + warehouse.getAddress() + " already exists");
        }
        return warehouseRepository.save(warehouse);
    }

    @Override
    public Warehouse updateWarehouse(Long warehouseId, WarehouseDTO warehouseDTO) {
        Warehouse warehouseToUpdate = getWarehouse(warehouseId);

        if (warehouseDTO.getAddress() != null) {
            Warehouse checked = warehouseRepository.findByAddress(warehouseDTO.getAddress());
            if (checked != null || checked.getActive() == true) {
                throw new AlreadyExistsException("Warehouse with address " + warehouseDTO.getAddress() + " already exists");
            }
            warehouseToUpdate.setAddress(warehouseDTO.getAddress());
        }

        if (warehouseDTO.getName() != null) {
            warehouseToUpdate.setName(warehouseDTO.getName());
        }
        return warehouseRepository.save(warehouseToUpdate);
    }

    @Override
    public void deleteWarehouse(Long warehouseId) {
        Warehouse warehouseToDelete = getWarehouse(warehouseId);
        warehouseToDelete.setActive(false);
        warehouseRepository.save(warehouseToDelete);
    }
}