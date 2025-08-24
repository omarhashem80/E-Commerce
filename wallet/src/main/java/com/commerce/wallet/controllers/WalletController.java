package com.commerce.wallet.controllers;

import com.commerce.wallet.dtos.TransactionDTO;
import com.commerce.wallet.dtos.WalletDTO;
import com.commerce.wallet.entities.Wallet;
import com.commerce.wallet.entities.WalletTransaction;
import com.commerce.wallet.payloads.ApiResponse;
import com.commerce.wallet.services.WalletService;
import com.commerce.wallet.utils.ResponseBuilder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallets")
@AllArgsConstructor
public class WalletController {
    private final WalletService walletService;

    // get wallet
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<WalletDTO>> getWalletById(@PathVariable Long userId) {
        WalletDTO walletDTO = walletService.getWalletByUserIdDTO(userId);
        return ResponseBuilder.build(HttpStatus.OK, "Wallet retrieved successfully", walletDTO);
    }

    // get wallet transactions
    @GetMapping("/{userId}/transactions")
    public ResponseEntity<ApiResponse<List<WalletTransaction>>> getWalletTransactionsById(@PathVariable Long userId) {
        List<WalletTransaction> walletTransactions = walletService.getWalletTransactionsById(userId);
        return ResponseBuilder.build(HttpStatus.OK, "Wallet transactions retrieved successfully", walletTransactions);
    }

    // add money to the wallet
    @PostMapping("{userId}/transactions")
    public ResponseEntity<ApiResponse<Wallet>> makeTransaction(@PathVariable Long userId, @RequestBody TransactionDTO transactionDTO) {
        Wallet wallet = walletService.makeTransaction(userId, transactionDTO);
        return ResponseBuilder.build(HttpStatus.CREATED, "Transaction created successfully", wallet);
    }
}
