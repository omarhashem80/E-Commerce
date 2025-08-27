package com.ecommerce.shop.services;

import com.ecommerce.shop.dtos.QuantityRequest;
import com.ecommerce.shop.entities.CartItem;
import com.ecommerce.shop.entities.Product;
import com.ecommerce.shop.exceptions.NotFoundException;
import com.ecommerce.shop.exceptions.ServiceNotFoundException;
import com.ecommerce.shop.payloads.ApiResponse;
import com.ecommerce.shop.proxies.ProductProxy;
import com.ecommerce.shop.repositories.CartItemRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {
    private final CartItemRepository cartItemRepository;
    private final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
    private final ProductProxy productProxy;

    @Override
    public List<CartItem> getCartItems(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    private CartItem addCartItem(Long userId, CartItem cartItem, Product product) {
        cartItem.setUserId(userId);
        cartItem.setPrice(product.getPrice());
        return cartItemRepository.save(cartItem);
    }

    @Override
    @CircuitBreaker(name = "cartItemService", fallbackMethod = "addCartItemFallback")
    public CartItem addCartItem(Long userId, CartItem cartItem) {
        logger.info("Attempting to reserve product {} with quantity {} for user {}",
                cartItem.getProductId(), cartItem.getQuantity(), userId);

        ResponseEntity<ApiResponse<Product>> reserved = productProxy.reserve(
                cartItem.getProductId(),
                new QuantityRequest(cartItem.getQuantity())
        );

        Product reservedProduct = reserved.getBody().getData();
        logger.info("Product {} reserved successfully for user {}", reservedProduct.getProductId(), userId);

        CartItem addedItem = addCartItem(userId, cartItem, reservedProduct);
        logger.info("Cart item {} added successfully for user {}", addedItem.getCartItemId(), userId);

        return addedItem;
    }

    public CartItem addCartItemFallback(Long userId, CartItem cartItem, Throwable ex) {
        logger.error("Failed to add cart item for user {}. Error: {}", userId, ex.getMessage());
        throw new ServiceNotFoundException("Inventory service unavailable. Please try again later.");
    }

    @Override
    public CartItem updateCartItem(Long userId, Long cartItemId,CartItem updatedCartItem) {
        CartItem item = cartItemRepository.findByCartItemIdAndUserId(cartItemId, userId)
                .orElseThrow(() -> new NotFoundException("Cart item with id: " + cartItemId + " not found"));
        item.setQuantity(updatedCartItem.getQuantity());

        CartItem updatedItem =  cartItemRepository.save(item);

        return updatedItem;
    }

    @CircuitBreaker(name = "cartItemService", fallbackMethod = "updateCartItemFallback")
    private void updateCartItem(Long userId, Long productId, Integer quantity) {
        productProxy.reserve(productId,
                new QuantityRequest(quantity));
    }

    public void updateCartItemFallback(Long userId, Long cartItemId, CartItem updatedCartItem, Throwable ex) {
        logger.error("CartItem update failed for user {} cartItem {}. Error: {}",
                userId, cartItemId, ex.getMessage());

        throw new ServiceNotFoundException("Inventory service unavailable. Please try again later.");
    }

    @Override
    public void deleteCartItem(Long userId, Long cartItemId) {
        cartItemRepository.deleteByCartItemIdAndUserId(cartItemId, userId);
    }

    @Override
    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }

    @Override
    public Long getCartUserId(Long userId) {
        return cartItemRepository.findByUserId(userId).stream()
                .findFirst()
                .map(CartItem::getUserId)
                .orElse(null);
    }
}
