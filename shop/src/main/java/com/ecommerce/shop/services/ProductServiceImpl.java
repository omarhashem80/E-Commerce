package com.ecommerce.shop.services;

import com.ecommerce.shop.entities.Product;
import com.ecommerce.shop.payloads.ApiResponse;
import com.ecommerce.shop.proxies.ProductProxy;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductProxy productProxy;

    @Override
    @CachePut(value = "productsCache", key = "'all'")
    @CircuitBreaker(name = "productService", fallbackMethod = "getProductsFallback")
    public List<Product> getProducts() {
        ResponseEntity<ApiResponse<List<Product>>> response = productProxy.getProducts();
        return response.getBody().getData();
    }
    @Cacheable(value = "productsCache", key = "'all'")
    public List<Product> getProductsFallback(Throwable ex) {
        return null;
    }
}
