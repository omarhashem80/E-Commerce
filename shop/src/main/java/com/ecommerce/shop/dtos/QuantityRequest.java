package com.ecommerce.shop.dtos;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class QuantityRequest {
    private Integer quantity;

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}


