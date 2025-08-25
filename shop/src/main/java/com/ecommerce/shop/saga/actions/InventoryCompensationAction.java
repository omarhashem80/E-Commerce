package com.ecommerce.shop.saga.actions;

import com.ecommerce.shop.proxies.ProductProxy;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InventoryCompensationAction implements CompensationAction {
    private final ProductProxy productProxy;
    private final Long productId;
    private final int quantity;

    @Override
    public void compensate() {
//        productProxy.addStock(productId, new QuantityRequest(quantity));
    }
}
