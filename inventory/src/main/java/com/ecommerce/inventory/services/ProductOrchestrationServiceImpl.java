package com.ecommerce.inventory.services;

import com.ecommerce.inventory.dtos.TransactionDTO;
import com.ecommerce.inventory.dtos.TransactionType;
import com.ecommerce.inventory.entities.Product;
import com.ecommerce.inventory.proxies.WalletProxy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class ProductOrchestrationServiceImpl implements ProductOrchestrationService {
    private final ProductService productService;
    private final WalletProxy walletProxy;

    @Override
    public Product handlePayments(Long productId, Integer quantity) {
        Product product = productService.sellQuantity(productId, quantity);
        walletProxy.makeTransaction(product.getSupplierId(), new TransactionDTO(product.getPrice().multiply(BigDecimal.valueOf(quantity)), TransactionType.DEPOSIT));
        return product;
    }


}
