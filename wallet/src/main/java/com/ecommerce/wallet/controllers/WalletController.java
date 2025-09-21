package com.ecommerce.wallet.controllers;

import com.ecommerce.wallet.dtos.TransactionDTO;
import com.ecommerce.wallet.dtos.TransferDTO;
import com.ecommerce.wallet.dtos.WalletDTO;
import com.ecommerce.wallet.entities.Wallet;
import com.ecommerce.wallet.entities.WalletTransaction;
import com.ecommerce.wallet.payloads.ResponseTemplate;
import com.ecommerce.wallet.services.WalletService;
import com.ecommerce.wallet.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Wallet Management APIs",
        description = "Endpoints for wallet operations, transactions, and money transfers"
)
@RestController
@RequestMapping("/wallets")
@AllArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @Operation(summary = "Get wallet by user ID", description = "Fetches the wallet details for a given user (Admin or the user themselves)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Wallet retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - not allowed to view this wallet"),
            @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isSelf(authentication, #userId)")
    public ResponseEntity<ResponseTemplate<WalletDTO>> getWalletById(@PathVariable Long userId) {
        WalletDTO walletDTO = walletService.getWalletByUserIdDTO(userId);
        return ResponseBuilder.build(HttpStatus.OK, "Wallet retrieved successfully", walletDTO);
    }

    @Operation(summary = "Get wallet transactions", description = "Fetches all wallet transactions for a given user (Admin or the user themselves)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Wallet transactions retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - not allowed to view transactions"),
            @ApiResponse(responseCode = "404", description = "Wallet or transactions not found")
    })
    @GetMapping("/{userId}/transactions")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isSelf(authentication, #userId)")
    public ResponseEntity<ResponseTemplate<List<WalletTransaction>>> getWalletTransactionsById(@PathVariable Long userId) {
        List<WalletTransaction> walletTransactions = walletService.getWalletTransactionsById(userId);
        return ResponseBuilder.build(HttpStatus.OK, "Wallet transactions retrieved successfully", walletTransactions);
    }

    @Operation(summary = "Make a wallet transaction", description = "Creates a new transaction for a user (only the user themselves can do this)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transaction created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid transaction request"),
            @ApiResponse(responseCode = "403", description = "Forbidden - not allowed to create transaction")
    })
    @PostMapping("{userId}/transactions")
    @PreAuthorize("@userSecurity.isSelf(authentication, #userId)")
    public ResponseEntity<ResponseTemplate<Wallet>> makeTransaction(
            @PathVariable Long userId,
            @RequestBody TransactionDTO transactionDTO
    ) {
        Wallet wallet = walletService.makeTransaction(userId, transactionDTO);
        return ResponseBuilder.build(HttpStatus.CREATED, "Transaction created successfully", wallet);
    }

    @Operation(summary = "Transfer money between users", description = "Transfers money from one user’s wallet to another")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Money transferred successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid transfer request"),
            @ApiResponse(responseCode = "403", description = "Forbidden - not allowed to transfer"),
            @ApiResponse(responseCode = "404", description = "Sender or receiver wallet not found")
    })
    @PostMapping("{senderId}/transactions/{receiverId}")
    public ResponseEntity<ResponseTemplate<Wallet>> transferMoney(
            @PathVariable Long senderId,
            @PathVariable Long receiverId,
            @RequestBody TransferDTO transferDTO
    ) {
        Wallet wallet = walletService.transferMoney(senderId, receiverId, transferDTO);
        return ResponseBuilder.build(HttpStatus.CREATED, "Transaction created successfully", wallet);
    }
}
