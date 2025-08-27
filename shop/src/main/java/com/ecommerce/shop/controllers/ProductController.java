package com.ecommerce.shop.controllers;

import com.ecommerce.shop.entities.Product;
import com.ecommerce.shop.payloads.ApiResponse;
import com.ecommerce.shop.proxies.ProductProxy;
import com.ecommerce.shop.services.ProductService;
import com.ecommerce.shop.utils.ResponseBuilder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductProxy productProxy;

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<Product>>> getProducts() {
        List<Product> products = productService.getProducts();
        return ResponseBuilder.build(HttpStatus.OK, "fetched successfully", products);
    }

    @GetMapping("/internal")
    public String getProductsInternal() {
        System.out.println("here");
        return productProxy.hello();
    }
}
