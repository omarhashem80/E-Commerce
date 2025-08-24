package com.ecommerce.inventory.proxies;

import com.ecommerce.inventory.dtos.TransactionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "wallet")
public interface WalletProxy {
    @PostMapping("wallet/wallets/{userId}/transactions")
    void makeTransaction(@PathVariable Long userId, TransactionDTO transactionDTO);
}
