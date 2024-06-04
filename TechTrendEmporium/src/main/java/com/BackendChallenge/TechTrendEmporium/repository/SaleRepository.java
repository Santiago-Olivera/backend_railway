package com.BackendChallenge.TechTrendEmporium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.BackendChallenge.TechTrendEmporium.entity.Sale;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findByCartUserId(Long userId);

    Iterable<? extends Sale> findByCartId(Long id);
}