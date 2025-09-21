package com.ecommerce.wallet.repositories;

import com.ecommerce.wallet.entities.PasswordResetToken;
import com.ecommerce.wallet.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteAllByUser(User user);
}
