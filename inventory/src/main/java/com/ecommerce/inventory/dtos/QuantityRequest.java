package com.ecommerce.inventory.dtos;

import jakarta.validation.constraints.Min;

public class QuantityRequest {
    @Min(value = 0, message = "Quantity must be greater than or equal to zero")
    private Integer quantity;

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}


