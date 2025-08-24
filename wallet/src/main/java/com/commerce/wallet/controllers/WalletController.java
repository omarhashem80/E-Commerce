package com.commerce.wallet.controllers;

import com.commerce.wallet.dtos.TransactionDTO;
import com.commerce.wallet.dtos.WalletDTO;
import com.commerce.wallet.entities.Wallet;
import com.commerce.wallet.entities.WalletTransaction;
import com.commerce.wallet.services.WalletService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallets")
@AllArgsConstructor
public class WalletController {
    private final WalletService walletService;

    // get wallet
    @GetMapping("/{userId}")
    public WalletDTO getWalletById(@PathVariable Long userId) {
        return walletService.getWalletByUserIdDTO(userId);
    }

    // get wallet transactions
    @GetMapping("/{userId}/transactions")
    public List<WalletTransaction> getWalletTransactionsById(@PathVariable Long userId) {
        return walletService.getWalletTransactionsById(userId);
    }

    // add money to the wallet
    @PostMapping("{userId}/transactions")
    public Wallet makeTransaction(@PathVariable Long userId, @RequestBody TransactionDTO transactionDTO) {
        return walletService.makeTransaction(userId, transactionDTO);
    }

}
