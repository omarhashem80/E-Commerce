package com.commerce.wallet.services;

import com.commerce.wallet.dtos.TransactionDTO;
import com.commerce.wallet.dtos.TransferDTO;
import com.commerce.wallet.dtos.WalletDTO;
import com.commerce.wallet.entities.Wallet;
import com.commerce.wallet.entities.WalletTransaction;

import java.util.List;

public interface WalletService {

    WalletDTO getWalletByUserIdDTO(Long userId);

    List<WalletTransaction> getWalletTransactionsById(Long userId);

    Wallet makeTransaction(Long userId, TransactionDTO transactionDTO);

    Wallet transferMoney(Long senderId, Long receiverId, TransferDTO transferDTO);

    boolean isOwner(Long userId, String email);
}
