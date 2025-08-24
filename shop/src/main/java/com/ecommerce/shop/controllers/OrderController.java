package com.ecommerce.shop.controllers;

import com.ecommerce.shop.dtos.StatusUpdateRequest;
import com.ecommerce.shop.entities.Order;
import com.ecommerce.shop.entities.OrderItem;
import com.ecommerce.shop.payloads.ApiResponse;
import com.ecommerce.shop.services.OrderOrchestrationService;
import com.ecommerce.shop.services.OrderService;
import com.ecommerce.shop.utils.ResponseBuilder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderOrchestrationService orderOrchestrationService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<Order>>> getOrdersByUser(@PathVariable Long userId) {
        return ResponseBuilder.build(HttpStatus.OK, "Orders fetched successfully", orderService.getOrdersByUser(userId));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Order>>> getAllOrders() {
        return ResponseBuilder.build(HttpStatus.OK, "All orders fetched successfully", orderService.getAllOrders());
    }

    @GetMapping("/{orderId}/details")
    public ResponseEntity<ApiResponse<Order>> getOrder(@PathVariable Long orderId) {
        return ResponseBuilder.build(HttpStatus.OK, "Order details fetched successfully", orderService.getOrderById(orderId));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<Order>> createOrder(@PathVariable Long userId) {
        Order order = orderOrchestrationService.createOrderWithPayments(userId);
        return ResponseBuilder.build(HttpStatus.CREATED, "Order created successfully", order);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<Order>> updateStatus(@PathVariable Long orderId, @RequestBody StatusUpdateRequest request) {
        Order updatedOrder = orderService.updateOrderStatus(orderId, request.getStatus());
        return ResponseBuilder.build(HttpStatus.OK, "Order status updated successfully", updatedOrder);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseBuilder.build(HttpStatus.NO_CONTENT, "Order deleted successfully", null);
    }

    @GetMapping("/{orderId}/items")
    public ResponseEntity<ApiResponse<List<OrderItem>>> getOrderItems(@PathVariable Long orderId) {
        return ResponseBuilder.build(HttpStatus.OK, "Order items fetched successfully", orderService.getOrderItems(orderId));
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<ApiResponse<OrderItem>> addOrderItem(@PathVariable Long orderId, @RequestBody OrderItem orderItem) {
        OrderItem addedItem = orderService.addOrderItem(orderId, orderItem);
        return ResponseBuilder.build(HttpStatus.CREATED, "Order item added successfully", addedItem);
    }

    @PatchMapping("/{orderId}/items/{itemId}/quantity")
    public ResponseEntity<ApiResponse<OrderItem>> updateQuantity(@PathVariable Long orderId, @PathVariable Long itemId, @RequestBody OrderItem orderItem) {
        OrderItem updatedItem = orderService.updateQuantity(orderId, itemId, orderItem.getQuantity());
        return ResponseBuilder.build(HttpStatus.OK, "Order item quantity updated successfully", updatedItem);
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<ApiResponse<Void>> deleteOrderItem(@PathVariable Long orderId, @PathVariable Long itemId) {
        orderService.deleteOrderItem(orderId, itemId);
        return ResponseBuilder.build(HttpStatus.NO_CONTENT, "Order item deleted successfully", null);
    }
}
