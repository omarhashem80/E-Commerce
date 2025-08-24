package com.ecommerce.inventory.controllers;

import com.ecommerce.inventory.dtos.ProductDTO;
import com.ecommerce.inventory.dtos.QuantityRequest;
import com.ecommerce.inventory.entities.Product;
import com.ecommerce.inventory.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor

public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<Product> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable Long productId) {
        return productService.getProductById(productId);
    }

    @PostMapping
    public Product createProduct(@RequestBody ProductDTO productDTO) {
        return productService.createProduct(productDTO);
    }

    @PatchMapping("/{productId}")
    public Product updateProduct(@PathVariable Long productId, @RequestBody Product product) {
        return productService.updateProduct(productId, product);
    }

    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
    }

    // reserve
    @PostMapping("/{productId}/reserve")
    public Product reserve(
            @PathVariable Long productId,
            @RequestBody QuantityRequest quantity) {
        return productService.reserveQuantity(productId, quantity.getQuantity());
    }

    // sell
    @PostMapping("/{productId}/sell")
    public Product sell(
            @PathVariable Long productId,
            @RequestBody QuantityRequest quantity) {
        return productService.sellQuantity(productId, quantity.getQuantity());
    }


}
