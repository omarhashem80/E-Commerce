package com.ecommerce.wallet.services;

import com.ecommerce.wallet.dtos.TransactionDTO;
import com.ecommerce.wallet.dtos.TransferDTO;
import com.ecommerce.wallet.dtos.WalletDTO;
import com.ecommerce.wallet.entities.TransactionType;
import com.ecommerce.wallet.entities.User;
import com.ecommerce.wallet.entities.Wallet;
import com.ecommerce.wallet.entities.WalletTransaction;
import com.ecommerce.wallet.exceptions.InvalidOperationException;
import com.ecommerce.wallet.exceptions.NotFoundException;
import com.ecommerce.wallet.repositories.UserRepository;
import com.ecommerce.wallet.repositories.WalletRepository;
import com.ecommerce.wallet.repositories.WalletTransactionRepository;
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
