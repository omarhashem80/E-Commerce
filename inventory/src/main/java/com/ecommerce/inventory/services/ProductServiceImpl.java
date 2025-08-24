package com.ecommerce.inventory.services;

import com.ecommerce.inventory.dtos.ProductDTO;
import com.ecommerce.inventory.entities.Product;
import com.ecommerce.inventory.entities.StockLevel;
import com.ecommerce.inventory.exceptions.InvalidOperationException;
import com.ecommerce.inventory.exceptions.ProductNotFoundException;
import com.ecommerce.inventory.repositories.ProductRepository;
import com.ecommerce.inventory.repositories.StockLevelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final StockLevelRepository stockLevelRepository;

    @Override
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

    @Override
    public Product getProductById(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null || product.getActive() == false) {
            throw new ProductNotFoundException(productId);
        }
        return product;
    }

    @Override
    public Product createProduct(ProductDTO productDTO) {
        productDTO.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(productDTO.toEntity());
    }

    @Override
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

    @Override
    public void deleteProduct(Long productId) {
        Product product = getProductById(productId);

        product.setActive(false);
        productRepository.save(product);
    }

    @Override
    public Product reserveQuantity(Long productId, Integer quantity) {
        Product product = getProductById(productId);
        List<StockLevel> stockLevelsList = product.getStockLevels();

        Integer availableQuantity = stockLevelsList.stream().mapToInt(StockLevel::getQuantityAvailable).sum();

        if (availableQuantity < quantity) {
            throw new InvalidOperationException("Not enough stock available to reserve");
        }

        if (quantity < 0) {
            throw new InvalidOperationException("Quantity must be greater than 0");
        }
        return product;
    }

    @Override
    public Product sellQuantity(Long productId, Integer quantity) {
        Product product = getProductById(productId);
        List<StockLevel> stockLevelsList = product.getStockLevels();

        Integer availableQuantity = stockLevelsList.stream().mapToInt(StockLevel::getQuantityAvailable).sum();

        if (availableQuantity < quantity) {
            throw new InvalidOperationException("Not enough stock available to sell");
        }

        if (quantity < 0) {
            throw new InvalidOperationException("Quantity must be greater than 0");
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
