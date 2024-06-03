package com.BackendChallenge.TechTrendEmporium.controller;

import com.BackendChallenge.TechTrendEmporium.dto.CategoryDTO;
import com.BackendChallenge.TechTrendEmporium.entity.Category;
import com.BackendChallenge.TechTrendEmporium.entity.CategoryStatus;
import com.BackendChallenge.TechTrendEmporium.entity.CategoryUpdateRequest;
import com.BackendChallenge.TechTrendEmporium.entity.Product;
import com.BackendChallenge.TechTrendEmporium.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getApprovedCategories();
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category category, Authentication authentication) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            // Admin can create category directly
            categoryService.createCategory(category);
            return ResponseEntity.ok("Category created successfully");
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("EMPLOYEE"))) {
            // Employee can create category but needs approval
            category.setStatus(CategoryStatus.PENDING);
            categoryService.createCategory(category);
            return ResponseEntity.ok("Category creation pending approval");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

    }

    @DeleteMapping
    public ResponseEntity<?> deleteCategory(@RequestBody Map<String, Long> requestBody, Authentication authentication) {
        Long categoryId = requestBody.get("id");
        if (categoryId == null) {
            return ResponseEntity.badRequest().body("Category ID is required");
        }

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            // Admin can delete category directly
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.ok("Category deleted successfully");
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("EMPLOYEE"))) {
            // Employee can request deletion of category, status set to PENDING
            categoryService.requestCategoryDeletion(categoryId);
            return ResponseEntity.ok("Category deletion request pending approval");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
    }

    @PutMapping
    public ResponseEntity<?> updateCategory(@RequestBody CategoryUpdateRequest request) {
        try {
            categoryService.updateCategory(request);
            return ResponseEntity.ok().body(Map.of("message", "Updated successfully"));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}



