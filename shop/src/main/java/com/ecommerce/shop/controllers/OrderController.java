package com.ecommerce.shop.controllers;

import com.ecommerce.shop.dtos.QuantityRequest;
import com.ecommerce.shop.dtos.StatusUpdateRequest;
import com.ecommerce.shop.dtos.TransactionDTO;
import com.ecommerce.shop.dtos.TransactionType;
import com.ecommerce.shop.entities.Order;
import com.ecommerce.shop.entities.OrderItem;
import com.ecommerce.shop.proxies.ProductProxy;
import com.ecommerce.shop.proxies.WalletProxy;
import com.ecommerce.shop.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final ProductProxy productProxy;
    private final WalletProxy walletProxy;

    @GetMapping("/{userId}")
    public List<Order> getOrdersByUser(@PathVariable Long userId) {
        return orderService.getOrdersByUser(userId);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{orderId}/details")
    public Order getOrder(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @PostMapping("/{userId}")
    public Order createOrder(@PathVariable Long userId) {
        Order order = orderService.createOrder(userId);
        List<OrderItem> orderItems = order.getOrderItems();
        BigDecimal amount = orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        walletProxy.withdrawal(userId, new TransactionDTO(amount, TransactionType.WITHDRAWAL));

        for (OrderItem item : orderItems) {
            productProxy.sell(item.getProductId(), new QuantityRequest(item.getQuantity()));
        }
        return null;
    }

    @PatchMapping("/{orderId}/status")
    public Order updateStatus(@PathVariable Long orderId, @RequestBody StatusUpdateRequest request) {
        return orderService.updateOrderStatus(orderId, request.getStatus());
    }

    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
    }

    @GetMapping("/{orderId}/items")
    public List<OrderItem> getOrderItems(@PathVariable Long orderId) {
        return orderService.getOrderItems(orderId);
    }

    @PostMapping("/{orderId}/items")
    public OrderItem addOrderItem(@PathVariable Long orderId, @RequestBody OrderItem orderItem) {
        return orderService.addOrderItem(orderId, orderItem);
    }

    @PatchMapping("/{orderId}/items/{itemId}/quantity")
    public OrderItem updateQuantity(@PathVariable Long orderId, @PathVariable Long itemId, @RequestBody OrderItem orderItem) {
        return orderService.updateQuantity(orderId, itemId, orderItem.getQuantity());
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    public void deleteOrderItem(@PathVariable Long orderId, @PathVariable Long itemId) {
        orderService.deleteOrderItem(orderId, itemId);
    }
}
