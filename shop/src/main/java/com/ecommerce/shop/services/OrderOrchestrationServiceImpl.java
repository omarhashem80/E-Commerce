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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(OrderOrchestrationServiceImpl.class);

    @Override
    public Order createOrderWithPayments(Long userId) {
        Order order = orderService.createOrder(userId);
        List<OrderItem> orderItems = order.getOrderItems();

        try {
            for (OrderItem item : orderItems) {
                boolean inventoryReduced = reduceInventory(item.getProductId(), item.getQuantity());
                boolean walletWithdrawn = withdrawFromWallet(userId, item.getSupplierId(), item.getPrice());

                if (!inventoryReduced || !walletWithdrawn) {
                    logger.warn("Order processing failed, triggering saga rollback");
                    sagaContext.rollback();
                    order.setStatus(Status.CANCELLED);
                    orderService.updateOrderStatus(order.getOrderId(), order.getStatus());
                    return order;
                }
            }

            order.setStatus(Status.PAID);
            orderService.updateOrderStatus(order.getOrderId(), order.getStatus());
            sagaContext.clear();
            return order;

        } catch (Exception e) {
            logger.error("Unexpected error during order processing", e);
            sagaContext.rollback();
            order.setStatus(Status.CANCELLED);
            orderService.updateOrderStatus(order.getOrderId(), order.getStatus());
            throw new ServiceNotFoundException("Order processing failed due to service unavailability");
        }
    }

    @CircuitBreaker(name = "walletService", fallbackMethod = "walletFallback")
    public boolean withdrawFromWallet(Long senderId, Long receiverId, BigDecimal amount) {
        walletProxy.transferMoney(senderId, receiverId, new TransferDTO(amount));
        sagaContext.addCompensation(new WalletCompensationAction(walletProxy, senderId, receiverId, amount));
        return true;
    }

    @CircuitBreaker(name = "inventoryService", fallbackMethod = "inventoryFallback")
    public boolean reduceInventory(Long productId, Integer quantity) {
        productProxy.sell(productId, new QuantityRequest(quantity));
        sagaContext.addCompensation(new InventoryCompensationAction(productProxy, productId, quantity));
        return true;
    }

    public boolean walletFallback(Long senderId, Long receiverId, BigDecimal amount, Throwable t) {
        logger.warn("Wallet service unavailable for transfer from {} to {} amount {}: {}",
                senderId, receiverId, amount, t.getMessage());
        return false;
    }

    public boolean inventoryFallback(Long productId, Integer quantity, Throwable t) {
        logger.warn("Inventory service unavailable for product {} quantity {}: {}",
                productId, quantity, t.getMessage());
        return false;
    }
}
