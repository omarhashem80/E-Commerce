package com.ecommerce.inventory.exceptions;

public class StockLevelNotFoundException extends NotFoundException {
    public StockLevelNotFoundException(Long id) {
        super("Stock level with id " + id + " not found");
    }
    public StockLevelNotFoundException(String message) {
        super(message);
    }
}
