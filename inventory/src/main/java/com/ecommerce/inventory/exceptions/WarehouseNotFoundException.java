package com.ecommerce.inventory.exceptions;

public class WarehouseNotFoundException extends NotFoundException {
    public WarehouseNotFoundException(Long id) {
        super("Warehouse with id " + id + " not found");
    }

    public WarehouseNotFoundException(String address) {
        super("Warehouse with address " + address + " not found");
    }
}

