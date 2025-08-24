package com.ecommerce.shop.repositories;

import com.ecommerce.shop.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUserId(Long userId);

    void deleteByUserId(Long userId);

    void deleteByCartItemIdAndUserId(Long cartItemId, Long userId);

    Optional<CartItem> findByCartItemIdAndUserId(Long cartItemId, Long userId);
}
