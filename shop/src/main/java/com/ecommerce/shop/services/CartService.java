package com.ecommerce.shop.services;

import com.ecommerce.shop.entities.CartItem;
import com.ecommerce.shop.entities.Product;
import com.ecommerce.shop.exceptions.NotFoundException;
import com.ecommerce.shop.repositories.CartItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class CartService {
    private final CartItemRepository cartItemRepository;

    public List<CartItem> getCartItems(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public CartItem addCartItem(Long userId, CartItem cartItem, Product product) {
        cartItem.setUserId(userId);
        cartItem.setPrice(product.getPrice());
        return cartItemRepository.save(cartItem);
    }

    public CartItem updateCartItem(Long userId, Long cartItemId, Integer quantity) {
        CartItem item = cartItemRepository.findByCartItemIdAndUserId(cartItemId, userId)
                .orElseThrow(() -> new NotFoundException("Cart item with id: " + cartItemId + " not found"));
        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    public void deleteCartItem(Long userId, Long cartItemId) {
        cartItemRepository.deleteByCartItemIdAndUserId(cartItemId, userId);
    }

    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
