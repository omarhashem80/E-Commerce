package com.ecommerce.inventory.services;

import com.ecommerce.inventory.dtos.ProductDTO;
import com.ecommerce.inventory.entities.Product;
import com.ecommerce.inventory.entities.StockLevel;
import com.ecommerce.inventory.exceptions.InvalidOperationException;
import com.ecommerce.inventory.exceptions.NotFoundException;
import com.ecommerce.inventory.repositories.ProductRepository;
import com.ecommerce.inventory.repositories.StockLevelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final StockLevelRepository stockLevelRepository;

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll().stream()
                .filter(product -> Boolean.TRUE.equals(product.getActive()))
                .peek(product -> {
                    product.getStockLevels().removeIf(inventory -> !Boolean.TRUE.equals(inventory.getActive()));
                })
                .filter(product -> !product.getStockLevels().isEmpty())
                .toList();
    }

    @Override
    public Product getProductById(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null || product.getActive() == false) {
            throw new NotFoundException("Product with id: " + productId + " not found");
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

        }
        Product saved = productRepository.save(product);
        return saved;
    }

    @Override
    public Product stockReturn(Long productId, Integer quantity) {
        Product product = getProductById(productId);
        List<StockLevel> stockLevels = product.getStockLevels();
        stockLevels.sort((a, b) -> b.getUpdatedAt().compareTo(a.getUpdatedAt()));
        Integer remainingQuantity = quantity;
        for (StockLevel stockLevel : stockLevels) {
            if (remainingQuantity < 1) break;
            Integer soldQuantity = stockLevel.getQuantitySold();
            if (soldQuantity > 0) {
                Integer toReturn = Math.min(soldQuantity, remainingQuantity);

                stockLevel.setQuantitySold(soldQuantity - toReturn);
                stockLevel.setQuantityAvailable(stockLevel.getQuantityAvailable() + toReturn);
                remainingQuantity -= toReturn;
            }
        }
        Product saved = productRepository.save(product);

        return saved;
    }
}
