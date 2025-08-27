package com.ecommerce.shop.controllers;

import com.ecommerce.shop.entities.CartItem;
import com.ecommerce.shop.payloads.ApiResponse;
import com.ecommerce.shop.services.CartService;
import com.ecommerce.shop.utils.ResponseBuilder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/{userId}")
    @PreAuthorize("@cartSecurity.isOwner(authentication, #userId)")
    public ResponseEntity<ApiResponse<List<CartItem>>> getCartItems(@PathVariable Long userId) {
        List<CartItem> items = cartService.getCartItems(userId);
        return ResponseBuilder.build(HttpStatus.OK, "Cart items fetched successfully", items);
    }

    @PostMapping("/{userId}")
    @PreAuthorize("@cartSecurity.isOwner(authentication, #userId)")
    public ResponseEntity<ApiResponse<CartItem>> addCartItem(@PathVariable Long userId, @RequestBody CartItem cartItem) {
        // check if the user exists first but with auth no need to check it
        CartItem addedItem = cartService.addCartItem(userId, cartItem);
        return ResponseBuilder.build(HttpStatus.CREATED, "Cart item added successfully", addedItem);
    }

    @PatchMapping("/{userId}/cartItems/{cartItemId}")
    @PreAuthorize("@cartSecurity.isOwner(authentication, #userId)")
    public ResponseEntity<ApiResponse<CartItem>> updateCartItem(
            @PathVariable Long userId,
            @PathVariable Long cartItemId,
            @RequestBody CartItem updatedCartItem) {

        CartItem updatedItem = cartService.updateCartItem(userId, cartItemId, updatedCartItem);
        return ResponseBuilder.build(HttpStatus.OK, "Cart item updated successfully", updatedItem);
    }

    @DeleteMapping("/{userId}/cartItems/{cartItemId}")
    @PreAuthorize("@cartSecurity.isOwner(authentication, #userId)")
    public ResponseEntity<ApiResponse<Void>> deleteCartItem(@PathVariable Long userId, @PathVariable Long cartItemId) {
        cartService.deleteCartItem(userId, cartItemId);
        return ResponseBuilder.build(HttpStatus.NO_CONTENT, "Cart item deleted successfully", null);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("@cartSecurity.isOwner(authentication, #userId)")
    public ResponseEntity<ApiResponse<Void>> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseBuilder.build(HttpStatus.NO_CONTENT, "Cart cleared successfully", null);
    }
}
