package com.ecommerce.inventory.services;

import com.ecommerce.inventory.dtos.ProductDTO;
import com.ecommerce.inventory.entities.StockLevel;
import com.ecommerce.inventory.entities.Product;
import com.ecommerce.inventory.repositories.StockLevelRepository;
import com.ecommerce.inventory.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final StockLevelRepository stockLevelRepository;

    public List<Product> getProducts() {
        return productRepository.findAll().stream()
                .filter(product -> Boolean.TRUE.equals(product.getActive()))
                .peek(product -> product.setStockLevels(
                        product.getStockLevels().stream()
                                .filter(inventory -> Boolean.TRUE.equals(inventory.getActive()))
                                .toList()
                ))
                .filter(product -> !product.getStockLevels().isEmpty())
                .toList();
    }

    public Product getProductById(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null || product.getActive() == false) {
            throw new RuntimeException("Product not found");
        }
        return product;
    }

    public Product createProduct(ProductDTO productDTO) {
        productDTO.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(productDTO.toEntity());
    }

    public Product updateProduct(Long productId, Product product) {
        Product productToUpdate = getProductById(productId);

        if (product.getName() != null) {
            productToUpdate.setName(product.getName());
        }
        if (product.getDescription() != null) {
            productToUpdate.setDescription(product.getDescription());
        }
        if (product.getCategory() != null) {
            productToUpdate.setCategory(product.getCategory());
        }
        if (product.getPrice() != null) {
            productToUpdate.setPrice(product.getPrice());
        }
        return productRepository.save(productToUpdate);
    }

    public void deleteProduct(Long productId) {
        Product product = getProductById(productId);

        product.setActive(false);
        productRepository.save(product);
    }

    public Product reserveQuantity(Long productId, Integer quantity) {
        System.out.println("************HERE*************");
        Product product = getProductById(productId);
        List<StockLevel> stockLevelsList = product.getStockLevels();

        Integer availableQuantity = stockLevelsList.stream().mapToInt(StockLevel::getQuantityAvailable).sum();

        if (availableQuantity < quantity) {
            throw new RuntimeException("Quantity not enough");
        }
        return product;
    }

    public Product sellQuantity(Long productId, Integer quantity) {
        Product product = getProductById(productId);
        List<StockLevel> stockLevelsList = product.getStockLevels();

        Integer availableQuantity = stockLevelsList.stream().mapToInt(StockLevel::getQuantityAvailable).sum();

        if (availableQuantity < quantity) {
            throw new RuntimeException("Quantity not enough");
        }

        Integer remainingQuantity = quantity;

        for (StockLevel stockLevel : stockLevelsList) {
            if (remainingQuantity == 0) break;

            int available = stockLevel.getQuantityAvailable();
            int toReserve = Math.min(available, remainingQuantity);

            stockLevel.setQuantityAvailable(available - toReserve);
            remainingQuantity -= toReserve;
            stockLevel.setQuantitySold(stockLevel.getQuantitySold() + toReserve);

            stockLevelRepository.save(stockLevel);
        }
        return product;
    }
}
