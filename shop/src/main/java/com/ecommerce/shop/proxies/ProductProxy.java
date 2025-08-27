package com.ecommerce.shop.proxies;

import com.ecommerce.shop.configs.FeignAuthConfig;
import com.ecommerce.shop.dtos.QuantityRequest;
import com.ecommerce.shop.entities.Product;
import com.ecommerce.shop.payloads.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "inventory", configuration = FeignAuthConfig.class)
public interface ProductProxy {

    @GetMapping("inventory/internal")
    String hello();

    @GetMapping("inventory/products")
    ResponseEntity<ApiResponse<List<Product>>> getProducts();

    @PostMapping("inventory/products/{productId}/reserve")
    ResponseEntity<ApiResponse<Product>> reserve(@PathVariable Long productId, @RequestBody QuantityRequest quantity);

    @PostMapping("inventory/products/{productId}/sell")
    ResponseEntity<ApiResponse<Product>> sell(@PathVariable Long productId, @RequestBody QuantityRequest quantity);

    @PostMapping("inventory/products/{productId}/stock")
    void stockReturn(@PathVariable Long productId, QuantityRequest quantityRequest);
}
