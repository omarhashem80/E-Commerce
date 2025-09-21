package com.ecommerce.shop.proxies;

import com.ecommerce.shop.configs.FeignCookieForwardingConfig;
import com.ecommerce.shop.dtos.QuantityRequest;
import com.ecommerce.shop.entities.Product;
import com.ecommerce.shop.payloads.ResponseTemplate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "inventory", configuration = FeignCookieForwardingConfig.class )
public interface ProductProxy {


    @GetMapping("inventory/products")
    ResponseEntity<ResponseTemplate<List<Product>>> getProducts();

    @PostMapping("inventory/products/{productId}/reserve")
    ResponseEntity<ResponseTemplate<Product>> reserve(@PathVariable Long productId, @RequestBody QuantityRequest quantity);

    @PostMapping("inventory/products/{productId}/sell")
    ResponseEntity<ResponseTemplate<Product>> sell(@PathVariable Long productId, @RequestBody QuantityRequest quantity);

    @PostMapping("inventory/products/{productId}/stock")
    void stockReturn(@PathVariable Long productId, QuantityRequest quantityRequest);
}
