package com.BackendChallenge.TechTrendEmporium.repository;

import com.BackendChallenge.TechTrendEmporium.entity.Category;
import com.BackendChallenge.TechTrendEmporium.entity.CategoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
    List<Category> findByStatus(CategoryStatus status);
    @Query("SELECT c FROM Category c WHERE c.status IN (:statuses) OR c.status IS NULL")
    List<Category> findByStatusInOrStatusIsNull(List<CategoryStatus> statuses);


}

