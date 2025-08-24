package com.ecommerce.shop.proxies;

import com.ecommerce.shop.dtos.QuantityRequest;
import com.ecommerce.shop.entities.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "inventory")
public interface ProductProxy {

    @GetMapping("inventory/products")
    List<Product> getProducts();

    @PostMapping("inventory/products/{productId}/reserve")
    Product reserve(@PathVariable Long productId, @RequestBody QuantityRequest quantity);

    @PostMapping("inventory/products/{productId}/sell")
    Product sell(@PathVariable Long productId, @RequestBody QuantityRequest quantity);

}
