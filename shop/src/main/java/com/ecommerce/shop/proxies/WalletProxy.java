package com.ecommerce.shop.proxies;

import com.ecommerce.shop.dtos.TransferDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient(name = "wallet")
public interface WalletProxy {
    @PostMapping("wallet/wallets/{senderId}/transactions/{receiverId}")
    void transferMoney(@PathVariable Long senderId, @PathVariable Long receiverId, TransferDTO transferDTO);
}
