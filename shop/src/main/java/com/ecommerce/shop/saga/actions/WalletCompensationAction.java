package com.ecommerce.shop.saga.actions;

import com.ecommerce.shop.dtos.TransactionDTO;
import com.ecommerce.shop.dtos.TransactionType;
import com.ecommerce.shop.proxies.WalletProxy;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
public class WalletCompensationAction implements CompensationAction {
    private final WalletProxy walletProxy;
    private final Long userId;
    private final BigDecimal amount;

    @Override
    public void compensate() {
        walletProxy.makeTransaction(userId, new TransactionDTO(amount, TransactionType.DEPOSIT));
    }
}

