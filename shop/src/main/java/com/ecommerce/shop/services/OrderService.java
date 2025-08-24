package com.ecommerce.shop.services;

import com.ecommerce.shop.entities.Order;
import com.ecommerce.shop.entities.OrderItem;
import com.ecommerce.shop.entities.Status;

import java.util.List;

public interface OrderService {

    List<Order> getAllOrders();

    Order getOrderById(Long orderId);

    Order createOrder(Long userId);

    List<Order> getOrdersByUser(Long userId);

    Order updateOrderStatus(Long orderId, Status status);

    void deleteOrder(Long orderId);

    List<OrderItem> getOrderItems(Long orderId);

    OrderItem addOrderItem(Long orderId, OrderItem orderItem);

    OrderItem getOrderItemById(Long orderId, Long itemId);

    OrderItem updateQuantity(Long orderId, Long itemId, Integer quantity);

    void deleteOrderItem(Long orderId, Long itemId);
}
