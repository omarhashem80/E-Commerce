package com.ecommerce.shop.services;

import com.ecommerce.shop.dtos.QuantityRequest;
import com.ecommerce.shop.dtos.TransactionDTO;
import com.ecommerce.shop.dtos.TransactionType;
import com.ecommerce.shop.entities.Order;
import com.ecommerce.shop.entities.OrderItem;
import com.ecommerce.shop.entities.Status;
import com.ecommerce.shop.proxies.ProductProxy;
import com.ecommerce.shop.proxies.WalletProxy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderOrchestrationServiceImpl implements OrderOrchestrationService {
    private final OrderService orderService;
    private final WalletProxy walletProxy;
    private final ProductProxy productProxy;

    @Override
    public Order createOrderWithPayments(Long userId) {
        Order order = orderService.createOrder(userId);
        List<OrderItem> orderItems = order.getOrderItems();

        BigDecimal amount = orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        walletProxy.makeTransaction(userId, new TransactionDTO(amount, TransactionType.WITHDRAWAL));


        for (OrderItem item : orderItems) {
            productProxy.sell(item.getProductId(), new QuantityRequest(item.getQuantity()));
        }
        order.setStatus(Status.PAID);
        orderService.updateOrderStatus(order.getOrderId(), order.getStatus());
        return order;
    }
}
