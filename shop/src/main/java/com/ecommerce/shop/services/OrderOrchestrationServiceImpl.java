package com.ecommerce.shop.services;

import com.ecommerce.shop.dtos.QuantityRequest;
import com.ecommerce.shop.dtos.TransferDTO;
import com.ecommerce.shop.entities.Order;
import com.ecommerce.shop.entities.OrderItem;
import com.ecommerce.shop.entities.Status;
import com.ecommerce.shop.exceptions.ServiceNotFoundException;
import com.ecommerce.shop.proxies.ProductProxy;
import com.ecommerce.shop.proxies.WalletProxy;
import com.ecommerce.shop.saga.SagaContext;
import com.ecommerce.shop.saga.actions.InventoryCompensationAction;
import com.ecommerce.shop.saga.actions.WalletCompensationAction;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
    private final SagaContext sagaContext;

    @Override
    public Order createOrderWithPayments(Long userId) {
        Order order = orderService.createOrder(userId);
        List<OrderItem> orderItems = order.getOrderItems();

//        BigDecimal amount = orderItems.stream()
//                .map(OrderItem::getPrice)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);

        for (OrderItem item : orderItems) {
            reduceInventory(item.getProductId(), item.getQuantity());
            withdrawFromWallet(userId, item.getSupplierId(), item.getPrice());
        }

        order.setStatus(Status.PAID);
        orderService.updateOrderStatus(order.getOrderId(), order.getStatus());
        return order;
    }

    @CircuitBreaker(name = "walletService", fallbackMethod = "walletFallback")
    public void withdrawFromWallet(Long userId, Long receiverId, BigDecimal amount) {
        walletProxy.transferMoney(userId, receiverId, new TransferDTO(amount));
        sagaContext.addCompensation(new WalletCompensationAction(walletProxy, userId, receiverId, amount));
    }

    @CircuitBreaker(name = "inventoryService", fallbackMethod = "inventoryFallback")
    public void reduceInventory(Long productId, Integer quantity) {
        productProxy.sell(productId, new QuantityRequest(quantity));
        sagaContext.addCompensation(new InventoryCompensationAction(productProxy, productId, quantity));
    }

    public void walletFallback(Long userId, BigDecimal amount, Throwable t) {
        throw new ServiceNotFoundException("Wallet service unavailable, transaction aborted");
    }

    public void inventoryFallback(Long productId, Integer quantity, Throwable t) {
        throw new ServiceNotFoundException("Inventory service unavailable, transaction aborted");
    }

}
