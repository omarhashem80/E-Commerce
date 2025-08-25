package com.ecommerce.shop.services;

import com.ecommerce.shop.entities.Product;
import com.ecommerce.shop.payloads.ApiResponse;

import java.util.List;
public interface ProductService {
    List<Product> getProducts();
}
