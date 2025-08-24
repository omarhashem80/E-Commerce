package com.ecommerce.shop.services;

import com.ecommerce.shop.entities.CartItem;
import com.ecommerce.shop.entities.Order;
import com.ecommerce.shop.entities.OrderItem;
import com.ecommerce.shop.entities.Status;
import com.ecommerce.shop.exceptions.InvalidOperationException;
import com.ecommerce.shop.exceptions.NotFoundException;
import com.ecommerce.shop.repositories.CartItemRepository;
import com.ecommerce.shop.repositories.OrderItemRepository;
import com.ecommerce.shop.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll().stream().filter(Order::isActive).toList();
    }

    public Order getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null || !order.isActive()) {
            throw new NotFoundException("Order with id: " + orderId + " not found");
        }
        return order;
    }

    public Order createOrder(Long userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new InvalidOperationException("Cart of user wit id: " + userId + " is empty");
        }

        BigDecimal totalPrice = cartItems.stream()
                .map(item -> BigDecimal.valueOf(item.getQuantity()).multiply(item.getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(totalPrice);
        order.setStatus(Status.PENDING);

        Order savedOrder = orderRepository.save(order);

        for (CartItem item : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProductId(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            orderItem.setSupplierId(item.getUserId());
            orderItemRepository.save(orderItem);
            order.getOrderItems().add(orderItem);
        }

        cartItemRepository.deleteByUserId(userId);

        return savedOrder;
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order updateOrderStatus(Long orderId, Status status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public void deleteOrder(Long orderId) {
        Order order = getOrderById(orderId);
        order.setActive(false);
        orderRepository.save(order);
    }

    public List<OrderItem> getOrderItems(Long orderId) {
        Order order = getOrderById(orderId);
        return order.getOrderItems();
    }

    public OrderItem addOrderItem(Long orderId, OrderItem orderItem) {
        Order order = getOrderById(orderId);
        orderItem.setOrder(order);
        return orderItemRepository.save(orderItem);

    }

    public OrderItem getOrderItemById(Long orderId, Long itemId) {
        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Order with id: " + orderId + " not found"));
        if (!item.getOrder().getOrderId().equals(orderId) || !item.getOrder().isActive()) {
            throw new NotFoundException("Order item with id: " + orderId + " not found");
        }
        return item;
    }

    public OrderItem updateQuantity(Long orderId, Long itemId, Integer quantity) {
        OrderItem item = getOrderItemById(orderId, itemId);
        item.setQuantity(quantity);
        return orderItemRepository.save(item);
    }

    public void deleteOrderItem(Long orderId, Long itemId) {
        OrderItem item = getOrderItemById(orderId, itemId);
        orderItemRepository.delete(item);
    }
}
