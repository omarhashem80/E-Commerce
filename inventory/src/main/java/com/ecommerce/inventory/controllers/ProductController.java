package com.ecommerce.inventory.controllers;

import com.ecommerce.inventory.dtos.ProductDTO;
import com.ecommerce.inventory.dtos.QuantityRequest;
import com.ecommerce.inventory.entities.Product;
import com.ecommerce.inventory.payloads.ApiResponse;
import com.ecommerce.inventory.services.ProductOrchestrationService;
import com.ecommerce.inventory.services.ProductService;
import com.ecommerce.inventory.utils.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor

public class ProductController {
    private final ProductService productService;
    private final ProductOrchestrationService productOrchestrationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getProducts() {
        List<Product> products = productService.getProducts();
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Products retrieved successfully",
                products
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Product retrieved successfully",
                product
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Product>> createProduct(@RequestBody ProductDTO productDTO) {
        Product createdProduct = productService.createProduct(productDTO);
        return ResponseBuilder.build(
                HttpStatus.CREATED,
                "Product created successfully",
                createdProduct
        );
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable Long productId,
            @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(productId, product);
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Product updated successfully",
                updatedProduct
        );
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Product deleted successfully",
                null
        );
    }

    // reserve
    @PostMapping("/{productId}/reserve")
    public ResponseEntity<ApiResponse<Product>> reserve(
            @PathVariable Long productId,
            @RequestBody @Valid QuantityRequest quantity) {
        Product reservedProduct = productService.reserveQuantity(productId, quantity.getQuantity());
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Quantity reserved successfully",
                reservedProduct
        );
    }

    // sell
    @PostMapping("/{productId}/sell")
    public ResponseEntity<ApiResponse<Product>> sell(
            @PathVariable Long productId,
            @RequestBody @Valid QuantityRequest quantity) {
        Product soldProduct = productOrchestrationService.markSold(productId, quantity.getQuantity());
        return ResponseBuilder.build(
                HttpStatus.OK,
                "Quantity sold successfully",
                soldProduct
        );
    }


}
