package com.ecommerce.wallet.services;

import com.ecommerce.wallet.dtos.TransactionDTO;
import com.ecommerce.wallet.dtos.TransferDTO;
import com.ecommerce.wallet.dtos.WalletDTO;
import com.ecommerce.wallet.entities.Wallet;
import com.ecommerce.wallet.entities.WalletTransaction;

import java.util.List;

public interface WalletService {

    WalletDTO getWalletByUserIdDTO(Long userId);

    List<WalletTransaction> getWalletTransactionsById(Long userId);

    Wallet makeTransaction(Long userId, TransactionDTO transactionDTO);

    Wallet transferMoney(Long senderId, Long receiverId, TransferDTO transferDTO);

    boolean isOwner(Long userId, String email);
}
