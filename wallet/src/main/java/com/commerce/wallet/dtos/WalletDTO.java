package com.commerce.wallet.dtos;

import com.commerce.wallet.entities.Role;
import com.commerce.wallet.entities.User;
import com.commerce.wallet.entities.Wallet;
import com.commerce.wallet.entities.WalletTransaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletDTO {

    private Long walletId;
    private Long userId;
    private String firstName;
    private String lastName;
    private Role role;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private List<WalletTransaction> transactions;

    public static WalletDTO fromEntity(Wallet wallet) {
        if (wallet == null) return null;

        WalletDTO dto = new WalletDTO();
        dto.setWalletId(wallet.getWalletId());
        User user = wallet.getUser();
        dto.setUserId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole());
        dto.setBalance(wallet.getBalance());
        dto.setCreatedAt(wallet.getCreatedAt());
        dto.setTransactions(wallet.getTransactions());
        return dto;
    }

}
