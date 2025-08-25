package com.ecommerce.shop.saga.actions;

import com.ecommerce.shop.dtos.TransferDTO;
import com.ecommerce.shop.proxies.WalletProxy;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
public class WalletCompensationAction implements CompensationAction {
    private final WalletProxy walletProxy;
    private final Long senderId;
    private final Long receiverId;
    private final BigDecimal amount;

    @Override
    public void compensate() {
        walletProxy.transferMoney(receiverId, senderId, new TransferDTO(amount));
    }
}

