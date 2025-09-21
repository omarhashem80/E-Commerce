package com.ecommerce.shop.controllers;

import com.ecommerce.shop.dtos.StatusUpdateRequest;
import com.ecommerce.shop.entities.Order;
import com.ecommerce.shop.entities.OrderItem;
import com.ecommerce.shop.payloads.ResponseTemplate;
import com.ecommerce.shop.services.OrderOrchestrationService;
import com.ecommerce.shop.services.OrderService;
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
        name = "Order Management APIs",
        description = "Endpoints for managing customer orders, order items, and their statuses"
)
@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderOrchestrationService orderOrchestrationService;

    @Operation(summary = "Get orders by user", description = "Fetches all orders placed by a specific user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders fetched successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - not allowed to access orders")
    })
    @GetMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @orderSecurity.isOwner(authentication, #userId)")
    public ResponseEntity<ResponseTemplate<List<Order>>> getOrdersByUser(@PathVariable Long userId) {
        List<Order> ordersByUser = orderService.getOrdersByUser(userId);
        return ResponseBuilder.build(HttpStatus.OK, "Orders fetched successfully", ordersByUser);
    }

    @Operation(summary = "Get all orders", description = "Fetches all orders (Admin only)")
    @ApiResponse(responseCode = "200", description = "All orders fetched successfully")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseTemplate<List<Order>>> getAllOrders() {
        List<Order> allOrders = orderService.getAllOrders();
        return ResponseBuilder.build(HttpStatus.OK, "All orders fetched successfully", allOrders);
    }

    @Operation(summary = "Get order details", description = "Fetches detailed information about a specific order")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order details fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{orderId}/details")
    @PreAuthorize("hasRole('ADMIN') or @orderSecurity.isOrderOwner(authentication, #orderId)")
    public ResponseEntity<ResponseTemplate<Order>> getOrder(@PathVariable Long orderId) {
        Order orderById = orderService.getOrderById(orderId);
        return ResponseBuilder.build(HttpStatus.OK, "Order details fetched successfully", orderById);
    }

    @Operation(summary = "Create order", description = "Creates a new order for a user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid order request")
    })
    @PostMapping("/{userId}")
    @PreAuthorize("@orderSecurity.isOwner(authentication, #userId)")
    public ResponseEntity<ResponseTemplate<Order>> createOrder(@PathVariable Long userId) {
        Order order = orderOrchestrationService.createOrderWithPayments(userId);
        return ResponseBuilder.build(HttpStatus.CREATED, "Order created successfully", order);
    }

    @Operation(summary = "Update order status", description = "Updates the status of a specific order")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN') or @orderSecurity.isOrderOwner(authentication, #orderId)")
    public ResponseEntity<ResponseTemplate<Order>> updateStatus(
            @PathVariable Long orderId,
            @RequestBody StatusUpdateRequest request
    ) {
        Order updatedOrder = orderService.updateOrderStatus(orderId, request.getStatus());
        return ResponseBuilder.build(HttpStatus.OK, "Order status updated successfully", updatedOrder);
    }

    @Operation(summary = "Delete order", description = "Deletes an order (only owner can delete)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @DeleteMapping("/{orderId}")
    @PreAuthorize("@orderSecurity.isOrderOwner(authentication, #orderId)")
    public ResponseEntity<ResponseTemplate<Void>> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseBuilder.build(HttpStatus.NO_CONTENT, "Order deleted successfully", null);
    }

    @Operation(summary = "Get order items", description = "Fetches all items of a specific order")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order items fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('ADMIN') or @orderSecurity.isOrderOwner(authentication, #orderId)")
    public ResponseEntity<ResponseTemplate<List<OrderItem>>> getOrderItems(@PathVariable Long orderId) {
        List<OrderItem> orderItems = orderService.getOrderItems(orderId);
        return ResponseBuilder.build(HttpStatus.OK, "Order items fetched successfully", orderItems);
    }

    @Operation(summary = "Add order item", description = "Adds a new item to an existing order")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order item added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid order item request")
    })
    @PostMapping("/{orderId}/items")
    @PreAuthorize("@orderSecurity.isOrderOwner(authentication, #orderId)")
    public ResponseEntity<ResponseTemplate<OrderItem>> addOrderItem(
            @PathVariable Long orderId,
            @RequestBody OrderItem orderItem
    ) {
        OrderItem addedItem = orderService.addOrderItem(orderId, orderItem);
        return ResponseBuilder.build(HttpStatus.CREATED, "Order item added successfully", addedItem);
    }

    @Operation(summary = "Update order item quantity", description = "Updates the quantity of an item in an order")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order item quantity updated successfully"),
            @ApiResponse(responseCode = "404", description = "Order item not found")
    })
    @PatchMapping("/{orderId}/items/{itemId}/quantity")
    @PreAuthorize("@orderSecurity.isOrderOwner(authentication, #orderId)")
    public ResponseEntity<ResponseTemplate<OrderItem>> updateQuantity(
            @PathVariable Long orderId,
            @PathVariable Long itemId,
            @RequestBody OrderItem orderItem
    ) {
        OrderItem updatedItem = orderService.updateQuantity(orderId, itemId, orderItem.getQuantity());
        return ResponseBuilder.build(HttpStatus.OK, "Order item quantity updated successfully", updatedItem);
    }

    @Operation(summary = "Delete order item", description = "Deletes an item from an order")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Order item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order item not found")
    })
    @DeleteMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("@orderSecurity.isOrderOwner(authentication, #orderId)")
    public ResponseEntity<ResponseTemplate<Void>> deleteOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId
    ) {
        orderService.deleteOrderItem(orderId, itemId);
        return ResponseBuilder.build(HttpStatus.NO_CONTENT, "Order item deleted successfully", null);
    }
}
