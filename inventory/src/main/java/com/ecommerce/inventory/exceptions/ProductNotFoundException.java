package com.ecommerce.inventory.exceptions;

public class ProductNotFoundException extends NotFoundException {
    public ProductNotFoundException(Long id) {
        super("Product with id " + id + " not found");
    }
}
