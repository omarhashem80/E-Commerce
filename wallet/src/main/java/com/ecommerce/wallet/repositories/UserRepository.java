package com.ecommerce.wallet.repositories;

import com.ecommerce.wallet.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);
    boolean existsByEmail(String email);
    boolean existsByUserName(String username);
    Optional<User> findByEmail(String email);
}
