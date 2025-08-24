package com.ecommerce.inventory.repositories;

import com.ecommerce.inventory.entities.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Warehouse findByAddress(String address);
}
