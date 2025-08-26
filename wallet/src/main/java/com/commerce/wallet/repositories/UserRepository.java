package com.commerce.wallet.repositories;

import com.commerce.wallet.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);
    Optional<User> findByEmail(String email);
}
