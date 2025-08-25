package com.ecommerce.shop.saga.actions;

import com.ecommerce.shop.dtos.QuantityRequest;
import com.ecommerce.shop.proxies.ProductProxy;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InventoryCompensationAction implements CompensationAction {
    private final ProductProxy productProxy;
    private final Long productId;
    private final int quantity;

    @Override
    public void compensate() {
        productProxy.stockReturn(productId, new QuantityRequest(quantity));
    }
}
