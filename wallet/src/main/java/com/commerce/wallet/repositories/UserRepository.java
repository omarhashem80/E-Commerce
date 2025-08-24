package com.commerce.wallet.repositories;

import com.commerce.wallet.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
//    @Query("UPDATE User u SET u.active = false WHERE u.userId = :id")
//    void deleteById(Integer id);
}
