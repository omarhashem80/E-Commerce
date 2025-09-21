package com.ecommerce.shop.controllers;

import com.ecommerce.shop.entities.CartItem;
import com.ecommerce.shop.payloads.ResponseTemplate;
import com.ecommerce.shop.services.CartService;
import com.ecommerce.shop.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Cart Management APIs",
        description = "Endpoints for managing user shopping carts and cart items"
)
@RestController
@RequestMapping("/carts")
@AllArgsConstructor
public class CartController {
    private final CartService cartService;

    @Operation(summary = "Get cart items", description = "Fetches all items in the cart of a specific user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart items fetched successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - cannot access another user's cart")
    })
    @GetMapping("/{userId}")
    @PreAuthorize("@cartSecurity.isOwner(authentication, #userId)")
    public ResponseEntity<ResponseTemplate<List<CartItem>>> getCartItems(@PathVariable Long userId) {
        List<CartItem> items = cartService.getCartItems(userId);
        return ResponseBuilder.build(HttpStatus.OK, "Cart items fetched successfully", items);
    }

    @Operation(summary = "Add cart item", description = "Adds a new item to the cart of a specific user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cart item added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid cart item request")
    })
    @PostMapping("/{userId}")
    @PreAuthorize("@cartSecurity.isOwner(authentication, #userId)")
    public ResponseEntity<ResponseTemplate<CartItem>> addCartItem(
            @PathVariable Long userId,
            @RequestBody CartItem cartItem
    ) {
        CartItem addedItem = cartService.addCartItem(userId, cartItem);
        return ResponseBuilder.build(HttpStatus.CREATED, "Cart item added successfully", addedItem);
    }

    @Operation(summary = "Update cart item", description = "Updates the details of a specific item in the user's cart")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart item updated successfully"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    @PatchMapping("/{userId}/cartItems/{cartItemId}")
    @PreAuthorize("@cartSecurity.isOwner(authentication, #userId)")
    public ResponseEntity<ResponseTemplate<CartItem>> updateCartItem(
            @PathVariable Long userId,
            @PathVariable Long cartItemId,
            @RequestBody CartItem updatedCartItem
    ) {
        CartItem updatedItem = cartService.updateCartItem(userId, cartItemId, updatedCartItem);
        return ResponseBuilder.build(HttpStatus.OK, "Cart item updated successfully", updatedItem);
    }

    @Operation(summary = "Delete cart item", description = "Deletes a specific item from the user's cart")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cart item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    @DeleteMapping("/{userId}/cartItems/{cartItemId}")
    @PreAuthorize("@cartSecurity.isOwner(authentication, #userId)")
    public ResponseEntity<ResponseTemplate<Void>> deleteCartItem(
            @PathVariable Long userId,
            @PathVariable Long cartItemId
    ) {
        cartService.deleteCartItem(userId, cartItemId);
        return ResponseBuilder.build(HttpStatus.NO_CONTENT, "Cart item deleted successfully", null);
    }

    @Operation(summary = "Clear cart", description = "Removes all items from the user's cart")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cart cleared successfully")
    })
    @DeleteMapping("/{userId}")
    @PreAuthorize("@cartSecurity.isOwner(authentication, #userId)")
    public ResponseEntity<ResponseTemplate<Void>> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseBuilder.build(HttpStatus.NO_CONTENT, "Cart cleared successfully", null);
    }
}
