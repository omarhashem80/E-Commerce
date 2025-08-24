package com.ecommerce.shop.proxies;

import com.ecommerce.shop.dtos.QuantityRequest;
import com.ecommerce.shop.entities.Product;
import com.ecommerce.shop.payloads.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "inventory")
public interface ProductProxy {

    @GetMapping("inventory/products")
    ResponseEntity<ApiResponse<List<Product>>> getProducts();

    @PostMapping("inventory/products/{productId}/reserve")
    public ResponseEntity<ApiResponse<Product>> reserve(@PathVariable Long productId, @RequestBody QuantityRequest quantity);

    @PostMapping("inventory/products/{productId}/sell")
    public ResponseEntity<ApiResponse<Product>> sell(@PathVariable Long productId, @RequestBody QuantityRequest quantity);

}
