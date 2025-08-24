package com.ecommerce.shop.controllers;

import com.ecommerce.shop.dtos.QuantityRequest;
import com.ecommerce.shop.entities.CartItem;
import com.ecommerce.shop.entities.Product;
import com.ecommerce.shop.proxies.ProductProxy;
import com.ecommerce.shop.services.CartService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
public class CartController {
    private final CartService cartService;
    private final ProductProxy productProxy;

    @GetMapping("/{userId}")
    public List<CartItem> getCartItems(@PathVariable Long userId) {
        return cartService.getCartItems(userId);
    }

    @PostMapping("/{userId}")
    public CartItem addCartItem(@PathVariable Long userId, @RequestBody CartItem cartItem) {
        Product reserved = productProxy.reserve(cartItem.getProductId(), new QuantityRequest(cartItem.getQuantity()));
        return cartService.addCartItem(userId, cartItem, reserved);
    }

    @PatchMapping("/{userId}/cartItems/{cartItemId}")
    public CartItem updateCartItem(
            @PathVariable Long userId,
            @PathVariable Long cartItemId,
            @RequestBody CartItem updatedCartItem) {
        Product reserved = productProxy.reserve(updatedCartItem.getProductId(), new QuantityRequest(updatedCartItem.getQuantity()));
        return cartService.updateCartItem(userId, cartItemId, updatedCartItem.getQuantity());
    }

    @DeleteMapping("/{userId}/cartItems/{cartItemId}")
    public void deleteCartItem(@PathVariable Long userId, @PathVariable Long cartItemId) {
        cartService.deleteCartItem(userId, cartItemId);
    }

    @DeleteMapping("/{userId}")
    public void clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
    }

}
