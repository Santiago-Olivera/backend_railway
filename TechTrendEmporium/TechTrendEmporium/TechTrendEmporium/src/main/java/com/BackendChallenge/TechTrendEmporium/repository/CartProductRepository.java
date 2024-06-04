package com.BackendChallenge.TechTrendEmporium.repository;

import com.BackendChallenge.TechTrendEmporium.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
    List<CartProduct> findByCartId(Long cartId);
    CartProduct findByCartIdAndProductId(Long id, Long productId);

    @Modifying
    @Transactional
    @Query("update CartProduct cp set cp.quantity = ?1 where cp.id = ?2")
    void updateQuantity(int quantity, Long id);
}