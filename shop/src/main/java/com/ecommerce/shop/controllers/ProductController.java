package com.ecommerce.shop.controllers;

import com.ecommerce.shop.entities.Product;
import com.ecommerce.shop.payloads.ApiResponse;
import com.ecommerce.shop.proxies.ProductProxy;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class ProductController {
    private final ProductProxy productProxy;

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<Product>>> getProducts() {
        return productProxy.getProducts();
    }
}
