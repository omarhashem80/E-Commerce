package com.ecommerce.shop.controllers;

import com.ecommerce.shop.dtos.QuantityRequest;
import com.ecommerce.shop.entities.CartItem;
import com.ecommerce.shop.entities.Product;
import com.ecommerce.shop.payloads.ApiResponse;
import com.ecommerce.shop.proxies.ProductProxy;
import com.ecommerce.shop.services.CartService;
import com.ecommerce.shop.utils.ResponseBuilder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;
    private final ProductProxy productProxy;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<CartItem>>> getCartItems(@PathVariable Long userId) {
        List<CartItem> items = cartService.getCartItems(userId);
        return ResponseBuilder.build(HttpStatus.OK, "Cart items fetched successfully", items);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<CartItem>> addCartItem(@PathVariable Long userId, @RequestBody CartItem cartItem) {
        // check if the user exists first but with auth no need to check it
        ResponseEntity<ApiResponse<Product>> reserved = productProxy.reserve(
                cartItem.getProductId(), new QuantityRequest(cartItem.getQuantity())
        );
        CartItem addedItem = cartService.addCartItem(userId, cartItem, reserved.getBody().getData());
        return ResponseBuilder.build(HttpStatus.CREATED, "Cart item added successfully", addedItem);
    }

    @PatchMapping("/{userId}/cartItems/{cartItemId}")
    public ResponseEntity<ApiResponse<CartItem>> updateCartItem(
            @PathVariable Long userId,
            @PathVariable Long cartItemId,
            @RequestBody CartItem updatedCartItem) {

        productProxy.reserve(updatedCartItem.getProductId(), new QuantityRequest(updatedCartItem.getQuantity()));
        CartItem updatedItem = cartService.updateCartItem(userId, cartItemId, updatedCartItem.getQuantity());
        return ResponseBuilder.build(HttpStatus.OK, "Cart item updated successfully", updatedItem);
    }

    @DeleteMapping("/{userId}/cartItems/{cartItemId}")
    public ResponseEntity<ApiResponse<Void>> deleteCartItem(@PathVariable Long userId, @PathVariable Long cartItemId) {
        cartService.deleteCartItem(userId, cartItemId);
        return ResponseBuilder.build(HttpStatus.NO_CONTENT, "Cart item deleted successfully", null);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseBuilder.build(HttpStatus.NO_CONTENT, "Cart cleared successfully", null);
    }
}
