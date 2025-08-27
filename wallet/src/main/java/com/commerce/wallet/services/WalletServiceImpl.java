package com.commerce.wallet.services;

import com.commerce.wallet.dtos.TransactionDTO;
import com.commerce.wallet.dtos.TransferDTO;
import com.commerce.wallet.dtos.WalletDTO;
import com.commerce.wallet.entities.TransactionType;
import com.commerce.wallet.entities.User;
import com.commerce.wallet.entities.Wallet;
import com.commerce.wallet.entities.WalletTransaction;
import com.commerce.wallet.exceptions.InvalidOperationException;
import com.commerce.wallet.exceptions.NotFoundException;
import com.commerce.wallet.repositories.UserRepository;
import com.commerce.wallet.repositories.WalletRepository;
import com.commerce.wallet.repositories.WalletTransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class WalletServiceImpl implements WalletService{

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final UserRepository userRepository;

    private Wallet getWalletByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || !user.isActive()) {
            throw new NotFoundException("User with id: " + userId + " not found");
        }
        Wallet wallet = user.getWallet();

        if (wallet == null || !wallet.isActive()) {
            throw new NotFoundException("User with id: "+ userId + " has no wallet");
        }

        return wallet;
    }

    @Override
    public WalletDTO getWalletByUserIdDTO(Long userId) {
        return WalletDTO.fromEntity(getWalletByUserId(userId));
    }

    @Override
    public List<WalletTransaction> getWalletTransactionsById(Long userId) {
        Wallet wallet = getWalletByUserId(userId);
        return wallet.getTransactions();
    }

    @Override
    public Wallet makeTransaction(Long userId, TransactionDTO transactionDTO) {
        Wallet wallet = getWalletByUserId(userId);

        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setAmount(transactionDTO.getAmount());
        walletTransaction.setType(transactionDTO.getType());
        walletTransaction.setWallet(wallet);
        walletTransaction.setActive(true);
        walletTransaction.setCreatedAt(LocalDateTime.now());

        wallet.addTransaction(walletTransaction);
        BigDecimal balance = wallet.getBalance();
        if (walletTransaction.getType() == TransactionType.WITHDRAWAL && balance.compareTo(walletTransaction.getAmount()) < 0) {
            throw new InvalidOperationException("Insufficient balance");
        }
        if (walletTransaction.getType() == TransactionType.DEPOSIT) {
            wallet.setBalance(balance.add(walletTransaction.getAmount()));
        } else {
            wallet.setBalance(balance.subtract(walletTransaction.getAmount()));
        }
        walletTransactionRepository.save(walletTransaction);
        walletRepository.save(wallet);

        return wallet;
    }

    @Override
    public Wallet transferMoney(Long senderId, Long receiverId, TransferDTO transferDTO) {
        Wallet senderWallet = getWalletByUserId(senderId);
        Wallet receiverWallet = getWalletByUserId(receiverId);
        makeTransaction(senderId, new TransactionDTO(transferDTO.getAmount(), TransactionType.WITHDRAWAL));
        return makeTransaction(receiverId, new TransactionDTO(transferDTO.getAmount(), TransactionType.DEPOSIT));
    }

    @Override
    public boolean isOwner(Long userId, String email) {
        Wallet wallet = getWalletByUserId(userId);
        return wallet.getUser().getEmail().equals(email);
    }
}
