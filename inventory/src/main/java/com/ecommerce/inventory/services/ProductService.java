package com.ecommerce.inventory.services;

import com.ecommerce.inventory.dtos.ProductDTO;
import com.ecommerce.inventory.entities.Product;

import java.util.List;

public interface ProductService {

    List<Product> getProducts();

    Product getProductById(Long productId);

    Product createProduct(ProductDTO productDTO);

    Product updateProduct(Long productId, Product product);

    void deleteProduct(Long productId);

    Product reserveQuantity(Long productId, Integer quantity);

    Product sellQuantity(Long productId, Integer quantity);

    Product stockReturn(Long productId, Integer quantity);
}
