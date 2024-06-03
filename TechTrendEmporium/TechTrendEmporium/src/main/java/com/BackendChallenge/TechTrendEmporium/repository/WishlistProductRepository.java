package com.BackendChallenge.TechTrendEmporium.repository;

import com.BackendChallenge.TechTrendEmporium.entity.WishlistProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistProductRepository extends JpaRepository<WishlistProduct, Long> {
    List<WishlistProduct> findByWishlistId(Long wishlistId);
    WishlistProduct findByWishlistIdAndProductId(Long id, Long productId);
}