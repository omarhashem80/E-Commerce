package com.ecommerce.shop.proxies;

import com.ecommerce.shop.dtos.TransactionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient(name = "wallet")
public interface WalletProxy {
    @PostMapping("wallet/wallets/{userId}/transactions")
    void makeTransaction(@PathVariable Long userId, TransactionDTO transactionDTO);
}
