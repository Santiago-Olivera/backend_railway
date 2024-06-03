package com.BackendChallenge.TechTrendEmporium.controller;

import com.BackendChallenge.TechTrendEmporium.dto.ProductDTO;
import com.BackendChallenge.TechTrendEmporium.entity.Product;
import com.BackendChallenge.TechTrendEmporium.entity.ProductStatus;
import com.BackendChallenge.TechTrendEmporium.entity.ProductUpdateRequest;
import com.BackendChallenge.TechTrendEmporium.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String price,
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {

        List<Product> products;
        if (price != null) {
            products = productService.getAllProductsSortedByPrice(price);
        } else if (title != null) {
            products = productService.getAllProductsSortedByTitle(title);
        } else if (category != null) {
            products = productService.getProductsByCategory(category);
            if (products == null || products.isEmpty()) {
                return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND);
            }
        } else {
            products = productService.getAllProducts();
        }
        if (products == null || products.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Product> filteredProducts = productService.filterApprovedProducts(products);
        List<Product> paginatedProducts = productService.getPaginatedList(filteredProducts, page, size);
        List<ProductDTO> productDTOs = new ArrayList<>();
        for (Product product : paginatedProducts) {
            productDTOs.add(productService.convertToDTO(product));
        }

        return new ResponseEntity<>(productDTOs, HttpStatus.OK);
    }




    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product, Authentication authentication) {
        try {
            Product createdProduct;
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
                // Admin can create product directly
                createdProduct = productService.createProduct(product);
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Product created successfully");
                response.put("productId", createdProduct.getId());
                return ResponseEntity.ok().body(response);
            } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("EMPLOYEE"))) {
                // Employee can create product but needs approval
                product.setStatus(ProductStatus.PENDING_CREATION);
                createdProduct = productService.createProduct(product);
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Product creation pending approval");
                response.put("productId", createdProduct.getId());
                return ResponseEntity.ok().body(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("status", HttpStatus.FORBIDDEN.value());
                response.put("error", HttpStatus.FORBIDDEN.getReasonPhrase());
                response.put("message", "Access Denied");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
            response.put("message", "Invalid category: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    @DeleteMapping
    public ResponseEntity<?> deleteProduct(@RequestBody Map<String, Long> requestBody, Authentication authentication) {
        Long productId = requestBody.get("id");
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            productService.deleteProduct(productId);
            return ResponseEntity.ok(Map.of("message", "Deleted successfully"));
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("EMPLOYEE"))) {
            productService.pendingDeleteProduct(productId);
            return ResponseEntity.ok(Map.of("message", "Delete pending for approval"));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
    }

    @PutMapping
    public ResponseEntity<?> updateProduct(@RequestBody ProductUpdateRequest request) {
        try {
            productService.updateProduct(request);
            return ResponseEntity.ok().body(Map.of("message", "Updated successfully"));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

