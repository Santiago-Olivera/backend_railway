package com.BackendChallenge.TechTrendEmporium.repository;

import com.BackendChallenge.TechTrendEmporium.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);
    Optional<Cart> findByUserIdAndStatus(Long userId, String status);

    List<Cart> findAllByUserId(Long userId);
}
