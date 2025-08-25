package com.ecommerce.shop.services;

import com.ecommerce.shop.entities.Product;
import com.ecommerce.shop.exceptions.ServiceNotFoundException;
import com.ecommerce.shop.payloads.ApiResponse;
import com.ecommerce.shop.proxies.ProductProxy;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductProxy productProxy;
    private final CacheManager cacheManager;
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    @CachePut(value = "productsCache", key = "'all'")
    @CircuitBreaker(name = "productService", fallbackMethod = "getProductsFallback")
    public List<Product> getProducts() {
        logger.info("Fetching fresh products from external service");
        ResponseEntity<ApiResponse<List<Product>>> response = productProxy.getProducts();
        List<Product> products = response.getBody().getData();
        logger.info("Successfully fetched {} products", products.size());
        return products;
    }

    public List<Product> getProductsFallback(Throwable ex) {
        logger.warn("Circuit breaker activated, attempting to serve cached products. Error: {}", ex.getMessage());

        Cache cache = cacheManager.getCache("productsCache");
        if (cache != null) {
            Cache.ValueWrapper wrapper = cache.get("all");
            if (wrapper != null) {
                @SuppressWarnings("unchecked")
                List<Product> cachedProducts = (List<Product>) wrapper.get();
                if (cachedProducts != null) {
                    logger.info("Serving {} cached products", cachedProducts.size());
                    return cachedProducts;
                }
            }
        }

        logger.error("Service unavailable, products not found");

        throw new ServiceNotFoundException("Service unavailable, products not found");
    }
}