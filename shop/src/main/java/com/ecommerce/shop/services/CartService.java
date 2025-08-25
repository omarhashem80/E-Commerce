package com.ecommerce.shop.services;

import com.ecommerce.shop.entities.CartItem;
import com.ecommerce.shop.entities.Product;

import java.util.List;

public interface CartService {

    List<CartItem> getCartItems(Long userId);

    CartItem addCartItem(Long userId, CartItem cartItem);

    CartItem updateCartItem(Long userId, Long cartItemId, CartItem updatedCartItem);

    void deleteCartItem(Long userId, Long cartItemId);

    void clearCart(Long userId);
}
